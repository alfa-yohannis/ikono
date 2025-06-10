package edu.pradita.uas.category.repository;

import edu.pradita.uas.category.model.Category;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

/**
 * Lapisan Akses Data (Data Access Layer) untuk entitas Category.
 * Tanggung jawab: Hanya berinteraksi langsung dengan database melalui Hibernate. (SRP)
 */
public class CategoryRepository {

    private final SessionFactory sessionFactory;

    public CategoryRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Category category) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(category); // Menggunakan persist untuk objek baru
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(Category category) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(category); // Menggunakan merge untuk update objek yang ada
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Category category = session.get(Category.class, id);
            if (category != null) {
                session.remove(category); // Menggunakan remove untuk menghapus
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Category findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Category.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Category> findAll() {
        try (Session session = sessionFactory.openSession()) {
            // Menggunakan HQL (Hibernate Query Language) untuk mengambil semua data
            return session.createQuery("FROM Category", Category.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Mengembalikan list kosong jika terjadi error
        }
    }

    public boolean isParent(int id) {
        try (Session session = sessionFactory.openSession()) {
            // Query untuk mengecek apakah sebuah kategori menjadi parent bagi kategori lain
            Long count = session.createQuery("SELECT count(c) FROM Category c WHERE c.parent.id = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
