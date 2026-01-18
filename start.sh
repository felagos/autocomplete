#!/bin/bash

# Script para inicializar y ejecutar el sistema completo

echo "🚀 Iniciando Sistema de Autocompletado..."
echo ""

# Verificar si Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker no está instalado. Por favor instala Docker Desktop."
    exit 1
fi

# Verificar si Docker Compose está instalado
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose no está instalado."
    exit 1
fi

echo "✅ Docker y Docker Compose detectados"
echo ""

# Preguntar modo de ejecución
echo "Selecciona el modo de ejecución:"
echo "1) Producción (PostgreSQL)"
echo "2) Desarrollo (H2)"
read -p "Opción [1-2]: " option

case $option in
    1)
        echo ""
        echo "📦 Construyendo y ejecutando en modo PRODUCCIÓN..."
        docker-compose up -d --build
        echo ""
        echo "✅ Sistema iniciado en modo producción"
        echo ""
        echo "🌐 Frontend: http://localhost"
        echo "🔧 Backend API: http://localhost:8080/api/autocomplete"
        echo "💾 PostgreSQL: localhost:5432"
        echo ""
        echo "Para ver logs: docker-compose logs -f"
        echo "Para detener: docker-compose down"
        ;;
    2)
        echo ""
        echo "📦 Construyendo y ejecutando en modo DESARROLLO..."
        docker-compose -f docker-compose.dev.yml up -d --build
        echo ""
        echo "✅ Sistema iniciado en modo desarrollo"
        echo ""
        echo "🌐 Frontend: http://localhost:3000"
        echo "🔧 Backend API: http://localhost:8080/api/autocomplete"
        echo "🗄️  H2 Console: http://localhost:8080/h2-console"
        echo ""
        echo "Para ver logs: docker-compose -f docker-compose.dev.yml logs -f"
        echo "Para detener: docker-compose -f docker-compose.dev.yml down"
        ;;
    *)
        echo "❌ Opción inválida"
        exit 1
        ;;
esac

echo ""
echo "⏳ Esperando que los servicios estén listos..."
sleep 10

echo ""
echo "🎉 ¡Sistema listo para usar!"
