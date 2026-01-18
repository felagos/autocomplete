# Sistema de Autocompletado

Sistema completo de autocompletado implementando conceptos del libro **"System Design Interview"**.

## 📋 Descripción General

Este proyecto implementa un sistema de autocompletado de alta performance que utiliza:
- **Frequency Table** para ranking de sugerencias
- **Caching** para optimización de búsquedas repetidas
- **Debouncing** en el cliente para reducir carga del servidor
- **Índices de base de datos** para búsquedas eficientes
- **API REST** para comunicación cliente-servidor

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                         FRONTEND                             │
│  ┌──────────────┐    ┌──────────────┐   ┌──────────────┐   │
│  │ Autocomplete │ ←→ │  API Client  │ ← │   TopTerms   │   │
│  │  Component   │    │   (Axios)    │   │  Component   │   │
│  └──────────────┘    └──────────────┘   └──────────────┘   │
│         ↓                    ↓                   ↓          │
│     Debouncing          HTTP Requests      Real-time        │
│     (300ms)             to Backend         Updates          │
└─────────────────────────────────────────────────────────────┘
                              ↕ HTTP (REST)
┌─────────────────────────────────────────────────────────────┐
│                         BACKEND                              │
│  ┌──────────────┐    ┌──────────────┐   ┌──────────────┐   │
│  │  Controller  │ ←→ │   Service    │ ← │  Repository  │   │
│  │   (REST)     │    │  (Business)  │   │     (JPA)    │   │
│  └──────────────┘    └──────────────┘   └──────────────┘   │
│         ↓                    ↓                   ↓          │
│    CORS Config         Cache Layer          Database        │
│    Validation          (Caffeine)           Indexing        │
└─────────────────────────────────────────────────────────────┘
                              ↕ JDBC
┌─────────────────────────────────────────────────────────────┐
│                   DATABASE (H2)                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │            FREQUENCY_TABLE                           │   │
│  ├──────────┬───────────┬──────────┬─────────────────┤   │
│  │ id (PK)  │   term    │frequency │   last_used     │   │
│  ├──────────┼───────────┼──────────┼─────────────────┤   │
│  │    1     │   java    │   4500   │  2026-01-17     │   │
│  │    2     │javascript │   5000   │  2026-01-17     │   │
│  └──────────┴───────────┴──────────┴─────────────────┘   │
│                                                             │
│  Indices:                                                   │
│  - idx_term: B-Tree index on term (for prefix search)      │
│  - idx_frequency: B-Tree index on frequency DESC           │
└─────────────────────────────────────────────────────────────┘
```

## 🎯 Conceptos de System Design Interview Implementados

### 1. **Data Model Design**
- **Frequency Table**: Almacena términos con su frecuencia de uso
- **Timestamps**: Tracking de creación y último uso
- **Normalization**: Términos almacenados en minúsculas

### 2. **Performance Optimization**
- **Database Indexing**: Índices en `term` y `frequency` para queries O(log n)
- **Caching**: Cache en memoria (Caffeine) con TTL de 5 minutos
- **Connection Pooling**: Pool de conexiones por defecto de HikariCP
- **Lazy Loading**: Carga bajo demanda de datos

### 3. **Scalability Patterns**
- **Stateless Backend**: Permite escalado horizontal
- **Cache-Aside Pattern**: Cache con fallback a database
- **Write-Through Cache**: Invalidación de cache en escrituras
- **Rate Limiting Ready**: Estructura preparada para rate limiting

### 4. **Client Optimization**
- **Debouncing**: 300ms delay para reducir requests
- **Optimistic UI**: Respuesta inmediata en la interfaz
- **Progressive Enhancement**: Funciona sin JavaScript básico

### 5. **API Design**
- **RESTful**: Endpoints semánticos y consistentes
- **Versioning Ready**: Estructura `/api/` para versionado
- **Pagination Support**: Parámetro `limit` en queries
- **Response Metadata**: Tiempo de ejecución en respuestas

## 📁 Estructura del Proyecto

```
autocomplete/
├── backend/                          # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/autocomplete/
│   │   │   │   ├── AutocompleteApplication.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── CacheConfig.java
│   │   │   │   │   └── WebConfig.java
│   │   │   │   ├── controller/
│   │   │   │   │   └── AutocompleteController.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── AutocompleteRequest.java
│   │   │   │   │   ├── AutocompleteResponse.java
│   │   │   │   │   ├── SuggestionDTO.java
│   │   │   │   │   └── TermSubmitRequest.java
│   │   │   │   ├── entity/
│   │   │   │   │   └── FrequencyTerm.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── FrequencyTermRepository.java
│   │   │   │   └── service/
│   │   │   │       └── AutocompleteService.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── data.sql
│   │   └── test/
│   ├── pom.xml
│   └── README.md
│
└── frontend/                         # React Frontend
    ├── src/
    │   ├── components/
    │   │   ├── Autocomplete.jsx
    │   │   ├── Autocomplete.css
    │   │   ├── TopTerms.jsx
    │   │   └── TopTerms.css
    │   ├── services/
    │   │   └── api.js
    │   ├── App.jsx
    │   ├── App.css
    │   ├── main.jsx
    │   └── index.css
    ├── index.html
    ├── package.json
    ├── vite.config.js
    └── README.md
```

## 🚀 Inicio Rápido

### Prerrequisitos
- **Java 17+** (para el backend)
- **Maven** (para el backend)
- **Bun** o **Node.js 18+** (para el frontend)

### 1. Iniciar el Backend

```bash
cd backend
mvn spring-boot:run
```

El backend estará disponible en `http://localhost:8080`

### 2. Iniciar el Frontend

Con Bun:
```bash
cd frontend
bun install
bun run dev
```

Con npm:
```bash
cd frontend
npm install
npm run dev
```

El frontend estará disponible en `http://localhost:3000`

### 3. Inicializar Datos de Ejemplo

```bash
curl -X POST http://localhost:8080/api/autocomplete/init
```

O usar el endpoint desde el navegador visitando:
`http://localhost:8080/api/autocomplete/init`

## 🔌 API Endpoints

### GET /api/autocomplete/suggest
Obtiene sugerencias basadas en un prefijo.

**Parámetros:**
- `prefix` (String): Prefijo de búsqueda
- `limit` (Integer): Número máximo de sugerencias (default: 10)

**Respuesta:**
```json
{
  "prefix": "jav",
  "suggestions": [
    {"term": "javascript", "frequency": 5000},
    {"term": "java", "frequency": 4500}
  ],
  "executionTimeMs": 15
}
```

### POST /api/autocomplete/submit
Guarda o actualiza un término incrementando su frecuencia.

**Body:**
```json
{
  "term": "react"
}
```

### GET /api/autocomplete/top
Obtiene los términos más populares.

**Parámetros:**
- `limit` (Integer): Número de términos (default: 10)

### POST /api/autocomplete/init
Inicializa datos de ejemplo.

## 🧪 Testing

### Backend
```bash
cd backend
mvn test
```

### Frontend
```bash
cd frontend
bun test  # o npm test
```

## 📊 Métricas de Performance

El sistema incluye métricas de performance:
- **Tiempo de respuesta**: Mostrado en cada búsqueda
- **Cache hit rate**: Visible en logs del backend
- **Query execution time**: Logged en modo debug

### Benchmarks Esperados
- Búsqueda sin cache: ~20-50ms
- Búsqueda con cache hit: ~5-15ms
- Actualización de frecuencia: ~10-30ms

## 🔧 Configuración

### Backend (application.yml)
```yaml
autocomplete:
  max-suggestions: 10        # Máximo de sugerencias
  min-prefix-length: 1       # Longitud mínima del prefijo
  cache-enabled: true        # Habilitar/deshabilitar cache

spring:
  cache:
    caffeine:
      spec: maximumSize=10000,expireAfterWrite=300s
```

### Frontend (api.js)
```javascript
const API_BASE_URL = 'http://localhost:8080/api/autocomplete'
const DEBOUNCE_DELAY = 300  // ms
const DEFAULT_LIMIT = 10    // sugerencias
```

## 🌐 Despliegue

### Backend
```bash
cd backend
mvn clean package
java -jar target/autocomplete-backend-1.0.0.jar
```

### Frontend
```bash
cd frontend
bun run build
# Servir archivos desde dist/
```

## 📈 Posibles Mejoras

### Escalabilidad
- [ ] Migrar a PostgreSQL/MySQL para producción
- [ ] Implementar Redis para cache distribuido
- [ ] Rate limiting con bucket algorithm
- [ ] Load balancing con múltiples instancias

### Features
- [ ] Búsqueda fuzzy (tolerancia a errores tipográficos)
- [ ] Sugerencias personalizadas por usuario
- [ ] Trending terms (términos populares recientes)
- [ ] Categorización de términos

### Optimización
- [ ] Trie data structure en memoria
- [ ] Pre-warming de cache al inicio
- [ ] Compresión de respuestas HTTP
- [ ] Service Worker para offline support

## 📚 Referencias

- **System Design Interview** - Alex Xu
- **Designing Data-Intensive Applications** - Martin Kleppmann
- Spring Boot Documentation
- React Documentation

## 🛠️ Stack Tecnológico

### Backend
- Spring Boot 3.2.1
- Spring Data JPA
- H2 Database
- Caffeine Cache
- Lombok
- Maven

### Frontend
- React 18.2
- Vite 5
- Axios
- Bun (runtime y package manager)

## 📝 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## 👥 Contribuciones

Las contribuciones son bienvenidas. Por favor:
1. Fork el proyecto
2. Crea una rama para tu feature
3. Commit tus cambios
4. Push a la rama
5. Abre un Pull Request
