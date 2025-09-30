-- V11__insert_usuario_admin.sql
-- Crea un usuario administrador de prueba
-- Email: admin@biblioteca.com
-- Password: admin123 (hasheado con BCrypt)

INSERT INTO usuarios (nombre, apellido, dni, tipo_usuario, email, telefono, activo, password, rol)
VALUES (
    'Administrador',
    'Sistema',
    '12345678',
    'DOCENTE',
    'admin@biblioteca.com',
    '3812345678',
    true,
    '$2a$10$XxDGNVMdP1JQAq0mAJOii.AfJ1GzThE8zqdJtu1vOvzJwHqfKy2Ca',
    'ADMIN'
);

-- Crear un usuario regular de prueba
-- Email: usuario@biblioteca.com
-- Password: usuario123 (hasheado con BCrypt)

INSERT INTO usuarios (nombre, apellido, dni, tipo_usuario, email, telefono, activo, password, rol)
VALUES (
    'Usuario',
    'Prueba',
    '87654321',
    'ALUMNO',
    'usuario@biblioteca.com',
    '3818765432',
    true,
    '$2a$10$LpyKxGWMmh4qkmRK977zlOh9f0cYmcL7FX/ZhnxpJTox.7vXVI7Qe',
    'USER'
);