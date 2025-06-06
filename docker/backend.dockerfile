# Étape 1 : Build du backend Java avec Maven
FROM maven:3.9.2-eclipse-temurin-17 AS builder-backend
LABEL maintainer="Elsa Hadjadj <elsa.simha.hadjadj@gmail.com>"
WORKDIR /app/backend
COPY ./genio-backend/ .
COPY ./config/application-external.properties ./utils/application-external.properties
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
LABEL maintainer="Elsa Hadjadj <elsa.simha.hadjadj@gmail.com>"
WORKDIR /app
COPY --from=builder-backend /app/backend/target/GenioService-1.0.1-RELEASE.jar app.jar
COPY ./config/application-external.properties ./utils/application-external.properties
COPY ./config/log4j2.xml /config/log4j2.xml
CMD ["java", "-Dlogging.config=file:/config/log4j2.xml", "-jar", "app.jar", "--spring.config.location=file:utils/application-external.properties"]
