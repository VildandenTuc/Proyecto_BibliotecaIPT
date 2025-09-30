#!/bin/bash

# ===========================
# Script de Deployment para Sistema de Biblioteca IPT
# ===========================

set -e

echo "=========================================="
echo "   Sistema de Biblioteca IPT - Deploy"
echo "=========================================="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Función para imprimir mensajes
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

# Verificar Java
echo "Verificando Java..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    print_success "Java $JAVA_VERSION detectado"
else
    print_error "Java no encontrado. Instalar Java 17 o superior."
    exit 1
fi

# Verificar Maven
echo "Verificando Maven..."
if [ -f "./mvnw" ]; then
    print_success "Maven Wrapper encontrado"
else
    print_error "Maven Wrapper no encontrado."
    exit 1
fi

# Verificar MySQL
echo "Verificando conexión a MySQL..."
if command -v mysql &> /dev/null; then
    print_success "MySQL CLI encontrado"
else
    print_warning "MySQL CLI no encontrado. Asegurar que MySQL esté corriendo."
fi

# Limpiar builds previos
echo ""
echo "Limpiando builds previos..."
./mvnw clean
print_success "Build anterior limpiado"

# Ejecutar tests
echo ""
echo "Ejecutando tests..."
if ./mvnw test; then
    print_success "Todos los tests pasaron"
else
    print_error "Tests fallaron. Revisar errores antes de deployar."
    exit 1
fi

# Compilar aplicación
echo ""
echo "Compilando aplicación..."
if ./mvnw package -DskipTests; then
    print_success "Aplicación compilada exitosamente"
else
    print_error "Compilación falló"
    exit 1
fi

# Verificar JAR generado
JAR_FILE=$(find target -name "*.jar" -type f | head -n 1)
if [ -f "$JAR_FILE" ]; then
    JAR_SIZE=$(du -h "$JAR_FILE" | cut -f1)
    print_success "JAR generado: $JAR_FILE ($JAR_SIZE)"
else
    print_error "JAR no encontrado"
    exit 1
fi

# Opciones de deployment
echo ""
echo "=========================================="
echo "Seleccionar tipo de deployment:"
echo "=========================================="
echo "1) Local (ejecutar directamente)"
echo "2) Docker (construir imagen)"
echo "3) Docker Compose (stack completo)"
echo "4) Solo compilar (no ejecutar)"
echo ""
read -p "Opción [1-4]: " DEPLOY_OPTION

case $DEPLOY_OPTION in
    1)
        echo ""
        print_warning "Iniciando aplicación en modo local..."
        echo "Presionar Ctrl+C para detener"
        echo ""
        java -jar "$JAR_FILE"
        ;;
    2)
        echo ""
        echo "Construyendo imagen Docker..."
        if docker build -t biblioteca-ipt:latest .; then
            print_success "Imagen Docker construida: biblioteca-ipt:latest"
            echo ""
            echo "Para ejecutar:"
            echo "docker run -p 8080:8080 \\"
            echo "  -e DB_HOST=host.docker.internal \\"
            echo "  -e DB_USER=biblioteca_user \\"
            echo "  -e DB_PASSWORD=tu_password \\"
            echo "  biblioteca-ipt:latest"
        else
            print_error "Error construyendo imagen Docker"
            exit 1
        fi
        ;;
    3)
        echo ""
        echo "Iniciando stack completo con Docker Compose..."
        if docker-compose up -d; then
            print_success "Stack iniciado exitosamente"
            echo ""
            echo "Servicios corriendo:"
            docker-compose ps
            echo ""
            echo "Logs: docker-compose logs -f"
            echo "Detener: docker-compose down"
        else
            print_error "Error iniciando Docker Compose"
            exit 1
        fi
        ;;
    4)
        print_success "Compilación completada. No se ejecutará la aplicación."
        ;;
    *)
        print_error "Opción inválida"
        exit 1
        ;;
esac

echo ""
echo "=========================================="
print_success "Deployment completado exitosamente!"
echo "=========================================="
echo ""
echo "Información útil:"
echo "- URL: http://localhost:8080"
echo "- API Docs: http://localhost:8080/api/"
echo "- Health: http://localhost:8080/actuator/health (si está habilitado)"
echo ""
echo "Usuario admin por defecto:"
echo "- Email: admin@biblioteca.com"
echo "- Password: admin123"
echo "⚠ IMPORTANTE: Cambiar estas credenciales en producción"
echo ""