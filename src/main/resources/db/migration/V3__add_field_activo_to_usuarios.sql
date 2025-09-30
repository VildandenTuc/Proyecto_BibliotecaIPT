-- V3__add_field_activo_to_usuarios.sql

ALTER TABLE usuarios
ADD COLUMN activo BOOLEAN NOT NULL DEFAULT TRUE;
