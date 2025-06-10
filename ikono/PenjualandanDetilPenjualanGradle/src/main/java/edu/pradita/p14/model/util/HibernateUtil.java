package edu.pradita.p14.model.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Kelas utilitas Hibernate yang menerapkan pola Singleton
 * untuk memastikan hanya ada satu instance SessionFactory.
 */
public class HibernateUtil {

    // Instance tunggal dari SessionFactory (volatile untuk thread-safety)
    private static volatile SessionFactory sessionFactory;

    // Private constructor untuk mencegah instansiasi dari luar
    private HibernateUtil() {}

    /**
     * Memberikan satu-satunya instance dari SessionFactory.
     * Menggunakan double-checked locking untuk thread safety.
     *
     * @return instance SessionFactory yang tunggal.
     */
    public static SessionFactory getSessionFactory() {
        // Pengecekan pertama (tanpa lock) untuk performa
        if (sessionFactory == null) {
            // Jika null, baru gunakan lock
            synchronized (HibernateUtil.class) {
                // Pengecekan kedua (dengan lock) untuk mencegah race condition
                if (sessionFactory == null) {
                    try {
                        // Membuat SessionFactory dari hibernate.cfg.xml
                        Configuration configuration = new Configuration().configure();
                        sessionFactory = configuration.buildSessionFactory();
                    } catch (Throwable ex) {
                        System.err.println("Inisialisasi SessionFactory gagal." + ex);
                        throw new ExceptionInInitializerError(ex);
                    }
                }
            }
        }
        return sessionFactory;
    }

    /**
     * Menutup SessionFactory untuk melepaskan semua resource database.
     * Panggil method ini saat aplikasi ditutup.
     */
    public static void shutdown() {
        if (sessionFactory != null) {
            getSessionFactory().close();
        }
    }
}
