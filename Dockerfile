#FROM openjdk:17-jdk-slim
#
## Set working directory
#WORKDIR /app
#
## Salin JAR file aplikasi ke dalam container
#COPY target/tokonyadia-api-0.0.1-SNAPSHOT.jar /app/tokonyadia.jar
#
## Tentukan perintah untuk menjalankan aplikasi
#ENTRYPOINT ["java", "-jar", "/app/tokonyadia.jar"]
#
## Expose port agar dapat diakses dari luar container
#EXPOSE 8888

FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]