# Gunakan image resmi untuk Java 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Salin JAR file aplikasi ke dalam container
COPY target/tokonyadia.jar /app/tokonyadia.jar

# Tentukan perintah untuk menjalankan aplikasi
ENTRYPOINT ["java", "-jar", "/app/tokonyadia.jar"]

# Expose port agar dapat diakses dari luar container
EXPOSE 8888
