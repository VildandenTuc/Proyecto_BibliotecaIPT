package com.iptucuman.biblioteca.service;

import com.iptucuman.biblioteca.dto.BackupInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BackupService {

    private static final Logger log = LoggerFactory.getLogger(BackupService.class);

    @Value("${backup.directory}")
    private String backupDirectory;

    @Value("${backup.mysqldump.path}")
    private String mysqldumpPath;

    @Value("${backup.mysql.path}")
    private String mysqlPath;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    /**
     * Genera un backup completo de la base de datos usando mysqldump
     * @return nombre del archivo de backup generado
     * @throws Exception si ocurre un error durante el proceso
     */
    public String generateBackup() throws Exception {
        // Extraer nombre de BD de la URL
        String dbName = extractDatabaseName(dbUrl);

        // Generar nombre de archivo con timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "backup_" + dbName + "_" + timestamp + ".sql";
        String filepath = backupDirectory + File.separator + filename;

        // Crear directorio si no existe
        File dir = new File(backupDirectory);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new RuntimeException("No se pudo crear el directorio de backups");
            }
        }

        log.info("Iniciando generación de backup: {}", filename);

        // Ejecutar mysqldump (Windows) con ruta completa
        String command = String.format(
            "\"%s\" -u%s -p%s --add-drop-table --databases %s --result-file=\"%s\"",
            mysqldumpPath, dbUsername, dbPassword, dbName, filepath
        );

        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            // Leer errores si los hay
            if (exitCode != 0) {
                BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
                );
                String errorLine;
                StringBuilder errorOutput = new StringBuilder();
                while ((errorLine = errorReader.readLine()) != null) {
                    errorOutput.append(errorLine).append("\n");
                }
                errorReader.close();

                log.error("Error al generar backup. Exit code: {}. Error: {}", exitCode, errorOutput.toString());
                throw new RuntimeException("Error al generar backup: " + errorOutput.toString());
            }

            // Verificar que el archivo se haya creado
            File backupFile = new File(filepath);
            if (!backupFile.exists() || backupFile.length() == 0) {
                throw new RuntimeException("El archivo de backup no se generó correctamente");
            }

            // Log de auditoría
            logBackupAction("EXPORT", filename, getCurrentUser());
            log.info("Backup generado exitosamente: {}", filename);

            return filename;

        } catch (IOException | InterruptedException e) {
            log.error("Error al ejecutar mysqldump", e);
            throw new Exception("Error al generar backup: " + e.getMessage(), e);
        }
    }

    /**
     * Restaura la base de datos desde un archivo de backup
     * @param file archivo .sql con el backup
     * @throws Exception si ocurre un error durante el proceso
     */
    public void restoreBackup(MultipartFile file) throws Exception {
        // Validar archivo
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.endsWith(".sql")) {
            throw new IllegalArgumentException("Solo se permiten archivos .sql");
        }

        log.info("Iniciando restauración de backup desde: {}", originalFilename);

        // Guardar archivo temporalmente
        String tempFile = backupDirectory + File.separator + "temp_restore_" +
                         System.currentTimeMillis() + ".sql";
        File tempBackupFile = new File(tempFile);

        try {
            file.transferTo(tempBackupFile);

            // Extraer nombre de BD
            String dbName = extractDatabaseName(dbUrl);

            // Ejecutar restore con mysql (Windows) usando ruta completa
            String command = String.format(
                "cmd.exe /c \"\"%s\" -u%s -p%s %s < \"%s\"\"",
                mysqlPath, dbUsername, dbPassword, dbName, tempFile
            );

            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            // Leer errores si los hay
            if (exitCode != 0) {
                BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
                );
                String errorLine;
                StringBuilder errorOutput = new StringBuilder();
                while ((errorLine = errorReader.readLine()) != null) {
                    errorOutput.append(errorLine).append("\n");
                }
                errorReader.close();

                log.error("Error al restaurar backup. Exit code: {}. Error: {}", exitCode, errorOutput.toString());
                throw new RuntimeException("Error al restaurar backup: " + errorOutput.toString());
            }

            // Log de auditoría
            logBackupAction("IMPORT", originalFilename, getCurrentUser());
            log.info("Backup restaurado exitosamente desde: {}", originalFilename);

        } catch (IOException | InterruptedException e) {
            log.error("Error al restaurar backup", e);
            throw new Exception("Error al restaurar backup: " + e.getMessage(), e);
        } finally {
            // Eliminar archivo temporal
            if (tempBackupFile.exists()) {
                boolean deleted = tempBackupFile.delete();
                if (!deleted) {
                    log.warn("No se pudo eliminar el archivo temporal: {}", tempFile);
                }
            }
        }
    }

    /**
     * Lista todos los archivos de backup disponibles
     * @return lista de BackupInfoDTO ordenada por fecha descendente
     */
    public List<BackupInfoDTO> listBackups() {
        File dir = new File(backupDirectory);

        if (!dir.exists() || !dir.isDirectory()) {
            log.warn("El directorio de backups no existe: {}", backupDirectory);
            return Collections.emptyList();
        }

        File[] files = dir.listFiles((d, name) ->
            name.endsWith(".sql") && !name.startsWith("temp_restore_")
        );

        if (files == null || files.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.stream(files)
            .map(file -> new BackupInfoDTO(
                file.getName(),
                file.length(),
                new Date(file.lastModified())
            ))
            .sorted((a, b) -> b.createdDate().compareTo(a.createdDate()))
            .collect(Collectors.toList());
    }

    /**
     * Obtiene un archivo de backup específico como Resource
     * @param filename nombre del archivo
     * @return Resource del archivo
     * @throws Exception si el archivo no existe o no es válido
     */
    public Resource getBackupFile(String filename) throws Exception {
        // Validar nombre de archivo (prevenir path traversal)
        if (filename == null || filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            throw new IllegalArgumentException("Nombre de archivo no válido");
        }

        if (!filename.endsWith(".sql")) {
            throw new IllegalArgumentException("Solo se pueden descargar archivos .sql");
        }

        String filepath = backupDirectory + File.separator + filename;
        File file = new File(filepath);

        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException("El archivo de backup no existe: " + filename);
        }

        log.info("Descargando backup: {}", filename);
        return new FileSystemResource(file);
    }

    /**
     * Elimina un archivo de backup
     * @param filename nombre del archivo a eliminar
     * @throws Exception si el archivo no se puede eliminar
     */
    public void deleteBackup(String filename) throws Exception {
        // Validar nombre de archivo (prevenir path traversal)
        if (filename == null || filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            throw new IllegalArgumentException("Nombre de archivo no válido");
        }

        if (!filename.endsWith(".sql")) {
            throw new IllegalArgumentException("Solo se pueden eliminar archivos .sql");
        }

        String filepath = backupDirectory + File.separator + filename;
        File file = new File(filepath);

        if (!file.exists()) {
            throw new RuntimeException("El archivo de backup no existe: " + filename);
        }

        boolean deleted = file.delete();
        if (!deleted) {
            throw new RuntimeException("No se pudo eliminar el archivo: " + filename);
        }

        logBackupAction("DELETE", filename, getCurrentUser());
        log.info("Backup eliminado: {}", filename);
    }

    /**
     * Extrae el nombre de la base de datos de la URL JDBC
     * @param url URL JDBC
     * @return nombre de la base de datos
     */
    private String extractDatabaseName(String url) {
        // jdbc:mysql://localhost:3306/dbbiblioteca
        int lastSlash = url.lastIndexOf("/");
        String dbNameWithParams = url.substring(lastSlash + 1);

        // Remover parámetros si existen (?param=value)
        int questionMark = dbNameWithParams.indexOf("?");
        if (questionMark > 0) {
            return dbNameWithParams.substring(0, questionMark);
        }

        return dbNameWithParams;
    }

    /**
     * Registra las acciones de backup en los logs
     * @param action tipo de acción (EXPORT, IMPORT, DELETE)
     * @param filename nombre del archivo
     * @param user usuario que realizó la acción
     */
    private void logBackupAction(String action, String filename, String user) {
        log.info("[BACKUP] Action={}, File={}, User={}, Timestamp={}",
            action, filename, user, LocalDateTime.now());
    }

    /**
     * Obtiene el usuario autenticado actual
     * @return nombre de usuario o "SYSTEM" si no hay usuario autenticado
     */
    private String getCurrentUser() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            return "SYSTEM";
        }
    }
}
