package com.pelunasan.dao;

import com.pelunasan.model.Hutang;
import com.pelunasan.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HutangDAO {

    public void simpan(Hutang hutang) {
        Transaction transaksi = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaksi = session.beginTransaction();
            session.persist(hutang);
            transaksi.commit();
        } catch (Exception e) {
            if (transaksi != null) transaksi.rollback();
            e.printStackTrace();
        }
    }

    public List<Hutang> getSemuaHutang() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Hutang", Hutang.class).list();
        }
    }

    public void tandaiLunas(Long id) {
        Transaction transaksi = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaksi = session.beginTransaction();
            Hutang hutang = session.get(Hutang.class, id);
            if (hutang != null) {
                hutang.setLunas(true);
                session.merge(hutang);
            }
            transaksi.commit();
        } catch (Exception e) {
            if (transaksi != null) transaksi.rollback();
            e.printStackTrace();
        }
    }

    public void hapus(Long id) {
        Transaction transaksi = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaksi = session.beginTransaction();
            Hutang hutang = session.get(Hutang.class, id);
            if (hutang != null) {
                session.remove(hutang);
            }
            transaksi.commit();
        } catch (Exception e) {
            if (transaksi != null) transaksi.rollback();
            e.printStackTrace();
        }
    }

    // ✅ Tambahan method untuk mencari hutang berdasarkan ID
    public Hutang cariById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Hutang.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void tutup() {
        HibernateUtil.shutdown();
    }
}
