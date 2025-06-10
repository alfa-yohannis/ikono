package org.example.config;

import org.example.database.entity.Product; // Pastikan ini ada
import org.example.database.entity.Users;   // Tambahkan import ini
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            // Load configuration from hibernate.cfg.xml
            configuration.configure("hibernate.cfg.xml");

            // Add annotated classes
            configuration.addAnnotatedClass(Product.class);
            configuration.addAnnotatedClass(Users.class); // <--- TAMBAHKAN BARIS INI

            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            getSessionFactory().close();
        }
    }
}