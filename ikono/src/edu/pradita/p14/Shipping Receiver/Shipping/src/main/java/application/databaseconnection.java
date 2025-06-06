package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Saran: Ubah nama kelas menjadi DatabaseConnection (CamelCase)
public class databaseconnection { // Atau DatabaseConnection jika Anda ubah

    // Tambahkan kembali parameter URL untuk kompatibilitas yang lebih baik
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/warehouses_id?allowPublicKeyRetrieval=true&useSSL=false";
    static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        // Class.forName() biasanya tidak diperlukan untuk driver JDBC modern
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void main(String[] args) {
        System.out.println("Mencoba membuat koneksi ke database...");
        try (Connection conn = databaseconnection.getConnection()) { // atau DatabaseConnection.getConnection()
            if (conn != null) {
                System.out.println("Koneksi ke database BERHASIL!");
                System.out.println("Produk Database: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Versi Database: " + conn.getMetaData().getDatabaseProductVersion());
            } else {
                System.out.println("Koneksi GAGAL (objek koneksi null).");
            }
        } catch (SQLException e) {
            System.err.println("Koneksi GAGAL. SQLException terjadi:"); // Gunakan System.err untuk error
            e.printStackTrace(); // Cetak stack trace lengkap untuk detail
        } catch (Exception e) { // Menangkap error tak terduga lainnya
            System.err.println("Koneksi GAGAL. Exception umum terjadi:");
            e.printStackTrace();
        }
        System.out.println("Tes koneksi selesai.");
    }
} // HAPUS KURUNG KURAWAL BERLEBIH DARI SINI JIKA ADA