-- V9__add-password-to-usuarios.sql
ALTER TABLE usuarios
ADD COLUMN password VARCHAR(255) DEFAULT 'changeme' NOT NULL;
