package org.example;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static HibernateUtil instance; // Instance tunggal dari HibernateUtil
    private final SessionFactory sessionFactory; // SessionFactory sekarang adalah field instance

    // Konstruktor private untuk mencegah instansiasi dari luar
    private HibernateUtil() {
        try {
            // Buat SessionFactory dari hibernate.cfg.xml
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Metode statis untuk mendapatkan instance tunggal
    public static synchronized HibernateUtil getInstance() {
        if (instance == null) {
            instance = new HibernateUtil();
        }
        return instance;
    }

    // Metode instance untuk mendapatkan SessionFactory
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Metode instance untuk shutdown
    public void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("Hibernate SessionFactory ditutup oleh instance HibernateUtil.");
        }
    }

    // Metode statis untuk shutdown (memanggil metode instance)
    // Ini untuk kompatibilitas jika masih ada yang memanggil HibernateUtil.shutdown() secara statis
    // atau untuk kemudahan akses dari App.stop()
    public static void closeSessionFactory() {
        if (instance != null) {
            instance.shutdown();
        }
    }
}