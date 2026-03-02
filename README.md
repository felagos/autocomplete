# Sistema de Autocompletado

Un sistema de autocompletado escalable implementado con conceptos de System Design Interview. Proporciona sugerencias contextuales basadas en prefijos y rastrea los términos más populares.

## 🎯 Características

- **Motor de Autocompletado Eficiente**: Búsqueda rápida de sugerencias basada en prefijos
- **Tracking de Términos**: Contador de frecuencia para términos más buscados
- **Caché Distribuido**: Implementación con Caffeine Cache para optimizar consultas
- **API REST**: Endpoints bien documentados para integración
- **Interfaz Moderna**: UI responsiva con Material Design 3 (Material-UI)
- **Arquitectura de Microservicios**: Backend y Frontend separados
- **Containerizada**: Fácil despliegue con Docker Compose

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                  FRONTEND (React + TypeScript)                │
│                   - Sistema Autocomplete                      │
│                   - Top Terms Component                       │
│                   - Material-UI Components                    │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  │ HTTP REST API
                  │
┌─────────────────▼───────────────────────────────────────────┐
│              BACKEND (Spring Boot 3.2.1, Java 21)            │
│  - AutocompleteController                                    │
│  - AutocompleteService (con Caché)                          │
│  - Repository Layer (JPA)                                    │
│  - Entity: FrequencyTerm                                     │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  │ JDBC
                  │
┌─────────────────▼───────────────────────────────────────────┐
│           PostgreSQL 16 (Base de Datos)                      │
│  - Almacenamiento de términos                               │
│  - Registro de frecuencias                                   │
└─────────────────────────────────────────────────────────────┘
```

## 📋 Requisitos Previos

- Docker y Docker Compose
- Git
- (Opcional) Java 21 + Gradle para desarrollo local
- (Opcional) Node.js 18+ para desarrollo del frontend

## 🚀 Inicio Rápido

### Con Docker Compose (Recomendado)

```bash
# Clona el repositorio
git clone <repository-url>
cd autocomplete

# Inicia todos los servicios
docker-compose up -d

# Espera a que se inicien (aproximadamente 30 segundos)
docker-compose logs -f
```

La aplicación estará disponible en:
- **Frontend**: http://localhost
- **Backend API**: http://localhost:8080
- **PostgreSQL**: localhost:5432

### Desarrollo Local

#### Backend

```bash
cd backend

# Compilar
./gradlew build

# Ejecutar (con H2 Database)
./gradlew bootRun
```

El backend estará disponible en `http://localhost:8080`

#### Frontend

```bash
cd frontend

# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
npm run dev
```

El frontend estará disponible en `http://localhost:5173`

## 📚 API Endpoints

### GET `/api/autocomplete/suggestions`

Obtiene sugerencias de autocompletado basadas en un prefijo.

**Parámetros Query:**
- `prefix` (string, requerido): Prefijo para buscar sugerencias
- `limit` (integer, opcional): Número máximo de sugerencias (default: 10)

**Ejemplo:**
```bash
curl "http://localhost:8080/api/autocomplete/suggestions?prefix=java&limit=5"
```

**Respuesta:**
```json
{
  "suggestions": [
    {
      "term": "java",
      "frequency": 156
    },
    {
      "term": "javascript",
      "frequency": 142
    }
  ]
}
```

### POST `/api/autocomplete/submit`

Registra un término de búsqueda (incrementa su frecuencia).

**Body:**
```json
{
  "term": "spring boot"
}
```

**Respuesta:**
```json
{
  "term": "spring boot",
  "frequency": 1
}
```

### GET `/api/autocomplete/top-terms`

Obtiene los términos más buscados.

**Parámetros Query:**
- `limit` (integer, opcional): Número de términos a retornar (default: 10)

**Ejemplo:**
```bash
curl "http://localhost:8080/api/autocomplete/top-terms?limit=5"
```

## 🗂️ Estructura del Proyecto

```
autocomplete/
├── backend/                           # Spring Boot Application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/autocomplete/
│   │   │   │   ├── AutocompleteApplication.java
│   │   │   │   ├── controller/
│   │   │   │   │   └── AutocompleteController.java
│   │   │   │   ├── service/
│   │   │   │   │   └── AutocompleteService.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── FrequencyTermRepository.java
│   │   │   │   ├── entity/
│   │   │   │   │   └── FrequencyTerm.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── AutocompleteRequest.java
│   │   │   │   │   ├── AutocompleteResponse.java
│   │   │   │   │   ├── SuggestionDTO.java
│   │   │   │   │   └── TermSubmitRequest.java
│   │   │   │   └── config/
│   │   │   │       ├── CacheConfig.java
│   │   │   │       └── WebConfig.java
│   │   │   └── resources/
│   │   │       ├── application.yml      # Config desarrollo
│   │   │       ├── application-prod.yml # Config producción
│   │   │       └── data.sql
│   │   └── test/
│   ├── build.gradle
│   ├── Dockerfile
│   └── gradlew
│
├── frontend/                          # React + TypeScript Application
│   ├── src/
│   │   ├── components/
│   │   │   ├── Autocomplete.tsx        # Componente buscador
│   │   │   ├── Autocomplete.css
│   │   │   ├── TopTerms.tsx            # Componente términos populares
│   │   │   └── TopTerms.css
│   │   ├── services/
│   │   │   └── api.ts                  # Cliente API
│   │   ├── App.tsx
│   │   ├── main.tsx
│   │   └── index.css
│   ├── package.json
│   ├── tsconfig.json
│   ├── vite.config.ts
│   ├── Dockerfile
│   └── nginx.conf
│
├── docker-compose.yml                 # Orquestación de servicios
├── docker-compose.dev.yml            # Configuración desarrollo
├── Makefile                           # Comandos útiles
├── README.md                          # Este archivo
```

## 🔧 Configuración

### Variables de Entorno (Backend)

En `backend/src/main/resources/application-prod.yml`:

```yaml
server:
  port: 8080
  
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/autocomplete
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
      
autocomplete:
  max-suggestions: 10
```

### Variables de Entorno (Frontend)

En `frontend/.env`:

```
VITE_API_URL=http://localhost:8080/api/autocomplete
```

## 💾 Base de Datos

### Schema

**Tabla: frequency_term**
```sql
CREATE TABLE frequency_term (
  id BIGSERIAL PRIMARY KEY,
  term VARCHAR(255) NOT NULL UNIQUE,
  frequency BIGINT NOT NULL DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_term ON frequency_term(term);
CREATE INDEX idx_frequency ON frequency_term(frequency DESC);
```

## 🚀 Despliegue en Producción

```bash
# Usar docker-compose.yml directamente
docker-compose -f docker-compose.yml up -d

# O con variables de entorno
docker-compose -f docker-compose.yml --env-file .env.prod up -d
```

## 📊 Características de Caché

El sistema implementa dos niveles de caché:

1. **Cache de Sugerencias**: Cachea resultados de búsquedas por prefijo
   - TTL: Por defecto según configuración de Caffeine
   - Key: `{prefix}_{limit}`

2. **Cache de Top Terms**: Cachea los términos más populares
   - TTL: Por defecto según configuración de Caffeine
   - Se invalida automáticamente al registrar nuevos términos

## 🧪 Testing

### Backend

```bash
cd backend
./gradlew test
```

### Frontend

```bash
cd frontend
npm run test
```

## 📈 Monitoreo

El backend expone endpoints de salud:

```bash
# Health check
curl http://localhost:8080/actuator/health

# Metrics (si está habilitado)
curl http://localhost:8080/actuator/metrics
```

## 🐛 Troubleshooting

### El backend no puede conectar a la base de datos

```bash
# Verifica el estado del contenedor PostgreSQL
docker-compose ps

# Revisa los logs
docker-compose logs postgres
```

### El frontend muestra errores de CORS

Asegúrate que `WebConfig.java` en el backend esté correctamente configurado:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost", "http://localhost:5173")
            .allowedMethods("GET", "POST", "DELETE")
            .allowCredentials(true);
    }
}
```

### Los contenedores no inician

```bash
# Detén y limpia
docker-compose down -v

# Reinicia con logs
docker-compose up --build
```

## 🔐 Seguridad

Consideraciones de seguridad implementadas:

- ✅ Validación de entrada (prefijo no vacío)
- ✅ Límite de sugerencias retornadas
- ✅ CORS configurado
- ✅ Variables de entorno para credenciales

**Mejoras futuras:**
- Implementar autenticación (JWT)
- Rate limiting
- SSL/TLS en producción

## 📖 Stack Tecnológico

### Backend
- **Java 21** - Lenguaje de programación
- **Spring Boot 3.2.1** - Framework web
- **Spring Data JPA** - ORM
- **PostgreSQL 16** - Base de datos
- **Caffeine** - Cache en memoria
- **Gradle** - Build tool

### Frontend
- **React 19** - UI Framework
- **TypeScript 5.6** - Tipado estático
- **Vite 5** - Build tool
- **Material-UI (@mui/material)** - Sistema de diseño basado en Material Design 3
- **Axios** - Cliente HTTP

### DevOps
- **Docker** - Containerización
- **Docker Compose** - Orquestación

## 📝 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## 👨‍💻 Contribuir

Las contribuciones son bienvenidas. Por favor:

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📧 Contacto

Para preguntas o sugerencias, por favor abre un issue en el repositorio.

---

**Última actualización:** Marzo de 2026
