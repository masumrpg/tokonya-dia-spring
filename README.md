# Tokonya Dia - Spring Boot Application

Tokonya Dia adalah aplikasi e-commerce berbasis **Spring Boot** yang dirancang untuk mempermudah pengelolaan toko
online. Aplikasi ini menyediakan fitur lengkap untuk manajemen produk, pelanggan, pesanan, dan integrasi pembayaran
dengan performa tinggi berkat penggunaan teknologi modern.

## 🎯 Fitur Utama

- **Manajemen Produk**: CRUD produk, kategori, dan inventaris.
- **Manajemen Pelanggan**: Fitur registrasi, login, dan pengelolaan informasi pengguna.
- **Manajemen Pesanan**: Proses checkout, pelacakan, dan pembatalan pesanan.
- **Sistem Pembayaran**: Dukungan integrasi dengan penyedia pembayaran.
- **Redis Cache**: Optimalisasi performa dengan caching data.
- **Keamanan**: Autentikasi JWT dan otorisasi berbasis peran.

## 🛠️ Teknologi yang Digunakan

- **Java**: Bahasa pemrograman utama.
- **Spring Boot**: Framework inti untuk pengembangan aplikasi.
- **Hibernate ORM**: Manajemen relasi database menggunakan ORM.
- **PostgreSQL**: Database relasional untuk penyimpanan data.
- **Redis**: Untuk caching dan peningkatan performa aplikasi.
- **Lombok**: Mengurangi boilerplate code.
- **JWT**: Autentikasi dan otorisasi berbasis token.
- **Swagger**: Dokumentasi API otomatis.

## ⚙️ Instalasi dan Penggunaan

Ikuti langkah-langkah berikut untuk menjalankan aplikasi di lingkungan lokal Anda.

### Prasyarat

- **Java 17** atau lebih baru
- **Maven 3.8+**
- **PostgreSQL 13+**
- **Redis**
- **Git**

### Langkah Instalasi

1. Clone repositori ini:
   ```bash
   git clone https://github.com/masumrpg/tokonya-dia-spring.git
   cd tokonya-dia-spring
   ```

2. Konfigurasikan database PostgreSQL dan Redis di file application.properties:
   ```properties
   # PostgreSQL Configuration
   spring.datasource.url=jdbc:postgresql://localhost:5432/tokonya_dia
   spring.datasource.username=<your_db_username>
   spring.datasource.password=<your_db_password>
   spring.jpa.hibernate.ddl-auto=update
   
   # Redis Configuration
   spring.redis.host=localhost
   spring.redis.port=6379
   ```

3. Jalankan aplikasi:
   ```shell
   1. Build java app:
   ./mvnw clean package
   
   2. Build dan jalankan aplikasi java dan postgresql
   docker-compose up --build
   ```

4. Akses aplikasi di http://localhost:8888

### 📂 Struktur Proyek

Berikut adalah gambaran umum struktur proyek:

```shell
src/main/java/com/example/tokonya_dia
├── controller   # Controller untuk API dan halaman
├── model        # Entitas dan model database
├── repository   # Repository untuk akses database
├── service      # Logika bisnis aplikasi
├── config       # Konfigurasi aplikasi (JWT, Redis, dll.)
├── dto          # Data Transfer Objects
```

### 📋 Dokumentasi API

Dokumentasi API tersedia melalui Swagger di endpoint:

```shell
http://localhost:8080/swagger-ui.html
```

## 📜 Lisensi

Proyek ini dilisensikan di bawah **MIT License**.

## 📞 Kontak

Jika Anda memiliki pertanyaan, saran, atau masukan, silakan hubungi kami:

- **Nama**: Ma'sum
- **Email**: [mclasix@gmail.com](mailto:mclasix@gmail.com)
- **GitHub**: [@masumrpg](https://github.com/masumrpg)
