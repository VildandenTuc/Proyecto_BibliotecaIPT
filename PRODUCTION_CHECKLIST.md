# ✅ Checklist de Producción - Sistema Biblioteca IPT

Este documento contiene todos los pasos necesarios para preparar la aplicación para producción.

## 🔒 Seguridad

### Credenciales y Secrets

- [ ] **Cambiar JWT Secret**
  - Generar clave segura de al menos 64 caracteres
  - Usar variables de entorno: `BIBLIOTECA_JWT_SECRET`
  - No commitear en el código

- [ ] **Cambiar Password de Base de Datos**
  - Usar password fuerte (mínimo 16 caracteres)
  - Variables de entorno: `DB_USER`, `DB_PASSWORD`
  - Restringir acceso solo desde IPs conocidas

- [ ] **Cambiar Credenciales Admin**
  - Cambiar password de `admin@biblioteca.com`
  - O eliminar cuenta y crear nueva con datos reales
  - Ejecutar: `UPDATE usuarios SET password='$2a$...' WHERE email='admin@biblioteca.com';`

- [ ] **Deshabilitar Usuario de Test**
  - Eliminar `usuario@biblioteca.com` si existe
  - Verificar que no existan usuarios de prueba

### Configuración de Seguridad

- [ ] **HTTPS/SSL**
  - Configurar certificado SSL
  - Forzar HTTPS en todas las rutas
  - Configurar `server.ssl.*` en `application.properties`

- [ ] **CORS**
  - Configurar dominios permitidos
  - Eliminar `allowedOrigins("*")` si existe
  - Whitelist solo dominios conocidos

- [ ] **Rate Limiting**
  - Implementar límite de requests
  - Proteger endpoints de login/registro
  - Considerar Spring Cloud Gateway o Bucket4j

- [ ] **Headers de Seguridad**
  - X-Frame-Options: DENY
  - X-Content-Type-Options: nosniff
  - X-XSS-Protection: 1; mode=block
  - Content-Security-Policy

## 🗄️ Base de Datos

### Configuración

- [ ] **Revisar Connection Pool**
  - Configurar `spring.datasource.hikari.maximum-pool-size`
  - Ajustar según carga esperada
  - Monitorear uso de conexiones

- [ ] **Backup Automatizado**
  - Configurar backup diario
  - Probar proceso de restore
  - Almacenar backups en ubicación segura

- [ ] **Índices de Base de Datos**
  - Verificar índices en columnas frecuentemente consultadas
  - Agregar índices si es necesario:
    ```sql
    CREATE INDEX idx_usuarios_email ON usuarios(email);
    CREATE INDEX idx_libros_disponible ON libros(disponible);
    CREATE INDEX idx_prestamos_activo ON prestamos(activo);
    ```

- [ ] **Migración Flyway**
  - Verificar que todas las migraciones se ejecuten correctamente
  - Hacer backup antes de ejecutar migraciones en producción
  - `spring.flyway.baseline-on-migrate=true` solo en primera vez

### Rendimiento

- [ ] **Query Optimization**
  - Revisar queries lentas con `EXPLAIN`
  - Activar query cache si aplica
  - Considerar paginación en todos los listados

## ⚙️ Configuración de Aplicación

### application.properties

- [ ] **Deshabilitar DevTools**
  - Remover o comentar `spring-boot-devtools` de `pom.xml`
  - Ya está comentado ✅

- [ ] **Configurar Logging**
  ```properties
  logging.level.root=WARN
  logging.level.com.iptucuman.biblioteca=INFO
  logging.level.org.springframework.security=WARN
  logging.file.name=/var/log/biblioteca/application.log
  logging.file.max-size=10MB
  logging.file.max-history=30
  ```

- [ ] **JPA Settings**
  ```properties
  spring.jpa.show-sql=false
  spring.jpa.hibernate.ddl-auto=validate
  spring.jpa.open-in-view=false
  ```

- [ ] **Server Settings**
  ```properties
  server.port=8080
  server.compression.enabled=true
  server.http2.enabled=true
  ```

### Variables de Entorno

Configurar todas las credenciales como variables de entorno:

```bash
# Base de datos
export DB_HOST=production-db-host
export DB_PORT=3306
export DB_NAME=dbbiblioteca
export DB_USER=biblioteca_user
export DB_PASSWORD=secure_password_here

# JWT
export JWT_SECRET=super-secret-key-minimum-64-characters-production
export JWT_EXPIRATION=86400000

# Otros
export SPRING_PROFILES_ACTIVE=prod
```

## 🚀 Deployment

### Pre-Deployment

- [ ] **Tests Completos**
  ```bash
  ./mvnw clean test
  ```
  - Todos los tests deben pasar ✅

- [ ] **Compilación Exitosa**
  ```bash
  ./mvnw clean package -DskipTests
  ```

- [ ] **Verificar JAR**
  - Tamaño razonable (~50-100MB)
  - Contiene todas las dependencias

### Deployment Options

#### Opción 1: JAR Standalone

- [ ] **Crear script de inicio**
  ```bash
  #!/bin/bash
  java -Xmx1g -Xms512m \
    -Dspring.profiles.active=prod \
    -jar biblioteca.jar
  ```

- [ ] **Configurar como servicio systemd**
  ```ini
  [Unit]
  Description=Sistema Biblioteca IPT
  After=network.target mysql.service

  [Service]
  Type=simple
  User=biblioteca
  WorkingDirectory=/opt/biblioteca
  ExecStart=/usr/bin/java -jar /opt/biblioteca/biblioteca.jar
  Restart=always

  [Install]
  WantedBy=multi-user.target
  ```

#### Opción 2: Docker

- [ ] **Build de imagen**
  ```bash
  docker build -t biblioteca-ipt:v1.0.0 .
  ```

- [ ] **Push a Registry**
  ```bash
  docker tag biblioteca-ipt:v1.0.0 registry.company.com/biblioteca-ipt:v1.0.0
  docker push registry.company.com/biblioteca-ipt:v1.0.0
  ```

- [ ] **Docker Compose para producción**
  - Revisar `docker-compose.yml`
  - Ajustar variables de entorno
  - Configurar volúmenes persistentes

#### Opción 3: Cloud (AWS, Azure, GCP)

- [ ] **Configurar instancia/servidor**
- [ ] **Configurar RDS/Cloud SQL para MySQL**
- [ ] **Configurar Load Balancer**
- [ ] **Configurar Auto Scaling (opcional)**
- [ ] **Configurar Secrets Manager**

## 📊 Monitoreo

### Logs

- [ ] **Configurar agregación de logs**
  - ELK Stack, Splunk, o CloudWatch
  - Logs estructurados en JSON

- [ ] **Log Rotation**
  - Configurar rotación diaria
  - Retención de 30 días

### Métricas

- [ ] **Spring Boot Actuator**
  ```properties
  management.endpoints.web.exposure.include=health,info,metrics
  management.endpoint.health.show-details=always
  ```

- [ ] **APM (Application Performance Monitoring)**
  - New Relic, Datadog, o Prometheus
  - Monitorear:
    - Tiempo de respuesta
    - Errores 4xx/5xx
    - Uso de memoria/CPU
    - Conexiones DB

### Alertas

- [ ] **Configurar alertas para:**
  - Aplicación caída
  - Errores 5xx > 10/minuto
  - Uso de memoria > 90%
  - Base de datos no disponible
  - Disco > 80% lleno

## 🧪 Testing en Producción

### Smoke Tests

- [ ] **Login admin funciona**
  ```bash
  curl -X POST http://production-url/auth/login \
    -H "Content-Type: application/json" \
    -d '{"email":"admin@biblioteca.com","password":"NEW_PASSWORD"}'
  ```

- [ ] **Endpoints protegidos requieren auth**
- [ ] **Registro de usuario funciona**
- [ ] **CRUD de libros funciona**
- [ ] **Sistema de préstamos funciona**

### Performance Tests

- [ ] **Load Testing**
  - JMeter, Gatling, o k6
  - Simular 100+ usuarios concurrentes
  - Verificar tiempos de respuesta < 500ms

## 📝 Documentación

- [ ] **Actualizar README.md**
  - URL de producción
  - Endpoints reales
  - Contacto de soporte

- [ ] **Documentar API**
  - Swagger/OpenAPI
  - Postman Collection
  - Ejemplos de requests

- [ ] **Runbook de Operaciones**
  - Procedimiento de deployment
  - Rollback procedure
  - Troubleshooting común

## 🔄 Post-Deployment

### Verificación

- [ ] **Health check exitoso**
  ```bash
  curl http://production-url/actuator/health
  ```

- [ ] **Logs sin errores**
- [ ] **Base de datos conectada**
- [ ] **Migraciones ejecutadas**
- [ ] **Usuarios pueden hacer login**

### Monitoreo Inicial

- [ ] **Monitorear primeras 24 horas**
- [ ] **Revisar logs cada 2 horas**
- [ ] **Verificar métricas de performance**
- [ ] **Estar disponible para hotfixes**

## 📞 Contacto y Soporte

- [ ] **Definir equipo de soporte**
- [ ] **Configurar canal de comunicación (Slack, Teams)**
- [ ] **Documentar escalation path**
- [ ] **Crear procedimiento de incident response**

## 🎯 Métricas de Éxito

- [ ] **Uptime > 99.9%**
- [ ] **Tiempo de respuesta < 500ms**
- [ ] **0 errores críticos en primera semana**
- [ ] **Feedback positivo de usuarios**

---

## ✅ Checklist Rápido Final

Antes de hacer deploy a producción, verificar:

1. ✅ Tests pasan
2. ✅ Passwords cambiados
3. ✅ JWT secret configurado
4. ✅ HTTPS habilitado
5. ✅ Backup de DB configurado
6. ✅ Logs configurados
7. ✅ Monitoreo activo
8. ✅ Documentación actualizada
9. ✅ Rollback plan listo
10. ✅ Equipo de soporte notificado

---

**¡Éxito en el deployment! 🚀**