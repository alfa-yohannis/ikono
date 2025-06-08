package org.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;

    // Blok inisialisasi statis untuk membuat SessionFactory hanya sekali
    static {
        try {
            // Membuat SessionFactory dari hibernate.cfg.xml
            Configuration configuration = new Configuration().configure(); // Mengambil konfigurasi dari hibernate.cfg.xml
            // Jika ada masalah dengan classloader di JavaFX, coba cara ini:
            // Configuration configuration = new Configuration().configure(HibernateUtil.class.getResource("/hibernate.cfg.xml"));
            // configuration.addAnnotatedClass(org.example.model.SalesReturn.class); // Jika mapping class tidak terbaca
            
            sessionFactory = configuration.buildSessionFactory();
            logger.info("SessionFactory berhasil dibuat.");
        } catch (Throwable ex) {
            // Mencatat exception saat inisialisasi gagal
            logger.error("Inisialisasi SessionFactory gagal.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    // Konstruktor privat untuk mencegah instansiasi dari luar
    private HibernateUtil() {}

    // Metode statis untuk mendapatkan instance SessionFactory
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
             // Ini seharusnya tidak terjadi jika blok statis berhasil
            logger.warn("SessionFactory belum diinisialisasi, mencoba membuat ulang...");
             try {
                Configuration configuration = new Configuration().configure();
                sessionFactory = configuration.buildSessionFactory();
            } catch (Throwable ex) {
                logger.error("Pembuatan ulang SessionFactory gagal.", ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    // Metode untuk menutup SessionFactory (penting untuk membebaskan resource)
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            logger.info("Menutup SessionFactory...");
            getSessionFactory().close();
            logger.info("SessionFactory berhasil ditutup.");
        }
    }
}