package edu.pradita.uas.category.repository;

import edu.pradita.uas.category.model.Category;
import edu.pradita.uas.category.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test untuk CategoryRepository.
 * Test ini sekarang kompatibel dengan H2 dan MySQL.
 */
class CategoryRepositoryTest {

    private static SessionFactory sessionFactory;
    private CategoryRepository repository;

    @BeforeAll
    static void setUpAll() {
        // Inisialisasi SessionFactory sekali saja untuk semua tes
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @AfterAll
    static void tearDownAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        // Buat instance repository baru untuk setiap tes
        repository = new CategoryRepository(sessionFactory);

        // Bersihkan tabel sebelum setiap tes untuk isolasi
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            // Perintah TRUNCATE TABLE kompatibel dengan MySQL dan H2 untuk menghapus semua baris.
            // Di MySQL, ini secara otomatis me-reset auto-increment.
            // Di H2, ini juga berfungsi seperti yang diharapkan.
            session.createNativeQuery("TRUNCATE TABLE categories", void.class).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.err.println("Could not truncate table: " + e.getMessage());
            // Fallback untuk beberapa database atau konfigurasi keamanan
            // di mana TRUNCATE tidak diizinkan.
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();
                // Menggunakan createMutationQuery sebagai pengganti createQuery yang usang untuk operasi DELETE.
                session.createMutationQuery("DELETE FROM Category").executeUpdate();
                // Untuk MySQL, reset auto_increment secara manual setelah DELETE
                session.createNativeQuery("ALTER TABLE categories AUTO_INCREMENT = 1", void.class).executeUpdate();
                tx.commit();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @Test
    @DisplayName("Should save a category and find it by ID")
    void testSaveAndFindById() {
        Category newCategory = new Category("Electronics", null);
        repository.save(newCategory);

        // ID seharusnya di-generate oleh database
        assertNotEquals(0, newCategory.getId());

        Category found = repository.findById(newCategory.getId());
        assertNotNull(found);
        assertEquals("Electronics", found.getName());
    }

    @Test
    @DisplayName("Should return all saved categories")
    void testFindAll() {
        repository.save(new Category("Books", null));
        repository.save(new Category("Music", null));

        List<Category> categories = repository.findAll();
        assertEquals(2, categories.size());
    }

    @Test
    @DisplayName("Should update an existing category")
    void testUpdate() {
        Category category = new Category("Laptops", null);
        repository.save(category);

        category.setName("Gaming Laptops");
        repository.update(category);

        Category updated = repository.findById(category.getId());
        assertEquals("Gaming Laptops", updated.getName());
    }

    @Test
    @DisplayName("Should delete a category by ID")
    void testDelete() {
        Category category = new Category("To Be Deleted", null);
        repository.save(category);

        int id = category.getId();
        assertNotNull(repository.findById(id));

        repository.delete(id);
        assertNull(repository.findById(id));
    }

    @Test
    @DisplayName("Should correctly identify if a category is a parent")
    void isParent() {
        Category parent = new Category("Clothing", null);
        repository.save(parent);

        Category child = new Category("Shirts", parent);
        repository.save(child);

        assertTrue(repository.isParent(parent.getId()));
        assertFalse(repository.isParent(child.getId()));
    }
}
