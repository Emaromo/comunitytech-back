# ============================
# 🧱 Etapa 1: Compilación
# ============================
FROM dockerproxy.com/library/maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ============================
# 🚀 Etapa 2: Ejecución
# ============================
FROM dockerproxy.com/library/eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
