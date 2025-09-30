# Guía de Contribución

¡Gracias por tu interés en contribuir al Sistema de Gestión de Biblioteca IPT! 🎉

## 📋 Tabla de Contenidos

- [Código de Conducta](#código-de-conducta)
- [¿Cómo puedo contribuir?](#cómo-puedo-contribuir)
- [Proceso de Desarrollo](#proceso-de-desarrollo)
- [Estándares de Código](#estándares-de-código)
- [Commits](#commits)
- [Pull Requests](#pull-requests)

## 📜 Código de Conducta

Este proyecto se adhiere a un código de conducta. Al participar, se espera que mantengas un ambiente respetuoso y constructivo.

## 🤝 ¿Cómo puedo contribuir?

### Reportar Bugs

Si encuentras un bug, por favor:

1. Verifica que no exista un issue similar
2. Crea un nuevo issue con:
   - Descripción clara del problema
   - Pasos para reproducir
   - Comportamiento esperado vs actual
   - Versión de Java, Spring Boot, MySQL
   - Logs relevantes

### Sugerir Mejoras

Para sugerir nuevas características:

1. Crea un issue describiendo:
   - El problema que resuelve
   - La solución propuesta
   - Ejemplos de uso
   - Alternativas consideradas

### Contribuir con Código

1. **Fork el repositorio**
2. **Crea una rama** desde `main`:
   ```bash
   git checkout -b feature/mi-nueva-caracteristica
   ```
3. **Realiza tus cambios**
4. **Ejecuta los tests**:
   ```bash
   ./mvnw test
   ```
5. **Commit y Push**
6. **Crea un Pull Request**

## 🔧 Proceso de Desarrollo

### Configuración del Entorno

1. Instala Java 17+
2. Instala Maven 3.6+
3. Instala MySQL 8.0+
4. Clona el repositorio
5. Configura `application.properties`
6. Ejecuta `./mvnw spring-boot:run`

### Estructura de Ramas

- `main` - Rama principal (producción)
- `develop` - Rama de desarrollo
- `feature/*` - Nuevas características
- `bugfix/*` - Correcciones de bugs
- `hotfix/*` - Correcciones urgentes

## 📝 Estándares de Código

### Nomenclatura

**Java:**
- Clases: `PascalCase` (ej: `UsuarioService`)
- Métodos: `camelCase` (ej: `registrarUsuario`)
- Constantes: `UPPER_SNAKE_CASE` (ej: `MAX_INTENTOS`)
- Variables: `camelCase` (ej: `emailUsuario`)

**Base de Datos:**
- Tablas: `snake_case` (ej: `usuarios`)
- Columnas: `snake_case` (ej: `fecha_prestamo`)

### Estilo de Código

- Usar **4 espacios** para indentación (no tabs)
- Líneas máximo **120 caracteres**
- Usar **Lombok** para getters/setters
- Validaciones con **Jakarta Validation**
- DTOs para todas las operaciones de API
- Servicios para lógica de negocio
- Repositorios solo para acceso a datos

### Tests

- **Nombrar tests descriptivamente:**
  ```java
  void testRegistroConPasswordSeguro() { }
  void testRegistroConPasswordDebil_DebeRechazar() { }
  ```

- **Patrón AAA (Arrange, Act, Assert):**
  ```java
  @Test
  void testCrearUsuario() {
      // Arrange
      UsuarioDTO dto = new UsuarioDTO(...);

      // Act
      Usuario resultado = service.crear(dto);

      // Assert
      assertNotNull(resultado);
      assertEquals("Juan", resultado.getNombre());
  }
  ```

- **Coverage mínimo:** 70% de cobertura

## 💬 Commits

### Formato de Commit Messages

Usar [Conventional Commits](https://www.conventionalcommits.org/):

```
<tipo>(<scope>): <descripción>

[cuerpo opcional]

[footer opcional]
```

**Tipos:**
- `feat`: Nueva característica
- `fix`: Corrección de bug
- `docs`: Cambios en documentación
- `style`: Formateo, espacios, etc.
- `refactor`: Refactorización de código
- `test`: Agregar o modificar tests
- `chore`: Tareas de mantenimiento

**Ejemplos:**
```
feat(auth): agregar validación de password seguro

fix(prestamos): corregir cálculo de fecha de devolución

docs(readme): actualizar instrucciones de instalación

test(usuarios): agregar tests para búsqueda por DNI
```

## 🔀 Pull Requests

### Checklist antes de crear PR

- [ ] El código compila sin errores
- [ ] Todos los tests pasan (`./mvnw test`)
- [ ] Se agregaron tests para nuevas funcionalidades
- [ ] La documentación está actualizada
- [ ] Se siguieron los estándares de código
- [ ] Los commits siguen Conventional Commits
- [ ] No hay conflictos con `main`

### Descripción del PR

**Plantilla:**

```markdown
## Descripción
Breve descripción de los cambios realizados.

## Tipo de Cambio
- [ ] Bug fix
- [ ] Nueva característica
- [ ] Breaking change
- [ ] Documentación

## ¿Cómo se probó?
Descripción de las pruebas realizadas.

## Checklist
- [ ] Tests agregados/actualizados
- [ ] Documentación actualizada
- [ ] Sin warnings de compilación
- [ ] Código revisado por autor
```

### Revisión de Código

Los PRs requieren:
- ✅ Al menos 1 aprobación
- ✅ Tests pasando
- ✅ Sin conflictos
- ✅ Código revisado

## 🎯 Prioridades

**Alta Prioridad:**
- Bugs de seguridad
- Pérdida de datos
- Crashes de aplicación

**Media Prioridad:**
- Bugs funcionales
- Mejoras de performance
- Refactoring importante

**Baja Prioridad:**
- Mejoras de UI/UX
- Documentación
- Tests adicionales

## 📞 Contacto

Para preguntas o ayuda:
- Abre un issue en GitHub
- Contacta al equipo de desarrollo

---

¡Gracias por contribuir! 🙌