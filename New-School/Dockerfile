FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia el archivo pom.xml y los scripts de Maven wrapper
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Da permisos de ejecución al script mvnw
RUN chmod +x ./mvnw

# Descarga las dependencias (esto se almacenará en caché si no cambia pom.xml)
RUN ./mvnw dependency:go-offline -B

# Copia el resto del código fuente
COPY src ./src

# Compila la aplicación
RUN ./mvnw package -DskipTests

# Configura la variable de entorno para el puerto
ENV PORT=8080

# Ejecuta la aplicación
ENTRYPOINT ["java", "-jar", "/app/target/New-School-0.0.1-SNAPSHOT.jar"]