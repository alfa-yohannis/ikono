package com.inventory.warehouseinventorysystem.dao.impl;

import com.inventory.warehouseinventorysystem.dao.ProductDao;
import com.inventory.warehouseinventorysystem.model.Product;
import com.inventory.warehouseinventorysystem.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query; // Pastikan import Query dari org.hibernate.query

import java.util.List;

public class ProductDaoImpl implements ProductDao {

    @Override
    public Product findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Product.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Product", Product.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> findByWarehouseId(int warehouseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Pastikan field di entitas Product yang merujuk ke Warehouse adalah 'warehouse'
            // dan ID dari warehouse adalah 'id'
            Query<Product> query = session.createQuery("SELECT p FROM Product p JOIN p.warehouse w WHERE w.id = :warehouseId", Product.class);
            query.setParameter("warehouseId", warehouseId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(Product product) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(product);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void update(Product product) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(product);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Product product) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            // Product managedProduct = session.get(Product.class, product.getId());
            // if (managedProduct != null) {
            //    session.remove(managedProduct);
            // }
            session.remove(product);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Product findBySku(String sku) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> query = session.createQuery("FROM Product p WHERE p.sku = :sku", Product.class);
            query.setParameter("sku", sku);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}