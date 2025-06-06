package edu.pradita.p14.javafx.dao;

import edu.pradita.p14.javafx.ShippingMerek;
import edu.pradita.p14.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class ShippingMerekDaoImpl implements ShippingMerekDao {

    // PERUBAHAN: Blok try-catch dihapus agar error bisa dilempar ke controller.
    // Transaksi tetap aman karena menggunakan try-with-resources.
    @Override
    public void save(ShippingMerek merek) throws Exception {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(merek);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            // Lempar lagi errornya agar bisa ditangkap oleh pemanggil (Controller)
            throw new Exception("Gagal menyimpan data ke database. Error: " + e.getMessage());
        }
    }

    @Override
    public void update(ShippingMerek merek) throws Exception {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(merek);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new Exception("Gagal memperbarui data. Error: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws Exception {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ShippingMerek merek = session.get(ShippingMerek.class, id);
            if (merek != null) {
                session.delete(merek);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new Exception("Gagal menghapus data. Error: " + e.getMessage());
        }
    }

    // Sisanya bisa dibiarkan sama atau ditambahkan penanganan error yang serupa jika perlu
    @Override
    public ShippingMerek findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(ShippingMerek.class, id);
        }
    }

    @Override
    public List<ShippingMerek> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from ShippingMerek", ShippingMerek.class).list();
        }
    }

    @Override
    public List<ShippingMerek> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ShippingMerek> query = session.createQuery("from ShippingMerek where name like :name", ShippingMerek.class);
            query.setParameter("name", "%" + name + "%");
            return query.list();
        }
    }
}