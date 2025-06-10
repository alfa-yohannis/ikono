package org.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Kelas utilitas untuk mengelola SessionFactory Hibernate.
 * Mengimplementasikan Pola Desain Kreasional Singleton untuk memastikan hanya ada
 * satu instance SessionFactory di seluruh aplikasi.
 */
public class HibernateUtil {

    // Instance SessionFactory tunggal, dibuat saat kelas dimuat (eager initialization)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Metode privat untuk membangun SessionFactory.
     * Dipanggil hanya sekali saat kelas dimuat.
     * @return SessionFactory yang telah dikonfigurasi.
     * @throws ExceptionInInitializerError jika pembuatan SessionFactory gagal.
     */
    private static SessionFactory buildSessionFactory() {
        try {
            // Membuat SessionFactory dari hibernate.cfg.xml
            // Configuration().configure() akan mencari hibernate.cfg.xml di classpath
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Mencatat error jika pembuatan SessionFactory gagal
            System.err.println("Pembuatan SessionFactory awal gagal." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Metode publik statis untuk mendapatkan instance SessionFactory.
     * @return instance SessionFactory.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Metode untuk menutup SessionFactory.
     * Harus dipanggil saat aplikasi ditutup untuk melepaskan sumber daya.
     */
    public static void shutdown() {
        if (getSessionFactory() != null && !getSessionFactory().isClosed()) {
            getSessionFactory().close();
            System.out.println("Hibernate SessionFactory berhasil di-shutdown.");
        }
    }
}