# Autocomplete Frontend

Frontend para sistema de autocompletado construido con React, Vite y Bun.

## Características

### Componentes Principales

1. **Autocomplete Component**
   - Input con búsqueda en tiempo real
   - Debouncing de 300ms para optimizar requests
   - Navegación con teclado (↑↓ Enter Esc)
   - Indicador de loading
   - Métricas de tiempo de ejecución

2. **TopTerms Component**
   - Lista de términos más populares
   - Visualización con barras de progreso
   - Actualización en tiempo real
   - Diseño responsive

### Optimizaciones

- **Debouncing**: Reduce llamadas al servidor mientras el usuario escribe
- **Click Outside**: Cierra dropdown al hacer click fuera
- **Keyboard Navigation**: Navegación completa con teclado
- **Loading States**: Feedback visual durante las búsquedas
- **Error Handling**: Manejo robusto de errores

## Tecnologías

- **React 18.2** - Librería UI
- **Vite 5** - Build tool y dev server
- **Axios** - Cliente HTTP
- **Bun** - Package manager y runtime

## Estructura del Proyecto

```
frontend/
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
└── vite.config.js
```

## Instalación y Ejecución

### Con Bun (recomendado)

```bash
cd frontend
bun install
bun run dev
```

### Con npm

```bash
cd frontend
npm install
npm run dev
```

La aplicación estará disponible en `http://localhost:3000`

## API Integration

El frontend se conecta al backend en `http://localhost:8080/api/autocomplete`

### Endpoints utilizados:

- `GET /suggest?prefix={text}&limit={n}` - Obtener sugerencias
- `POST /submit` - Enviar término seleccionado
- `GET /top?limit={n}` - Obtener términos más populares

## Conceptos de System Design Interview

### 1. **Client-Side Debouncing**
Reduce la carga en el servidor implementando un delay de 300ms antes de enviar requests. Esto optimiza especialmente en búsquedas de múltiples caracteres.

### 2. **Optimistic UI Updates**
La interfaz responde inmediatamente mientras se procesa la request en background.

### 3. **Keyboard Shortcuts**
Mejora la UX con navegación eficiente:
- `↓` / `↑` - Navegar sugerencias
- `Enter` - Seleccionar
- `Esc` - Cerrar

### 4. **Performance Metrics**
Muestra el tiempo de ejecución de cada búsqueda para monitorear performance.

### 5. **Error Boundaries**
Manejo graceful de errores sin romper la aplicación.

## Build para Producción

```bash
bun run build
```

Los archivos optimizados se generarán en la carpeta `dist/`

## Previsualizare Build

```bash
bun run preview
```

## Personalización

### Cambiar tiempo de debounce

En [Autocomplete.jsx](frontend/src/components/Autocomplete.jsx#L42):

```javascript
}, 300) // Cambiar valor en milisegundos
```

### Cambiar número de sugerencias

En [api.js](frontend/src/services/api.js#L15):

```javascript
export const getSuggestions = async (prefix, limit = 10) // Cambiar limit
```

### Cambiar URL del backend

En [api.js](frontend/src/services/api.js#L3):

```javascript
const API_BASE_URL = 'http://localhost:8080/api/autocomplete'
```
