# Changelog

Todos los cambios notables de este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/),
y este proyecto adhiere a [Semantic Versioning](https://semver.org/lang/es/).

## [0.1.0] - 2025-09-30

### ✨ Agregado

#### Autenticación y Seguridad
- Sistema de autenticación con JWT
- Endpoint de registro de usuarios (`POST /auth/register`)
- Endpoint de login (`POST /auth/login`)
- Validación de passwords seguros con requisitos:
  - Mínimo 8 caracteres
  - Al menos 1 mayúscula
  - Al menos 1 minúscula
  - Al menos 1 número
  - Al menos 1 carácter especial
- Encriptación de passwords con BCrypt
- Sistema de roles (USER, ADMIN)
- Protección de endpoints con Spring Security

#### Gestión de Usuarios
- CRUD completo de usuarios
- Tipos de usuario (ALUMNO, DOCENTE)
- Búsqueda por DNI
- Búsqueda por nombre y apellido
- Filtrado por tipo de usuario
- Sistema de baja lógica (campo activo)
- Paginación en listados

#### Gestión de Libros
- CRUD completo de libros
- Control de ejemplares disponibles
- Campo disponibilidad automática
- Búsqueda por autor
- Búsqueda por categoría
- Filtrado de libros disponibles
- Baja lógica y física
- Paginación en listados

#### Gestión de Categorías
- CRUD completo de categorías
- Búsqueda de categorías activas
- Baja lógica de categorías
- Listado con paginación

#### Sistema de Préstamos
- Registro de préstamos
- Control de disponibilidad al prestar
- Sistema de devoluciones
- Consulta de préstamos activos
- Consulta de préstamos por usuario
- Consulta de préstamos por libro
- Actualización automática de disponibilidad

#### Base de Datos
- Migraciones con Flyway
- 11 migraciones iniciales:
  - V1: Esquema inicial
  - V2: Constraint único para DNI
  - V3: Campo activo en usuarios
  - V4: Disponibilidad de libros
  - V5: Control de ejemplares
  - V6: Estructura de préstamos
  - V7: Campo activo en categorías
  - V8: Email único en usuarios
  - V9: Campo password
  - V10: Sistema de roles
  - V11: Usuario administrador inicial

#### Testing
- Suite de tests de integración para autenticación
- Tests de validación de passwords
- Tests de registro y login
- Tests de generación de tokens JWT
- 8 tests automatizados con cobertura completa

#### Documentación
- README.md completo con:
  - Badges de tecnologías
  - Instalación y configuración
  - Documentación completa de API
  - Ejemplos de uso
  - Guía de deployment
- CONTRIBUTING.md con estándares de contribución
- CHANGELOG.md para tracking de versiones
- LICENSE (MIT)
- application.properties.example
- .gitignore mejorado

### 🔧 Técnico

#### Tecnologías
- Spring Boot 3.5.3
- Java 17
- MySQL 8.0+
- Spring Data JPA
- Spring Security 6.x
- JWT (JJWT 0.12.6)
- Flyway 10.x
- Lombok
- Jakarta Validation
- JUnit 5

#### Arquitectura
- Patrón de capas (Controller, Service, Repository)
- DTOs para todas las operaciones
- Validaciones personalizadas
- Manejo centralizado de excepciones
- Inyección de dependencias
- Configuración externalizada

### 📝 Notas

#### Usuario Admin por Defecto
- Email: `admin@biblioteca.com`
- Password: `admin123`
- Rol: ADMIN
- ⚠️ Cambiar en producción

#### Configuración Inicial
- Base de datos: `dbbiblioteca`
- Puerto: `8080`
- JWT expiration: 24 horas
- Passwords: BCrypt con 10 rounds

### 🔒 Seguridad

- Validación de passwords con requisitos robustos
- Encriptación BCrypt para passwords
- Tokens JWT con expiración
- Endpoints protegidos con Spring Security
- Validación de entrada con Jakarta Validation
- Manejo seguro de credenciales
- CORS configurado

### 🐛 Conocido

Ninguno en esta versión inicial.

---

## [Unreleased]

### Planificado

- [ ] Endpoint de renovación de token
- [ ] Cambio de password
- [ ] Recuperación de password
- [ ] Sistema de notificaciones
- [ ] Exportación de reportes
- [ ] Historial de préstamos
- [ ] Multas por retraso
- [ ] Reservas de libros
- [ ] API de estadísticas
- [ ] Dashboard administrativo

---

## Formato

### Tipos de Cambios

- **Agregado** para nuevas características
- **Cambiado** para cambios en funcionalidad existente
- **Deprecado** para características que serán removidas
- **Removido** para características removidas
- **Corregido** para correcciones de bugs
- **Seguridad** para vulnerabilidades corregidas

---

[0.1.0]: https://github.com/usuario/biblioteca-ipt/releases/tag/v0.1.0