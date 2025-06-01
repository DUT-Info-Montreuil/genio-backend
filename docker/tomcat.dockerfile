# Étape 1 : Build du backend Java avec Maven
FROM maven:3.9.2-eclipse-temurin-17 AS builder-backend
WORKDIR /app/backend
COPY ./genio-backend/ .
COPY ./config/application-external.properties ./utils/application-external.properties
RUN mvn clean package -DskipTests

# Étape 2 : Build du frontend Angular
FROM node:18 AS builder-frontend
WORKDIR /app/frontend
COPY ./genio-frontend/ .
RUN npm install && npm run build

# Étape 3 : Image finale
FROM tomcat:9-jdk17
LABEL maintainer="Elsa Hadjadj <elsa.simha.hadjadj@gmail.com>"
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder-backend /app/backend/target/GenioService-1.0.0-RELEASE.war /usr/local/tomcat/webapps/genioservice.war
COPY --from=builder-frontend /app/frontend/dist /usr/local/tomcat/webapps/ROOT
COPY ./config/application-external.properties /usr/local/tomcat/conf/
EXPOSE 8080
CMD ["catalina.sh", "run"]