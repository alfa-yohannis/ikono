package edu.pradita.p14.model.dao;

import edu.pradita.p14.model.entitas.Produk;
import edu.pradita.p14.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class ProdukDao {

    /**
     * Mengambil semua data produk dari database.
     * @return List dari semua objek Produk.
     */
    public List<Produk> getAllProduk() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Menggunakan Hibernate Query Language (HQL) untuk mengambil semua produk
            return session.createQuery("from Produk", Produk.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            // Mengembalikan list kosong jika terjadi error
            return null;
        }
    }

    /**
     * Mencari produk berdasarkan ID.
     * @param id ID produk yang dicari.
     * @return Objek Produk jika ditemukan, null jika tidak.
     */
    public Produk getProdukById(String id) {
        Transaction transaction = null;
        Produk produk = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            produk = session.get(Produk.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return produk;
    }

    /**
     * Menyimpan atau memperbarui data produk.
     * @param produk Objek produk yang akan disimpan.
     */
    public void saveOrUpdate(Produk produk) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(produk);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
