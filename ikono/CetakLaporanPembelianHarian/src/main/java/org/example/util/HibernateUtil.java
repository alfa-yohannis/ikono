package org.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    // Instance SessionFactory dibuat sekali saat kelas dimuat (eager initialization)
    private static final SessionFactory sessionFactory = buildSessionFactory(); //

    // Konstruktor private untuk mencegah instansiasi dari luar (membuat kelas ini utility class murni)
    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() { //
        try {
            // Membuat SessionFactory dari hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory(); //
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex); //
            throw new ExceptionInInitializerError(ex); //
        }
    }

    public static SessionFactory getSessionFactory() { //
        return sessionFactory; //
    }

    public static void shutdown() { //
        if (sessionFactory != null && !sessionFactory.isClosed()) { //
            sessionFactory.close(); //
        }
    }
}
