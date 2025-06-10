package edu.pradita.p14.model.dao;

import edu.pradita.p14.model.entitas.Transaksi;
import edu.pradita.p14.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TransaksiDao {

    /**
     * Menyimpan objek transaksi beserta semua detailnya ke database.
     * @param transaksi Objek transaksi yang sudah lengkap dengan detail item.
     */
    public boolean simpanTransaksi(Transaksi transaksi) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Memulai transaksi
            transaction = session.beginTransaction();

            // Menyimpan objek Transaksi. Karena ada relasi CascadeType.ALL,
            // semua objek DetailTransaksi di dalamnya juga akan ikut tersimpan.
            session.persist(transaksi);

            // Commit transaksi
            transaction.commit();
            return true;
        } catch (Exception e) {
            // Jika terjadi error, rollback transaksi
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}
