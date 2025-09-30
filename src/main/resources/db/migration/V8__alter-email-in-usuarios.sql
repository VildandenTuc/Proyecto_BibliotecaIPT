-- Eliminar posibles NULL antes de aplicar restricción NOT NULL
UPDATE usuarios
SET email = CONCAT('usuario', id_usuario, '@example.com')
WHERE email IS NULL;

-- Asegurarnos de que no existan duplicados antes de UNIQUE
-- (si hay duplicados reales deberás resolverlos manualmente)
-- Ejemplo: dejar solo el menor id_usuario
-- DELETE FROM usuarios WHERE id_usuario NOT IN (
--   SELECT MIN(id_usuario) FROM usuarios GROUP BY email
-- );

-- Modificar la columna email a NOT NULL
ALTER TABLE usuarios
    MODIFY COLUMN email VARCHAR(100) NOT NULL;

-- Agregar restricción de unicidad
ALTER TABLE usuarios
    ADD CONSTRAINT unique_email UNIQUE (email);
