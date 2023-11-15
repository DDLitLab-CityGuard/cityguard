# Schritt 1: Verwende das offizielle Maven-Image, um die Abhängigkeiten zu bauen und das Jar zu packen
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Schritt 2: Erstelle das eigentliche Docker-Image mit Java 17
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 5123
ENTRYPOINT ["java","-jar","app.jar"]
