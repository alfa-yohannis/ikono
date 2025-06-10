package edu.pradita.p14;

import org.hibernate.Session;
import java.util.List;

public class HibernateTransactionAdapter implements DataProvider {
    @Override
    public List<Transaction> getAllTransactions() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Transaction", Transaction.class).list();
        }
    }

    @Override
    public void saveTransaction(Transaction t) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            org.hibernate.Transaction tx = session.beginTransaction();
            session.save(t);
            tx.commit();
        }
    }

    @Override
    public void updateTransaction(Transaction t) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            org.hibernate.Transaction tx = session.beginTransaction();
            session.update(t);
            tx.commit();
        }
    }

    @Override
    public void deleteTransaction(Transaction t) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            org.hibernate.Transaction tx = session.beginTransaction();
            session.delete(t);
            tx.commit();
        }
    }
} 