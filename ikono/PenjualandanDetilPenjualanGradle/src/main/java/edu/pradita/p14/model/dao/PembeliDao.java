package edu.pradita.p14.model.dao;

import edu.pradita.p14.model.entitas.Pembeli;
import edu.pradita.p14.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PembeliDao {

    /**
     * Menyimpan atau memperbarui data pembeli.
     * Jika ID pembeli sudah ada, Hibernate akan melakukan update.
     * Jika belum, akan melakukan insert.
     *
     * @param pembeli Objek Pembeli yang akan disimpan.
     */
    public void saveOrUpdate(Pembeli pembeli) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(pembeli);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Mencari pembeli berdasarkan ID-nya.
     *
     * @param id ID pembeli yang ingin dicari.
     * @return Objek Pembeli jika ditemukan, atau null jika tidak ada.
     */
    public Pembeli findById(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Menggunakan method get dari session untuk mencari berdasarkan primary key
            return session.get(Pembeli.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
