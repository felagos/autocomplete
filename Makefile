.PHONY: help docker-up docker-down docker-rebuild docker-logs frontend backend lint lint-fix clean install

# Variables
DOCKER_COMPOSE_FILE := docker-compose.yml
BACKEND_DIR := backend
FRONTEND_DIR := frontend

# Detectar el sistema operativo
UNAME_S := $(shell uname -s 2>/dev/null || echo "WINDOWS")
ifeq ($(UNAME_S),Linux)
    DETECTED_OS := linux
endif
ifeq ($(UNAME_S),Darwin)
    DETECTED_OS := macos
endif
ifdef DETECTED_OS
    # Unix/Linux/MacOS
    GRADLE := ./$(BACKEND_DIR)/gradlew
    BUN := bun
    MKDIR := mkdir -p
    RM := rm -rf
else
    # Windows
    GRADLE := .\$(BACKEND_DIR)\gradlew.bat
    BUN := bun
    MKDIR := mkdir
    RM := rmdir /s /q
endif

help: ## Muestra esta ayuda
	@echo "Comandos disponibles:"
	@echo ""
	@echo "Docker:"
	@echo "  make docker-up          - Inicia los servicios con Docker Compose"
	@echo "  make docker-down        - Detiene los servicios de Docker Compose"
	@echo "  make docker-rebuild     - Reconstruye y reinicia los servicios de Docker Compose"
	@echo "  make docker-logs        - Muestra los logs de Docker Compose"
	@echo ""
	@echo "Desarrollo:"
	@echo "  make frontend           - Inicia el servidor de desarrollo del frontend"
	@echo "  make backend            - Compila y ejecuta el backend"
	@echo "  make backend-build      - Solo compila el backend"
	@echo ""
	@echo "Linter y Validación:"
	@echo "  make lint               - Verifica errores de linter (frontend y backend)"
	@echo "  make lint-frontend      - Verifica errores de linter en el frontend"
	@echo "  make lint-backend       - Verifica errores de linter en el backend"
	@echo "  make lint-fix           - Corrige automáticamente los errores de linter"
	@echo "  make lint-fix-frontend  - Corrige automáticamente los errores de linter del frontend"
	@echo "  make lint-fix-backend   - Corrige automáticamente los errores de linter del backend"
	@echo ""
	@echo "Utilidades:"
	@echo "  make install            - Instala las dependencias (frontend y backend)"
	@echo "  make clean              - Limpia los archivos generados"
	@echo "  make help               - Muestra esta ayuda"

# ======================== DOCKER ========================

docker-up: ## Inicia los servicios con Docker Compose
	@echo "Iniciando servicios con Docker Compose..."
	docker-compose -f $(DOCKER_COMPOSE_FILE) up -d
	@echo "Servicios iniciados. Accede a:"
	@echo "  - Frontend: http://localhost:5173"
	@echo "  - Backend API: http://localhost:8080"

docker-down: ## Detiene los servicios de Docker Compose
	@echo "Deteniendo servicios de Docker Compose..."
	docker-compose -f $(DOCKER_COMPOSE_FILE) down

docker-rebuild: ## Reconstruye y reinicia los servicios de Docker Compose
	@echo "Reconstruyendo servicios de Docker Compose..."
	docker-compose -f $(DOCKER_COMPOSE_FILE) down
	docker-compose -f $(DOCKER_COMPOSE_FILE) build --no-cache
	docker-compose -f $(DOCKER_COMPOSE_FILE) up -d
	@echo "✓ Servicios reconstruidos y reiniciados. Accede a:"
	@echo "  - Frontend: http://localhost:5173"
	@echo "  - Backend API: http://localhost:8080"

docker-logs: ## Muestra los logs de Docker Compose
	docker-compose -f $(DOCKER_COMPOSE_FILE) logs -f

# ======================== FRONTEND ========================

frontend: install-frontend ## Inicia el servidor de desarrollo del frontend
	@echo "Iniciando servidor de desarrollo del frontend..."
	@cd $(FRONTEND_DIR) && $(BUN) run dev

install-frontend: ## Instala las dependencias del frontend
	@echo "Instalando dependencias del frontend..."
	@cd $(FRONTEND_DIR) && $(BUN) install

# ======================== BACKEND ========================

backend: backend-build ## Compila y ejecuta el backend
	@echo "Ejecutando el backend..."
	@cd $(BACKEND_DIR) && $(GRADLE) bootRun

backend-build: install-backend ## Compila el backend
	@echo "Compilando el backend..."
	@cd $(BACKEND_DIR) && $(GRADLE) clean build -x test

install-backend: ## Instala las dependencias del backend (Gradle)
	@echo "Descargando dependencias del backend..."
	@cd $(BACKEND_DIR) && $(GRADLE) dependencies

# ======================== LINTER ========================

lint: lint-frontend lint-backend ## Verifica errores de linter (frontend y backend)

lint-frontend: ## Verifica errores de linter en el frontend
	@echo "Verificando linter del frontend..."
	@cd $(FRONTEND_DIR) && $(BUN) run lint

lint-backend: ## Verifica errores de linter en el backend
	@echo "Verificando linter del backend..."
	@cd $(BACKEND_DIR) && $(GRADLE) checkstyleMain

lint-fix: lint-fix-frontend lint-fix-backend ## Corrige automáticamente los errores de linter

lint-fix-frontend: ## Corrige automáticamente los errores de linter del frontend
	@echo "Corrigiendo errores de linter del frontend..."
	@cd $(FRONTEND_DIR) && $(BUN) run lint -- --fix

lint-fix-backend: ## Corrige automáticamente los errores de linter del backend
	@echo "Corrigiendo errores de linter del backend (manual review needed)..."
	@echo "Nota: El backend usa Checkstyle que generalmente requiere correcciones manuales."
	@echo "Ejecutando análisis de Checkstyle..."
	@cd $(BACKEND_DIR) && $(GRADLE) checkstyleMain

# ======================== UTILIDADES ========================

install: install-frontend install-backend ## Instala todas las dependencias

clean: ## Limpia los archivos generados
	@echo "Limpiando archivos generados..."
ifdef DETECTED_OS
	@$(RM) $(FRONTEND_DIR)/node_modules
	@$(RM) $(BACKEND_DIR)/build
	@$(RM) $(BACKEND_DIR)/.gradle
	@echo "✓ Limpieza completada"
else
	@echo "Limpiando frontend..."
	@if exist $(FRONTEND_DIR)\node_modules $(RM) $(FRONTEND_DIR)\node_modules
	@echo "Limpiando backend..."
	@if exist $(BACKEND_DIR)\build $(RM) $(BACKEND_DIR)\build
	@if exist $(BACKEND_DIR)\.gradle $(RM) $(BACKEND_DIR)\.gradle
	@echo "✓ Limpieza completada"
endif

# ======================== DEFAULT ========================

.DEFAULT_GOAL := help
