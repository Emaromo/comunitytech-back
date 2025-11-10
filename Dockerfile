# ============================
# 🧩 FASE 1: Compilación del proyecto (Maven)
# ============================
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Carpeta de trabajo
WORKDIR /app

# Copiar los archivos del proyecto
COPY pom.xml .
COPY src ./src

# Compilar el proyecto sin ejecutar tests
RUN mvn clean package -DskipTests


# ============================
# 🚀 FASE 2: Ejecución del backend
# ============================
FROM eclipse-temurin:17-jdk-alpine

# Carpeta de trabajo
WORKDIR /app

# Copiamos el jar generado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Configurar variables de entorno (puedes editarlas en EasyPanel también)
ENV SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3307/udemy_curso_spring_react?useUnicode=true&characterEncoding=utf8&serverTimezone=America/Argentina/Buenos_Aires
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=
ENV SERVER_PORT=8082

# Exponer el puerto 8082
EXPOSE 8082

# Comando para ejecutar el backend
ENTRYPOINT ["java", "-jar", "app.jar"]
