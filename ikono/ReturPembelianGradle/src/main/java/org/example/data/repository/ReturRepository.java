package org.example.data.repository;

import org.example.data.entity.Pembelian;
import org.example.data.entity.ReturPembelian;
import org.example.util.HibernateUtil;
import org.example.util.Observable; // Menggunakan Observable buatan sendiri
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

/**
 * Repository untuk mengelola operasi CRUD (Create, Read, Update, Delete)
 * untuk entitas ReturPembelian dan Pembelian.
 * Menggunakan pola Observer untuk memberitahu perubahan data.
 */
public class ReturRepository extends Observable {

    /**
     * Menyimpan objek ReturPembelian ke database.
     * Memberi notifikasi kepada observer setelah penyimpanan berhasil.
     * @param retur Objek ReturPembelian yang akan disimpan.
     * @throws Exception jika terjadi kesalahan saat transaksi database.
     */
    public void save(ReturPembelian retur) throws Exception {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(retur); // Menyimpan objek retur
            transaction.commit(); // Menyelesaikan transaksi
            notifyObservers("RETUR_BARU_DISIMPAN"); // Memberi notifikasi kepada observer
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Batalkan transaksi jika terjadi error
            }
            // Melempar kembali exception untuk ditangani di lapisan atas
            throw new Exception("Gagal menyimpan retur: " + e.getMessage(), e);
        }
    }

    /**
     * Mengambil semua data Pembelian dari database.
     * @return List objek Pembelian, atau list kosong jika tidak ada data atau terjadi error.
     */
    public List<Pembelian> findAllPembelian() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Menggunakan HQL (Hibernate Query Language) untuk mengambil data
            Query<Pembelian> query = session.createQuery("FROM Pembelian p LEFT JOIN FETCH p.products", Pembelian.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace(); // Cetak stack trace untuk debugging
            // Mengembalikan list kosong jika terjadi error untuk menghindari NullPointerException di controller
            return Collections.emptyList();
        }
    }

    /**
     * (Contoh metode tambahan, bisa Anda implementasikan jika perlu)
     * Mencari Pembelian berdasarkan ID.
     * @param idPembelian ID Pembelian yang dicari.
     * @return Objek Pembelian jika ditemukan, null jika tidak.
     */
    public Pembelian findPembelianById(String idPembelian) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Pembelian.class, idPembelian);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}