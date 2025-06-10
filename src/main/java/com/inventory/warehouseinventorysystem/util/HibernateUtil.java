package com.inventory.warehouseinventorysystem.util;

import com.inventory.warehouseinventorysystem.model.Product;
import com.inventory.warehouseinventorysystem.model.Warehouse;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure(); // Memuat hibernate.cfg.xml

            // Mendaftarkan kelas entitas secara programatik
            // Ini memastikan Hibernate tahu tentang entitas Anda,
            // terutama jika Anda tidak menggunakan <mapping class="..."> di hibernate.cfg.xml
            // atau hibernate.packagesToScan.
            configuration.addAnnotatedClass(Warehouse.class);
            configuration.addAnnotatedClass(Product.class);

            System.out.println("Hibernate Configuration loaded and SessionFactory built successfully.");
            return configuration.buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            ex.printStackTrace(); // Cetak stack trace untuk detail error
            // Melempar error ini penting agar aplikasi tahu ada masalah serius saat startup
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (SESSION_FACTORY == null) {
            // Ini seharusnya tidak terjadi jika buildSessionFactory() melempar ExceptionInInitializerError
            // Tapi sebagai pengaman tambahan:
            throw new IllegalStateException("SessionFactory has not been initialized or initialization failed.");
        }
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        if (SESSION_FACTORY != null && !SESSION_FACTORY.isClosed()) {
            System.out.println("Shutting down Hibernate SessionFactory...");
            SESSION_FACTORY.close();
            System.out.println("Hibernate SessionFactory shut down successfully.");
        }
    }
}