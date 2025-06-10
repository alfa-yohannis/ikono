# POSApp

Aplikasi Point of Sale (POS) berbasis JavaFX dengan integrasi database menggunakan Hibernate ORM, serta menerapkan prinsip SOLID dan beberapa design pattern.

## Cara Menjalankan

1. Clone repository dan masuk ke folder project:
   ```bash
   git clone <repo-url>
   cd POSApp
   ```
2. Build dan jalankan aplikasi:
   ```bash
   ./gradlew run
   ```
   Atau jalankan class utama (`edu.pradita.p14.Main`) dari IDE.

3. Unit Test:
   ```bash
   ./gradlew test
   ```

## Dependensi Utama
- JavaFX
- Hibernate ORM
- Jakarta Persistence API
- Gson
- Apache POI (Export Excel)
- iText (Export PDF)
- JUnit (Unit Test)

## Pola Desain yang Digunakan
- Singleton:
  - `DatabaseConnection` memastikan hanya ada satu koneksi database di aplikasi.
- Adapter:
  - `PaymentAdapter` mengadaptasi sistem pembayaran lama ke interface baru.
- Observer:
  - `TransactionNotifier` dan `EmailNotifier` untuk notifikasi otomatis saat transaksi diproses.

## Prinsip SOLID
- Single Responsibility & Open/Closed:
  - Setiap class memiliki tanggung jawab tunggal dan mudah diperluas tanpa mengubah kode yang sudah ada.

## Fitur
- CRUD transaksi
- Export PDF & Excel
- Print transaksi (dengan notifikasi observer)
- Search & filter
- Unit test

## Diagram
- Diagram Kelas, Activity, dan Sequence tersedia di folder `src/main/resources/` dalam format `.puml` (PlantUML).

