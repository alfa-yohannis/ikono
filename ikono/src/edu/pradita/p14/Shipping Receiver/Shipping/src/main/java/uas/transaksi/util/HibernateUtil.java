package uas.transaksi.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);

    static {
        try {
            log.info("Attempting to build Hibernate SessionFactory...");
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            log.info("Hibernate SessionFactory built successfully.");
        } catch (Throwable ex) {
            log.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            log.info("Closing Hibernate SessionFactory.");
            sessionFactory.close();
        }
    }
}