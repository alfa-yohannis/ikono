package uas.transaksi.dao.impl; // PERHATIKAN: Paketnya sekarang 'impl'

import uas.transaksi.dao.IShippingMerekDao; // Import interface
import uas.transaksi.data.ShippingMerekData;
import uas.transaksi.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ShippingMerekDaoImpl implements IShippingMerekDao { // Implement interface

    public ShippingMerekDaoImpl() {}

    @Override
    public void saveShippingMerek(ShippingMerekData merekV2) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(merekV2);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error saving shipping merek: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public ShippingMerekData getShippingMerekById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(ShippingMerekData.class, id);
        } catch (Exception e) {
            System.err.println("Error getting shipping merek by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isValidCourierByName(String courierName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<Long> query = session.createQuery("SELECT COUNT(s) FROM ShippingMerekData s WHERE s.name = :name", Long.class);
            query.setParameter("name", courierName);
            Long count = query.getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error checking valid courier by name: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ShippingMerekData> getAllShippingMerek() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from ShippingMerekData", ShippingMerekData.class).list();
        } catch (Exception e) {
            System.err.println("Error getting all shipping merek: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateShippingMerek(ShippingMerekData merekV2) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(merekV2);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating shipping merek: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteShippingMerek(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ShippingMerekData merekV2 = session.get(ShippingMerekData.class, id);
            if (merekV2 != null) {
                session.remove(merekV2);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting shipping merek: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}