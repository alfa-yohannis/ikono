package KoreksiStokOutput;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    // 1. Buat instance final dan statis
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Buat SessionFactory dari file hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Gagal membuat SessionFactory." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // 2. Sediakan metode global untuk mengakses instance
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}