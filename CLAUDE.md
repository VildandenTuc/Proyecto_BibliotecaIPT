# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
This is a Spring Boot library management system (Sistema de gestión de biblioteca) using Maven build tool. The application manages books, users, categories, and book loans with JWT authentication.

## Development Commands

### Build and Run
- `./mvnw spring-boot:run` - Start the application in development mode
- `./mvnw clean compile` - Compile the project
- `./mvnw clean package` - Build JAR file
- `./mvnw clean install` - Full build with tests

### Testing
- `./mvnw test` - Run all tests
- `./mvnw test -Dtest=ClassName` - Run specific test class

### Database Management
- Database migrations are managed by Flyway automatically on startup
- Migration files located in `src/main/resources/db/migration/`
- Follow naming convention: `V{version_number}__{description}.sql`

## Architecture

### Core Domain Models (src/main/java/com/iptucuman/biblioteca/modelo/)
- `Usuario` - User entity with authentication and role-based access
- `Libro` - Book entity with availability tracking
- `Categoria` - Book categories
- `Prestamo` - Book loan transactions
- `TipoUsuario` - User role enumeration

### Layered Architecture
- **Controllers** (`controller/`) - REST API endpoints with `/api/` prefix
- **Services** (`service/`) - Business logic layer
- **Repositories** (`repository/`) - JPA data access layer
- **DTOs** (`dto/`) - Data transfer objects for API communication
- **Security** (`security/`) - JWT authentication and Spring Security configuration

### Key Technologies
- Spring Boot 3.5.3 with Java 17
- Spring Data JPA with MySQL database
- Spring Security with JWT authentication
- Flyway for database migrations
- Lombok for reducing boilerplate code
- Jakarta Bean Validation for input validation

### Database Configuration
- Uses MySQL database named `dbbiblioteca`
- Connection configured in `application.properties`
- Hibernate validation mode prevents automatic schema changes
- All schema changes must be done through Flyway migrations

### Security Implementation
- JWT-based authentication with configurable secret and expiration
- Password encoding configured in `PasswordConfig`
- Authentication endpoints in `AuthController`
- Role-based access control using `TipoUsuario` enum

### API Structure
- Base URL: `/api/`
- Controllers follow RESTful conventions
- Request/Response handled through DTOs
- Pagination support for list endpoints
- Validation using Jakarta Bean Validation annotations

## Important Files
- `pom.xml` - Maven dependencies and build configuration
- `application.properties` - Application and database configuration
- `BibliotecaApplication.java` - Main Spring Boot application class
- `SecurityConfig.java` - Spring Security configuration
- Database migration files in `src/main/resources/db/migration/`

## Development Notes
- Uses Lombok annotations for getters/setters/constructors
- Follow existing naming conventions for DTOs (e.g., `RegistroDTO`, `DetalleDTO`, `RespuestaDTO`)
- All new database changes require Flyway migration files
- Authentication required for most endpoints except login/register
- Use constructor injection for dependencies in services and controllers