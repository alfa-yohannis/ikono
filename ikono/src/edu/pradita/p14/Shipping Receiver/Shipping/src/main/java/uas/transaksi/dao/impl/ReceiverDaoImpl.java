package uas.transaksi.dao.impl; // PERHATIKAN: Paketnya sekarang 'impl'

import uas.transaksi.dao.IReceiverDao; // Import interface
import uas.transaksi.data.ReceiverData;
import uas.transaksi.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class ReceiverDaoImpl implements IReceiverDao { // Implement interface

    public ReceiverDaoImpl() {}

    @Override
    public void saveReceiver(ReceiverData receiver) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(receiver);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error saving receiver: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public ReceiverData getReceiverById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(ReceiverData.class, id);
        } catch (Exception e) {
            System.err.println("Error getting receiver by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isReceiverExist(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ReceiverData receiver = session.get(ReceiverData.class, id);
            return receiver != null;
        } catch (Exception e) {
            System.err.println("Error checking receiver existence: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ReceiverData> getAllReceivers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from ReceiverData", ReceiverData.class).list();
        } catch (Exception e) {
            System.err.println("Error getting all receivers: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateReceiver(ReceiverData receiver) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(receiver);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating receiver: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteReceiver(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ReceiverData receiver = session.get(ReceiverData.class, id);
            if (receiver != null) {
                session.remove(receiver);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting receiver: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}