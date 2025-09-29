# Etapa 1: Construcción
FROM node:22-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm install --legacy-peer-deps

COPY . .
RUN npm run build:prod

# Etapa 2: Servidor Nginx
FROM nginx:stable-alpine

# Elimina config default
RUN rm -rf /usr/share/nginx/html/*

# Copia los archivos compilados de Angular
COPY --from=build /app/dist/frontend /usr/share/nginx/html

# Copia una configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
