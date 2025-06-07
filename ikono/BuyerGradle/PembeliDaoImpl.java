package org.example.dao;

import org.example.model.Pembeli;
import org.example.util.HibernateUtil; // Masih dibutuhkan jika getSessionFactory() memanggilnya.
import org.hibernate.Session;
import org.hibernate.SessionFactory; // Import SessionFactory
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

public class PembeliDaoImpl implements PembeliDao {

    private static final Logger logger = LoggerFactory.getLogger(PembeliDaoImpl.class);

    // Metode untuk di-override oleh tes
    protected SessionFactory getSessionFactory() {
        return HibernateUtil.getSessionFactory();
    }

    @Override
    public void save(Pembeli pembeli) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) { // DIPERBARUI untuk menggunakan getSessionFactory()
            transaction = session.beginTransaction();
            session.save(pembeli);
            transaction.commit();
            logger.info("Pembeli saved: {}", pembeli.getNamaLengkap());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving pembeli: {}", pembeli.getNamaLengkap(), e);
            // throw e; // atau handle secara spesifik
        }
    }

    @Override
    public Optional<Pembeli> findById(int id) {
        try (Session session = getSessionFactory().openSession()) { // DIPERBARUI
            Pembeli pembeli = session.get(Pembeli.class, id);
            logger.debug("Finding pembeli by ID: {}, Result: {}", id, (pembeli != null ? pembeli.getNamaLengkap() : "Not Found"));
            return Optional.ofNullable(pembeli);
        } catch (Exception e) {
            logger.error("Error finding pembeli by ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Pembeli> findAll() {
        try (Session session = getSessionFactory().openSession()) { // DIPERBARUI
            CriteriaQuery<Pembeli> criteriaQuery = session.getCriteriaBuilder().createQuery(Pembeli.class);
            criteriaQuery.from(Pembeli.class);
            List<Pembeli> pembeliList = session.createQuery(criteriaQuery).getResultList();
            logger.debug("Fetched all pembeli. Count: {}", pembeliList.size());
            return pembeliList;
        } catch (Exception e) {
            logger.error("Error fetching all pembeli", e);
            return List.of(); // Return empty list on error
        }
    }

    @Override
    public void update(Pembeli pembeli) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) { // DIPERBARUI
            transaction = session.beginTransaction();
            session.update(pembeli);
            transaction.commit();
            logger.info("Pembeli updated: ID {}", pembeli.getIdPembeli());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating pembeli: ID {}", pembeli.getIdPembeli(), e);
        }
    }

    @Override
    public void delete(Pembeli pembeli) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) { // DIPERBARUI
            transaction = session.beginTransaction();
            session.delete(pembeli);
            transaction.commit();
            logger.info("Pembeli deleted: ID {}", pembeli.getIdPembeli());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting pembeli: ID {}", pembeli.getIdPembeli(), e);
        }
    }
    
    @Override
    public void deleteById(int id) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) { // DIPERBARUI
            transaction = session.beginTransaction();
            Pembeli pembeli = session.get(Pembeli.class, id);
            if (pembeli != null) {
                session.delete(pembeli);
                logger.info("Pembeli deleted by ID: {}", id);
            } else {
                logger.warn("Pembeli with ID {} not found for deletion.", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting pembeli by ID: {}", id, e);
        }
    }
}