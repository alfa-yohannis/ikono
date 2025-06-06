package uas.transaksi.dao;

import uas.transaksi.data.SenderData;
import uas.transaksi.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class SenderDao implements ISenderDao { // <--- Implement interface

    public SenderDao() {}

    @Override // <--- Tambahkan @Override
    public void saveSender(SenderData sender) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(sender);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error saving sender: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override // <--- Tambahkan @Override
    public SenderData getSenderById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(SenderData.class, id);
        } catch (Exception e) {
            System.err.println("Error getting sender by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override // <--- Tambahkan @Override
    public List<SenderData> getAllSenders() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from SenderData", SenderData.class).list();
        } catch (Exception e) {
            System.err.println("Error getting all senders: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override // <--- Tambahkan @Override
    public void updateSender(SenderData sender) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(sender);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating sender: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override // <--- Tambahkan @Override
    public void deleteSender(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            SenderData sender = session.get(SenderData.class, id);
            if (sender != null) {
                session.remove(sender);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting sender: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}