@echo off
REM Script para inicializar y ejecutar el sistema completo en Windows

echo ================================================
echo   Sistema de Autocompletado - Inicio
echo ================================================
echo.

REM Verificar si Docker está instalado
docker --version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker no esta instalado. Por favor instala Docker Desktop.
    pause
    exit /b 1
)

REM Verificar si Docker Compose está instalado
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker Compose no esta instalado.
    pause
    exit /b 1
)

echo [OK] Docker y Docker Compose detectados
echo.

REM Preguntar modo de ejecución
echo Selecciona el modo de ejecucion:
echo 1) Produccion (PostgreSQL)
echo 2) Desarrollo (H2)
echo.
set /p option="Opcion [1-2]: "

if "%option%"=="1" (
    echo.
    echo [INFO] Construyendo y ejecutando en modo PRODUCCION...
    docker-compose up -d --build
    echo.
    echo [OK] Sistema iniciado en modo produccion
    echo.
    echo Frontend:    http://localhost
    echo Backend API: http://localhost:8080/api/autocomplete
    echo PostgreSQL:  localhost:5432
    echo.
    echo Para ver logs: docker-compose logs -f
    echo Para detener: docker-compose down
) else if "%option%"=="2" (
    echo.
    echo [INFO] Construyendo y ejecutando en modo DESARROLLO...
    docker-compose -f docker-compose.dev.yml up -d --build
    echo.
    echo [OK] Sistema iniciado en modo desarrollo
    echo.
    echo Frontend:    http://localhost:3000
    echo Backend API: http://localhost:8080/api/autocomplete
    echo H2 Console:  http://localhost:8080/h2-console
    echo.
    echo Para ver logs: docker-compose -f docker-compose.dev.yml logs -f
    echo Para detener: docker-compose -f docker-compose.dev.yml down
) else (
    echo [ERROR] Opcion invalida
    pause
    exit /b 1
)

echo.
echo [INFO] Esperando que los servicios esten listos...
timeout /t 15 /nobreak >nul

echo.
echo ================================================
echo   Sistema listo para usar!
echo ================================================
echo.
pause
