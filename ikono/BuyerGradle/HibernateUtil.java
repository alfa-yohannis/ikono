package org.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;

    private HibernateUtil() {
        // Private constructor to prevent instantiation
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Create the SessionFactory from hibernate.cfg.xml
                Configuration configuration = new Configuration().configure(); // Default: hibernate.cfg.xml
                // Anda bisa menambahkan anotasi kelas di sini secara programatik jika diperlukan
                // configuration.addAnnotatedClass(org.example.model.Pembeli.class);
                sessionFactory = configuration.buildSessionFactory();
                logger.info("Hibernate SessionFactory created successfully.");
            } catch (Throwable ex) {
                // Make sure you log the exception, as it might be swallowed
                logger.error("Initial SessionFactory creation failed.", ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            getSessionFactory().close();
            logger.info("Hibernate SessionFactory shut down.");
        }
    }
}

