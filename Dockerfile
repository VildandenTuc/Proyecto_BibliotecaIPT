# Dockerfile Multi-Stage para Sistema de Biblioteca IPT

# ===========================
# Stage 1: Build
# ===========================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Descargar dependencias (capa cacheada)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar aplicación (skip tests para build más rápido)
RUN ./mvnw clean package -DskipTests

# ===========================
# Stage 2: Runtime
# ===========================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring

# Copiar JAR desde stage de build
COPY --from=build /app/target/*.jar app.jar

# Cambiar ownership
RUN chown spring:spring app.jar

# Cambiar a usuario no-root
USER spring:spring

# Exponer puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Variables de entorno con valores por defecto
ENV JAVA_OPTS="-Xmx512m -Xms256m" \
    DB_HOST=localhost \
    DB_PORT=3306 \
    DB_NAME=dbbiblioteca \
    DB_USER=biblioteca_user \
    DB_PASSWORD=changeme \
    JWT_SECRET=ChangeThisSecretKeyInProduction32Chars \
    JWT_EXPIRATION=86400000

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar \
    --spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME} \
    --spring.datasource.username=${DB_USER} \
    --spring.datasource.password=${DB_PASSWORD} \
    --biblioteca.jwt.secret=${JWT_SECRET} \
    --biblioteca.jwt.expiration=${JWT_EXPIRATION}"]