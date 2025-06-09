package dokter.dao.impl;

import dokter.dao.PatientDAO;
import dokter.model.Patient;
import dokter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    @Override
    public void save(Patient patient) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(patient);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Patient getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Patient.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Patient> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Patient> patients = session.createQuery("FROM Patient", Patient.class).list();
            patients.forEach(Patient::updateProperties); // Ensure JavaFX properties if they will be used
            return patients;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 