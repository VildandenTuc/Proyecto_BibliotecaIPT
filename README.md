# 📚 Sistema de Gestión de Biblioteca IPT

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Sistema completo de gestión de biblioteca desarrollado con Spring Boot, diseñado para administrar libros, usuarios, categorías y préstamos con autenticación JWT y validaciones robustas.

## 🚀 Características Principales

- ✅ **Autenticación JWT** - Sistema seguro de login y registro con tokens
- 🔐 **Validación de Passwords** - Requisitos de seguridad (mayúsculas, minúsculas, números, símbolos)
- 📖 **Gestión de Libros** - CRUD completo con control de disponibilidad y ejemplares
- 👥 **Gestión de Usuarios** - Diferentes tipos (ALUMNO, DOCENTE) con roles (USER, ADMIN)
- 📑 **Categorías** - Organización de libros por categorías
- 📅 **Sistema de Préstamos** - Control de préstamos y devoluciones con fechas
- 🗃️ **Migraciones con Flyway** - Versionado y control de esquema de base de datos
- 🔍 **Búsquedas Avanzadas** - Filtros por nombre, autor, categoría, disponibilidad
- 📄 **Paginación** - Soporte para paginación en listados
- 🧪 **Tests Automatizados** - Suite completa de tests de integración

## 📋 Requisitos Previos

- **Java 17** o superior
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git** (opcional)

## 🛠️ Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/biblioteca-ipt.git
cd biblioteca-ipt
```

### 2. Configurar Base de Datos

Crear la base de datos MySQL:

```sql
CREATE DATABASE dbbiblioteca;
CREATE USER 'biblioteca_user'@'localhost' IDENTIFIED BY 'tu_password';
GRANT ALL PRIVILEGES ON dbbiblioteca.* TO 'biblioteca_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configurar `application.properties`

Editar `src/main/resources/application.properties`:

```properties
# Configuración de Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/dbbiblioteca
spring.datasource.username=biblioteca_user
spring.datasource.password=tu_password

# Configuración JWT
biblioteca.jwt.secret=TuClaveSecretaSuperSeguraMinimo32Caracteres
biblioteca.jwt.expiration=86400000
```

### 4. Compilar y Ejecutar

```bash
# Compilar el proyecto
./mvnw clean compile

# Ejecutar tests
./mvnw test

# Ejecutar aplicación
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 📚 Documentación de API

### Autenticación

#### Registro de Usuario

**Endpoint:** `POST /auth/register`

**Request:**
```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "dni": "40123456",
  "tipoUsuario": "ALUMNO",
  "email": "juan.perez@example.com",
  "telefono": "3815551234",
  "password": "SecurePass123!",
  "rol": "USER"
}
```

**Response:** `201 Created`
```json
{
  "idUsuario": 1,
  "nombre": "Juan",
  "apellido": "Pérez",
  "dni": "40123456",
  "tipoUsuario": "ALUMNO",
  "email": "juan.perez@example.com",
  "telefono": "3815551234",
  "activo": true
}
```

**Requisitos de Password:**
- Mínimo 8 caracteres
- Al menos 1 letra mayúscula
- Al menos 1 letra minúscula
- Al menos 1 número
- Al menos 1 carácter especial (!@#$%^&*()_+-=[]{}etc.)

#### Login

**Endpoint:** `POST /auth/login`

**Request:**
```json
{
  "email": "juan.perez@example.com",
  "password": "SecurePass123!"
}
```

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGV4YW1wbGUuY29tIiwiaWF0IjoxNzU5MjcxNjAxLCJleHAiOjE3NTkzNTgwMDF9..."
}
```

### Usuarios

| Endpoint | Método | Descripción | Auth |
|----------|--------|-------------|------|
| `/api/usuarios` | GET | Listar todos los usuarios (paginado) | ✅ |
| `/api/usuarios/{id}` | GET | Obtener usuario por ID | ✅ |
| `/api/usuarios` | POST | Crear nuevo usuario | ✅ |
| `/api/usuarios/{id}` | PUT | Actualizar usuario | ✅ |
| `/api/usuarios/{id}` | DELETE | Desactivar usuario (baja lógica) | ✅ |
| `/api/usuarios/activos` | GET | Listar usuarios activos | ✅ |
| `/api/usuarios/buscar/dni?dni={dni}` | GET | Buscar por DNI | ✅ |
| `/api/usuarios/buscar/nombre-apellido?texto={texto}` | GET | Buscar por nombre/apellido | ✅ |

### Libros

| Endpoint | Método | Descripción | Auth |
|----------|--------|-------------|------|
| `/api/libros` | GET | Listar todos los libros | ✅ |
| `/api/libros/{id}` | GET | Obtener libro por ID | ✅ |
| `/api/libros` | POST | Crear nuevo libro | ✅ |
| `/api/libros/{id}` | PUT | Actualizar libro | ✅ |
| `/api/libros/{id}` | DELETE | Eliminar libro (física) | ✅ |
| `/api/libros/logica/{id}` | DELETE | Eliminar libro (lógica) | ✅ |
| `/api/libros/disponibles` | GET | Listar libros disponibles (paginado) | ✅ |
| `/api/libros/categoria/{id}` | GET | Buscar por categoría (paginado) | ✅ |
| `/api/libros/autor?autor={autor}` | GET | Buscar por autor (paginado) | ✅ |

### Categorías

| Endpoint | Método | Descripción | Auth |
|----------|--------|-------------|------|
| `/api/categorias` | GET | Listar todas las categorías | ✅ |
| `/api/categorias/{id}` | GET | Obtener categoría por ID | ✅ |
| `/api/categorias` | POST | Crear nueva categoría | ✅ |
| `/api/categorias/{id}` | PUT | Actualizar categoría | ✅ |
| `/api/categorias/{id}` | DELETE | Eliminar categoría (física) | ✅ |
| `/api/categorias/activas` | GET | Listar categorías activas | ✅ |

### Préstamos

| Endpoint | Método | Descripción | Auth |
|----------|--------|-------------|------|
| `/api/prestamos` | GET | Listar todos los préstamos | ✅ |
| `/api/prestamos` | POST | Registrar nuevo préstamo | ✅ |
| `/api/prestamos/devolver` | PUT | Registrar devolución | ✅ |
| `/api/prestamos/activos` | GET | Listar préstamos activos | ✅ |
| `/api/prestamos/usuario/{id}` | GET | Préstamos por usuario | ✅ |
| `/api/prestamos/libro/{id}` | GET | Préstamos por libro | ✅ |

## 🔒 Autenticación

Para acceder a los endpoints protegidos, incluye el token JWT en el header:

```
Authorization: Bearer {tu_token_jwt}
```

**Ejemplo con curl:**
```bash
curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
     http://localhost:8080/api/usuarios
```

## 🗄️ Estructura de Base de Datos

### Tablas Principales

- **usuarios** - Información de usuarios del sistema
- **libros** - Catálogo de libros
- **categorias** - Categorías para organizar libros
- **prestamos** - Registro de préstamos y devoluciones

### Migraciones Flyway

Las migraciones se encuentran en `src/main/resources/db/migration/`:

- `V1__init_library_schema.sql` - Esquema inicial
- `V2__add_unique_constraint_dni.sql` - Constraint único para DNI
- `V3__add_field_activo_to_usuarios.sql` - Campo activo en usuarios
- `V4__add_column_disponible_to_libros.sql` - Disponibilidad de libros
- `V5__add_column_ejemplares_to_libros.sql` - Control de ejemplares
- `V6__actualizar_estructura_tabla_prestamos.sql` - Estructura de préstamos
- `V7__add_field_activo_to_categorias.sql` - Campo activo en categorías
- `V8__alter-email-in-usuarios.sql` - Email único en usuarios
- `V9__add-password-to-usuarios.sql` - Campo password
- `V10__agregar_columna_rol.sql` - Sistema de roles
- `V11__insert_usuario_admin.sql` - Usuario administrador inicial

## 🏗️ Arquitectura del Proyecto

```
src/
├── main/
│   ├── java/com/iptucuman/biblioteca/
│   │   ├── config/              # Configuraciones (PasswordEncoder)
│   │   ├── controller/          # Controladores REST
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── exception/           # Manejo de excepciones
│   │   ├── modelo/              # Entidades JPA
│   │   ├── repository/          # Repositorios JPA
│   │   ├── security/            # Seguridad y JWT
│   │   ├── service/             # Lógica de negocio
│   │   └── validation/          # Validaciones personalizadas
│   └── resources/
│       ├── application.properties  # Configuración principal
│       └── db/migration/          # Migraciones Flyway
└── test/
    └── java/com/iptucuman/biblioteca/
        └── controller/            # Tests de integración
```

## 🧪 Testing

### Ejecutar todos los tests:
```bash
./mvnw test
```

### Ejecutar tests específicos:
```bash
./mvnw test -Dtest=AuthControllerTest
```

### Coverage de Tests:
- ✅ Registro con password seguro
- ✅ Validación de passwords débiles
- ✅ Login exitoso y fallido
- ✅ Generación de tokens JWT
- ✅ Validación de emails duplicados

## 📦 Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Spring Boot | 3.5.3 | Framework principal |
| Spring Data JPA | 3.5.3 | Persistencia de datos |
| Spring Security | 6.x | Autenticación y autorización |
| JWT (JJWT) | 0.12.6 | Tokens de autenticación |
| MySQL | 8.0+ | Base de datos |
| Flyway | 10.x | Migraciones de BD |
| Lombok | 1.18.x | Reducción de boilerplate |
| Jakarta Validation | 3.x | Validaciones |
| JUnit 5 | 5.10.x | Testing |

## 🚀 Deployment

### Compilar JAR para producción:

```bash
./mvnw clean package -DskipTests
```

El archivo JAR se generará en `target/biblioteca-0.0.1-SNAPSHOT.jar`

### Ejecutar en producción:

```bash
java -jar target/biblioteca-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:mysql://tu-servidor:3306/dbbiblioteca \
  --spring.datasource.username=usuario \
  --spring.datasource.password=password \
  --biblioteca.jwt.secret=TuClaveSecretaProduccion
```

### Variables de Entorno Recomendadas:

```bash
export DB_URL=jdbc:mysql://localhost:3306/dbbiblioteca
export DB_USERNAME=biblioteca_user
export DB_PASSWORD=password_seguro
export JWT_SECRET=ClaveSecretaProduccionMinimo32Caracteres
export JWT_EXPIRATION=86400000
```

## 👥 Usuario Admin por Defecto

El sistema incluye un usuario administrador creado automáticamente:

- **Email:** `admin@biblioteca.com`
- **Password:** `admin123`
- **Rol:** `ADMIN`

⚠️ **IMPORTANTE:** Cambiar estas credenciales en producción.

## 📝 Ejemplos de Uso

### Ejemplo completo con Insomnia/Postman:

1. **Registrar usuario:**
```http
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "nombre": "María",
  "apellido": "González",
  "dni": "41234567",
  "tipoUsuario": "DOCENTE",
  "email": "maria.gonzalez@example.com",
  "telefono": "3815551234",
  "password": "SecurePass123!",
  "rol": "USER"
}
```

2. **Login:**
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "maria.gonzalez@example.com",
  "password": "SecurePass123!"
}
```

3. **Crear libro (con token):**
```http
POST http://localhost:8080/api/libros
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "El Quijote",
  "autor": "Miguel de Cervantes",
  "isbn": "9788491050247",
  "idCategoria": 1,
  "ejemplares": 5
}
```

4. **Registrar préstamo:**
```http
POST http://localhost:8080/api/prestamos
Authorization: Bearer {token}
Content-Type: application/json

{
  "idUsuario": 1,
  "idLibro": 1,
  "fechaPrestamo": "2025-09-30",
  "fechaDevolucionPrevista": "2025-10-14"
}
```

## 🤝 Contribución

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/NuevaCaracteristica`)
3. Commit tus cambios (`git commit -m 'Agregar nueva característica'`)
4. Push a la rama (`git push origin feature/NuevaCaracteristica`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 👨‍💻 Autor

Desarrollado por Guido Alfredo Albarracin {software developer} para el Instituto Privado Tucumán (IPT)

## 📞 Soporte

Para reportar bugs o solicitar nuevas características, por favor abre un issue en el repositorio.

---

**Desarrollado con ❤️ usando Spring Boot**