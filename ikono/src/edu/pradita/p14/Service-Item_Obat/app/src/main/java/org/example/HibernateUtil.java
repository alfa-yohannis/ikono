package org.example;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    // 1. Instance SessionFactory dibuat sekali saja saat kelas dimuat.
    //    Ini adalah inti dari bagaimana kita mendapatkan satu instance SessionFactory.
    private static final SessionFactory sessionFactory = buildSessionFactory();

    // 2. Constructor privat untuk mencegah instansiasi kelas HibernateUtil dari luar.
    //    Ini memperkuat pola Singleton untuk kelas HibernateUtil itu sendiri,
    //    meskipun fokus utama kita adalah satu instance SessionFactory.
    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        try {
            // Membuat SessionFactory dari file hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // 3. Metode publik statis untuk menyediakan titik akses global ke instance SessionFactory.
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            getSessionFactory().close();
            System.out.println("Hibernate SessionFactory shutdown.");
        }
    }
}