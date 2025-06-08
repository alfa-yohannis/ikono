package org.example.repository;

import org.example.model.SalesReturn;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query; // Pastikan import ini benar
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SalesReturnRepositoryImpl implements SalesReturnRepository {
    private static final Logger logger = LoggerFactory.getLogger(SalesReturnRepositoryImpl.class);

    @Override
    public SalesReturn save(SalesReturn salesReturn) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(salesReturn);
            transaction.commit();
            logger.info("Retur penjualan berhasil disimpan dengan ID: {}", salesReturn.getId());
            return salesReturn;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
                logger.warn("Transaksi di-rollback karena gagal menyimpan retur penjualan.", e);
            }
            logger.error("Gagal menyimpan data retur penjualan: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal menyimpan data retur penjualan: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<SalesReturn> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            SalesReturn salesReturn = session.get(SalesReturn.class, id);
            if (salesReturn != null) {
                logger.debug("Retur penjualan ditemukan dengan ID: {}", id);
            } else {
                logger.debug("Retur penjualan dengan ID: {} tidak ditemukan.", id);
            }
            return Optional.ofNullable(salesReturn);
        } catch (Exception e) {
            logger.error("Gagal mencari data retur penjualan berdasarkan ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Gagal mencari data retur penjualan berdasarkan ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<SalesReturn> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<SalesReturn> query = session.createQuery("FROM SalesReturn", SalesReturn.class);
            List<SalesReturn> returns = query.list();
            logger.debug("Berhasil mengambil {} data retur penjualan.", returns.size());
            return returns;
        } catch (Exception e) {
            logger.error("Gagal mengambil semua data retur penjualan: {}", e.getMessage(), e);
            // Kembalikan list kosong jika terjadi error untuk mencegah NullPointerException di layer atas
            return Collections.emptyList(); 
            // atau throw new RuntimeException("Gagal mengambil semua data retur penjualan: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(SalesReturn salesReturn) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(salesReturn);
            transaction.commit();
            logger.info("Retur penjualan dengan ID: {} berhasil diperbarui.", salesReturn.getId());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
                logger.warn("Transaksi di-rollback karena gagal memperbarui retur penjualan ID: {}.", salesReturn.getId(), e);
            }
            logger.error("Gagal memperbarui data retur penjualan ID {}: {}", salesReturn.getId(), e.getMessage(), e);
            throw new RuntimeException("Gagal memperbarui data retur penjualan: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(SalesReturn salesReturn) {
        deleteById(salesReturn.getId());
    }
    
    @Override
    public void deleteById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            SalesReturn salesReturn = session.get(SalesReturn.class, id);
            if (salesReturn != null) {
                session.delete(salesReturn);
                transaction.commit();
                logger.info("Retur penjualan dengan ID: {} berhasil dihapus.", id);
            } else {
                if(transaction != null && transaction.isActive()) transaction.rollback(); // Rollback jika data tidak ada sebelum commit
                logger.warn("Gagal menghapus: Data retur dengan ID {} tidak ditemukan.", id);
                // Anda bisa melempar exception khusus jika data tidak ditemukan
                // throw new EntityNotFoundException("SalesReturn dengan ID " + id + " tidak ditemukan.");
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
                logger.warn("Transaksi di-rollback karena gagal menghapus retur penjualan ID: {}.", id, e);
            }
            logger.error("Gagal menghapus data retur penjualan ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Gagal menghapus data retur penjualan berdasarkan ID: " + e.getMessage(), e);
        }
    }
}