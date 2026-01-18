# Autocomplete Backend

Backend para sistema de autocompletado usando Spring Boot y JPA.

## Conceptos de System Design Interview Implementados

### 1. **Frequency Table**
- Tabla que almacena términos y su frecuencia de uso
- Índices optimizados para búsqueda por prefijo y ordenamiento por frecuencia
- Actualización incremental de frecuencias

### 2. **Caching Strategy**
- Caffeine cache para almacenar resultados de búsquedas frecuentes
- TTL de 5 minutos para balance entre frescura y performance
- Cache invalidation en escrituras (write-through pattern)

### 3. **Database Indexing**
- Índice en columna `term` para búsqueda rápida por prefijo
- Índice compuesto en `frequency DESC` para ordenamiento eficiente

### 4. **API Design**
- RESTful endpoints con separación de responsabilidades
- Validación de datos de entrada
- Respuestas estructuradas con métricas de performance

## Tecnologías

- **Spring Boot 3.2.1**
- **Spring Data JPA**
- **H2 Database** (desarrollo)
- **Caffeine Cache**
- **Lombok**
- **Maven**

## Endpoints

### GET /api/autocomplete/suggest
Obtiene sugerencias basadas en un prefijo.

**Parámetros:**
- `prefix` (String, required): Prefijo de búsqueda
- `limit` (Integer, optional, default=10): Número máximo de sugerencias

**Ejemplo:**
```
GET /api/autocomplete/suggest?prefix=jav&limit=5
```

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
- `limit` (Integer, optional, default=10): Número de términos

### POST /api/autocomplete/init
Inicializa datos de ejemplo (solo para desarrollo).

## Ejecución

```bash
cd backend
mvn spring-boot:run
```

El servidor estará disponible en `http://localhost:8080`

## H2 Console

Acceder a la base de datos en: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:autocomplete`
- Username: `sa`
- Password: (vacío)

## Estructura del Proyecto

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/autocomplete/
│   │   │   ├── AutocompleteApplication.java
│   │   │   ├── config/
│   │   │   │   ├── CacheConfig.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/
│   │   │   │   └── AutocompleteController.java
│   │   │   ├── dto/
│   │   │   │   ├── AutocompleteRequest.java
│   │   │   │   ├── AutocompleteResponse.java
│   │   │   │   ├── SuggestionDTO.java
│   │   │   │   └── TermSubmitRequest.java
│   │   │   ├── entity/
│   │   │   │   └── FrequencyTerm.java
│   │   │   ├── repository/
│   │   │   │   └── FrequencyTermRepository.java
│   │   │   └── service/
│   │   │       └── AutocompleteService.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── data.sql
└── pom.xml
```

## Performance Optimizations

1. **Database Indexing**: Índices en term y frequency para búsquedas O(log n)
2. **Caching**: Reduce queries repetidas a la base de datos
3. **Limit Results**: Limita resultados para reducir transferencia de datos
4. **Connection Pooling**: Pool de conexiones por defecto de Spring Boot
