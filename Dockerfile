# ============================
# 🧱 FASE 1: Construcción (Build con Maven + Java 21)
# ============================
FROM mirror.gcr.io/library/maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar archivos de configuración
COPY pom.xml .
COPY src ./src

# Compilar el proyecto sin tests
RUN mvn clean package -DskipTests

# ============================
# 🚀 FASE 2: Ejecución (Runtime)
# ============================
FROM mirror.gcr.io/library/eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto interno donde corre Spring Boot
EXPOSE 8082

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
