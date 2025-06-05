package com.pelunasan.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
    try {
        System.out.println("===> Mencoba load hibernate.cfg.xml...");
        Configuration configuration = new Configuration().configure();
        System.out.println("===> Hibernate configuration loaded.");
        return configuration.buildSessionFactory();
    } catch (Throwable ex) {
        System.err.println("Initial SessionFactory creation failed: " + ex);
        throw new ExceptionInInitializerError(ex);
    }
}


    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
