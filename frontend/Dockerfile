# Multi-stage build para optimizar imagen
FROM oven/bun:1-alpine AS build

WORKDIR /app

# Copiar package files
COPY package.json bun.lockb* ./

# Instalar dependencias
RUN bun install --frozen-lockfile || bun install

# Copiar código fuente
COPY . .

# Build de producción
RUN bun run build

# Imagen final con servidor nginx
FROM nginx:alpine

# Copiar build desde etapa anterior
COPY --from=build /app/dist /usr/share/nginx/html

# Copiar configuración de nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponer puerto
EXPOSE 80

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1

# Nginx se ejecuta en foreground por defecto
CMD ["nginx", "-g", "daemon off;"]
