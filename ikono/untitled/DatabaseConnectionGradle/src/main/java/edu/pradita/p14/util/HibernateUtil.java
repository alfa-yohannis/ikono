package edu.pradita.p14.util;

import edu.pradita.p14.util.strategy.DatabaseStrategy;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import java.util.Properties;

public class HibernateUtil {
    private static volatile SessionFactory sessionFactory;

    private HibernateUtil() {}

    public static SessionFactory getSessionFactory(DatabaseStrategy strategy, String host, String port, String dbName, String username, String password) {
        // Jika sessionFactory sudah ada dan ingin dibuat ulang (misal ganti DB type),
        // pastikan sudah di-shutdown dulu dari luar.
        // Di sini kita akan selalu membuat instance baru jika sessionFactory null.
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) { // Double-checked locking
                    try {
                        Configuration configuration = new Configuration();
                        Properties settings = strategy.getProperties(host, port, dbName, username, password);
                        configuration.setProperties(settings);

                        // Jika Anda punya Entity classes, tambahkan di sini:
                        // configuration.addAnnotatedClass(User.class);
                        // configuration.addAnnotatedClass(Product.class);

                        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                                .applySettings(configuration.getProperties()).build();
                        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                    } catch (Exception e) {
                        // Cetak stack trace untuk debugging
                        // e.printStackTrace();
                        // Lempar kembali exception agar bisa ditangkap oleh UI
                        throw new RuntimeException("Gagal membuat SessionFactory: " + e.getMessage(), e);
                    }
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
        sessionFactory = null; // Set ke null agar bisa dibuat ulang
    }
}