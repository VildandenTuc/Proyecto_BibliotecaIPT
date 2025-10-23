package com.iptucuman.biblioteca.dto;

import java.util.Date;

/**
 * DTO para información de backups de la base de datos
 */
public record BackupInfoDTO(
        String filename,
        Long sizeBytes,
        Date createdDate
) {
}
