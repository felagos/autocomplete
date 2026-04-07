.PHONY: up down logs restart clean

# Start all Docker Compose services
up:
	docker compose up -d

# Stop all Docker Compose services
down:
	docker compose down

# View logs from all containers
logs:
	docker compose logs -f

# Restart all services
restart: down up

# Clean up containers, volumes, and networks
clean:
	docker compose down -v

# Build and start all services
build:
	docker compose up -d --build

help:
	@echo "Available commands:"
	@echo "  make up       - Start all Docker Compose services"
	@echo "  make down     - Stop all Docker Compose services"
	@echo "  make logs     - View logs from all containers"
	@echo "  make restart  - Restart all services"
	@echo "  make build    - Build and start all services"
	@echo "  make clean    - Remove containers, volumes, and networks"
	@echo "  make help     - Show this help message"
