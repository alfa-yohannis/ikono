package dokter.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import dokter.dao.SpecializationDAO;
import dokter.model.Specialization;
import dokter.util.HibernateUtil;

public class SpecializationDAOImpl implements SpecializationDAO {

    @Override
    public void save(Specialization specialization) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(specialization);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    @Override
    public Specialization getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Specialization.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Specialization getByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Specialization> query = session.createQuery("FROM Specialization WHERE specializationName = :name", Specialization.class);
            query.setParameter("name", name);
            return query.uniqueResult(); // Expecting one or null
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Specialization> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Specialization", Specialization.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 