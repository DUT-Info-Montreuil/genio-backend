# Étape 1 : Builder Angular
FROM node:18-alpine AS builder-frontend
LABEL maintainer="Elsa Hadjadj <elsa.simha.hadjadj@gmail.com>"
WORKDIR /app
COPY ./genio-frontend/ .
RUN npm install && npm run build -- --configuration production --base-href /genio/

# Étape 2 : Serveur Nginx pour servir les fichiers Angular
FROM nginx:stable-alpine
RUN mkdir -p /usr/share/nginx/html/genio
COPY --from=builder-frontend /app/dist/genio-frontend/browser/ /usr/share/nginx/html/genio/
COPY ./config/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 8100
CMD ["nginx", "-g", "daemon off;"]
