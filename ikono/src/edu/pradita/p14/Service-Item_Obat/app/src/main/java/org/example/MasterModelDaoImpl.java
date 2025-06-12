package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query; // Pastikan import ini ada

import java.util.List;
import java.util.Optional;

public class MasterModelDaoImpl implements MasterModelDao {

    private final SessionFactory sessionFactory;

    // Constructor untuk menerima SessionFactory
    public MasterModelDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(MasterModel model) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(model);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Di aplikasi nyata, gunakan logger
        }
    }

    @Override
    public void update(MasterModel model) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(model); // merge() lebih baik untuk update
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void delete(MasterModel model) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            // Untuk menghapus, pastikan objeknya adalah managed entity
            MasterModel managedModel = session.get(MasterModel.class, model.getId());
            if (managedModel != null) {
                session.remove(managedModel);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Optional<MasterModel> findById(String id) {
        try (Session session = sessionFactory.openSession()) {
            // Operasi baca tidak selalu memerlukan transaksi eksplisit
            return Optional.ofNullable(session.get(MasterModel.class, id));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<MasterModel> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<MasterModel> query = session.createQuery("from MasterModel", MasterModel.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Kembalikan list kosong jika error
        }
    }
}