package com.example.uas.util;

public class HibernateUtil {
    private static org.hibernate.boot.registry.StandardServiceRegistry registry;
    private static org.hibernate.SessionFactory sessionFactory;

    public static org.hibernate.SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                registry = new org.hibernate.boot.registry.StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .build();
                org.hibernate.boot.MetadataSources sources = new org.hibernate.boot.MetadataSources(registry);
                org.hibernate.boot.Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    org.hibernate.boot.registry.StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            org.hibernate.boot.registry.StandardServiceRegistryBuilder.destroy(registry);
        }
    }
} 