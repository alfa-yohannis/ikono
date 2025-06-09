package dokter.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import dokter.dao.DoctorDAO;
import dokter.model.Doctor;
import dokter.util.HibernateUtil;

public class DoctorDAOImpl implements DoctorDAO {

    @Override
    public void saveOrUpdate(Doctor doctor) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(doctor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Doctor getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Doctor.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Doctor> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Doctor> doctors = session.createQuery("FROM Doctor d JOIN FETCH d.specialization", Doctor.class).list();
            doctors.forEach(Doctor::updateProperties); // Ensure JavaFX properties are set
            return doctors;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(Doctor doctor) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(doctor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<Doctor> searchByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Doctor> query = session.createQuery(
                "FROM Doctor d JOIN FETCH d.specialization WHERE LOWER(d.firstName) LIKE LOWER(:keyword) OR LOWER(d.lastName) LIKE LOWER(:keyword) OR LOWER(CONCAT(d.firstName, ' ', d.lastName)) LIKE LOWER(:keyword)", Doctor.class);
            query.setParameter("keyword", "%" + name + "%");
            List<Doctor> doctors = query.list();
            doctors.forEach(Doctor::updateProperties);
            return doctors;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 