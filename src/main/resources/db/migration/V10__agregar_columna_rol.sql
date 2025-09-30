-- 1. Agregar la nueva columna "rol" a la tabla usuarios
ALTER TABLE usuarios
ADD COLUMN rol VARCHAR(50) NOT NULL DEFAULT 'USER';

-- 2. Asignar rol ADMIN al usuario con idUsuario = 1
UPDATE usuarios
SET rol = 'ADMIN'
WHERE id_usuario = 1;
