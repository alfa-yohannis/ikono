package edu.pradita.p14;

/**
 * Singleton pattern untuk koneksi database.
 * Hanya ada satu instance DatabaseConnection di seluruh aplikasi.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;

    private DatabaseConnection() {
        // Inisialisasi koneksi
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Contoh method koneksi ke database dengan error handling sederhana.
     */
    public void connect() {
        try {
            // Simulasi koneksi database
            System.out.println("Connected to database!");
        } catch (Exception e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
        }
    }
} 
