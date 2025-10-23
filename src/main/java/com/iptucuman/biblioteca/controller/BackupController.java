package com.iptucuman.biblioteca.controller;

import com.iptucuman.biblioteca.dto.BackupInfoDTO;
import com.iptucuman.biblioteca.service.BackupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de backups de la base de datos
 * Solo accesible para usuarios con rol ADMIN
 */
@RestController
@RequestMapping("/api/backup")
@PreAuthorize("hasRole('ADMIN')")
public class BackupController {

    private static final Logger log = LoggerFactory.getLogger(BackupController.class);

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    /**
     * Genera y descarga un backup completo de la base de datos
     * POST /api/backup/export
     * @return archivo .sql del backup
     */
    @PostMapping("/export")
    public ResponseEntity<Resource> exportBackup() {
        try {
            log.info("Solicitud de generación de backup recibida");

            // Generar backup
            String filename = backupService.generateBackup();

            // Obtener el archivo para descarga
            Resource resource = backupService.getBackupFile(filename);

            // Preparar headers para descarga
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            log.info("Backup generado y listo para descarga: {}", filename);

            return ResponseEntity.ok()
                .headers(headers)
                .body(resource);

        } catch (Exception e) {
            log.error("Error al generar backup", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }

    /**
     * Restaura la base de datos desde un archivo de backup
     * POST /api/backup/import
     * @param file archivo .sql con el backup
     * @return mensaje de confirmación
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importBackup(
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {
            log.info("Solicitud de restauración de backup recibida. Archivo: {}",
                file.getOriginalFilename());

            // Validaciones
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "El archivo no puede estar vacío");
                return ResponseEntity.badRequest().body(response);
            }

            String filename = file.getOriginalFilename();
            if (filename == null || !filename.endsWith(".sql")) {
                response.put("success", false);
                response.put("message", "Solo se permiten archivos .sql");
                return ResponseEntity.badRequest().body(response);
            }

            // Restaurar backup
            backupService.restoreBackup(file);

            response.put("success", true);
            response.put("message", "Backup restaurado exitosamente");
            response.put("filename", filename);

            log.info("Backup restaurado exitosamente desde: {}", filename);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al restaurar backup: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("Error al restaurar backup", e);
            response.put("success", false);
            response.put("message", "Error al restaurar backup: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
        }
    }

    /**
     * Lista todos los backups disponibles en el servidor
     * GET /api/backup/list
     * @return lista de BackupInfoDTO
     */
    @GetMapping("/list")
    public ResponseEntity<List<BackupInfoDTO>> listBackups() {
        try {
            log.info("Solicitud de lista de backups recibida");

            List<BackupInfoDTO> backups = backupService.listBackups();

            log.info("Lista de backups obtenida. Total: {}", backups.size());

            return ResponseEntity.ok(backups);

        } catch (Exception e) {
            log.error("Error al listar backups", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }

    /**
     * Descarga un backup específico
     * GET /api/backup/download/{filename}
     * @param filename nombre del archivo a descargar
     * @return archivo .sql del backup
     */
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadBackup(@PathVariable String filename) {
        try {
            log.info("Solicitud de descarga de backup: {}", filename);

            // Obtener el archivo
            Resource resource = backupService.getBackupFile(filename);

            // Preparar headers para descarga
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            log.info("Backup listo para descarga: {}", filename);

            return ResponseEntity.ok()
                .headers(headers)
                .body(resource);

        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al descargar backup: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);

        } catch (Exception e) {
            log.error("Error al descargar backup: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }

    /**
     * Elimina un backup específico
     * DELETE /api/backup/{filename}
     * @param filename nombre del archivo a eliminar
     * @return mensaje de confirmación
     */
    @DeleteMapping("/{filename}")
    public ResponseEntity<Map<String, Object>> deleteBackup(@PathVariable String filename) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("Solicitud de eliminación de backup: {}", filename);

            backupService.deleteBackup(filename);

            response.put("success", true);
            response.put("message", "Backup eliminado exitosamente");
            response.put("filename", filename);

            log.info("Backup eliminado: {}", filename);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al eliminar backup: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("Error al eliminar backup: {}", filename, e);
            response.put("success", false);
            response.put("message", "Error al eliminar backup: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
        }
    }
}
