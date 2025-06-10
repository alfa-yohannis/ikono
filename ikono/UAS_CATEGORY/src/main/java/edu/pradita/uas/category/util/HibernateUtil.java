package edu.pradita.uas.category.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Kelas utilitas untuk mengelola SessionFactory Hibernate.
 * Diterapkan menggunakan pola Singleton untuk memastikan hanya ada satu
 * instance SessionFactory yang dibuat selama aplikasi berjalan.
 */
public class HibernateUtil {

    // Instance SessionFactory yang bersifat private, static, dan final.
    // Dibuat sekali dan tidak bisa diubah.
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    /**
     * Constructor private untuk mencegah instansiasi dari luar kelas.
     */
    private HibernateUtil() {}

    private static SessionFactory buildSessionFactory() {
        try {
            // Membuat SessionFactory dari konfigurasi di hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Jika gagal membuat SessionFactory, catat error dan lempar exception.
            // Ini adalah error fatal yang menghentikan aplikasi.
            System.err.println("Initial SessionFactory creation failed. " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Metode publik static untuk mengakses instance tunggal SessionFactory.
     * @return SessionFactory yang sudah diinisialisasi.
     */
    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    /**
     * Metode untuk menutup SessionFactory, bisa dipanggil saat aplikasi berhenti.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}
