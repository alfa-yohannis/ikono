package com.inventory.warehouseinventorysystem.dao.impl;

import com.inventory.warehouseinventorysystem.dao.WarehouseDao;
import com.inventory.warehouseinventorysystem.model.Warehouse;
import com.inventory.warehouseinventorysystem.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

// Import untuk SLF4J Logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WarehouseDaoImpl implements WarehouseDao {

    // Deklarasi SLF4J Logger
    private static final Logger logger = LoggerFactory.getLogger(WarehouseDaoImpl.class);

    @Override
    public Warehouse findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Warehouse.class, id);
        } catch (Exception e) {
            logger.error("Error finding Warehouse by ID: {}", id, e);
            return null;
        }
    }

    @Override
    public List<Warehouse> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Warehouse", Warehouse.class).list();
        } catch (Exception e) {
            logger.error("Error finding all Warehouses", e);
            return null;
        }
    }

    @Override
    public void save(Warehouse warehouse) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(warehouse);
            transaction.commit();
            logger.info("Warehouse saved successfully: {}", warehouse.getName());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error saving Warehouse: {}", warehouse.getName(), e);
            throw new RuntimeException("Could not save warehouse: " + warehouse.getName(), e);
        }
    }

    @Override
    public void update(Warehouse warehouse) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(warehouse);
            transaction.commit();
            logger.info("Warehouse updated successfully: {}", warehouse.getName());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error updating Warehouse: {}", warehouse.getName(), e);
            throw new RuntimeException("Could not update warehouse: " + warehouse.getName(), e);
        }
    }

    /*
    // Metode delete(Warehouse warehouse) yang lama - sekarang kita gunakan deleteById
    @Override
    public void delete(Warehouse warehouse) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(warehouse);
            transaction.commit();
            logger.info("Warehouse (object) deleted successfully: {}", warehouse.getName());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error deleting Warehouse (object): {}", warehouse.getName(), e);
            throw new RuntimeException("Could not delete warehouse: " + warehouse.getName(), e);
        }
    }
    */

    @Override
    public void deleteById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Warehouse warehouse = session.get(Warehouse.class, id); // Ambil entitas dalam sesi ini
            if (warehouse != null) {
                // Pemeriksaan produk bisa dilakukan di sini karena 'warehouse' adalah managed entity
                // dan koleksi 'products' bisa diakses (akan di-load oleh Hibernate jika LAZY).
                // Ini hanya untuk logging, karena cascade akan menghapus produk.
                // Untuk mengakses size, Hibernate akan melakukan query jika LAZY.
                // Untuk menghindari query tambahan jika tidak perlu, bisa dicek dengan Hibernate.isInitialized()
                // atau cukup log bahwa cascade akan terjadi.
                if (warehouse.getProducts() != null && !warehouse.getProducts().isEmpty()) {
                    // Untuk mendapatkan size, Hibernate mungkin perlu query jika LAZY dan belum diinisialisasi
                    // int productCount = warehouse.getProducts().size(); // Ini akan menginisialisasi koleksi
                    logger.warn("Warehouse '{}' (ID: {}) has associated products. They will also be deleted due to cascade settings.",
                            warehouse.getName(), id);
                } else {
                    logger.info("Warehouse '{}' (ID: {}) has no associated products or product list is null/empty.", warehouse.getName(), id);
                }
                session.remove(warehouse); // Menghapus managed entity
                transaction.commit();
                logger.info("Warehouse with ID: {} deleted successfully from database.", id);
            } else {
                // Jika gudang tidak ditemukan, commit transaksi kosong (tidak ada perubahan)
                transaction.commit(); // Penting untuk commit atau rollback transaksi
                logger.warn("Warehouse with ID: {} not found for deletion. No action taken.", id);
                // Anda bisa memilih untuk melempar exception di sini jika gudang tidak ditemukan dianggap error
                // throw new IllegalArgumentException("Warehouse with id " + id + " not found for deletion.");
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error deleting warehouse with ID: {}", id, e);
            throw new RuntimeException("Could not delete warehouse with ID: " + id, e);
        }
    }

    @Override
    public Warehouse findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Warehouse> query = session.createQuery("FROM Warehouse w WHERE w.name = :name", Warehouse.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding Warehouse by name: {}", name, e);
            return null;
        }
    }
}