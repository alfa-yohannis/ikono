package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MasterModelDaoImplTest {

    private SessionFactory sessionFactory;
    private MasterModelDao dao;

    @BeforeEach
    void setUp() {
        try {
            sessionFactory = new Configuration()
                                .configure("hibernate.cfg.xml") // Dari src/test/resources
                                .buildSessionFactory();
            System.out.println("Test SessionFactory berhasil dibuat dari src/test/resources/hibernate.cfg.xml untuk metode test.");
            dao = new MasterModelDaoImpl(sessionFactory);

            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                List<MasterModel> allItems = session.createQuery("from MasterModel", MasterModel.class).list();
                for (MasterModel item : allItems) {
                    session.remove(item);
                }
                transaction.commit();
                System.out.println("Tabel master_data sudah dibersihkan sebelum test.");
            } catch (Exception e) {
                System.err.println("Gagal membersihkan tabel sebelum test: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Throwable ex) {
            System.err.println("Gagal membuat SessionFactory untuk testing: " + ex);
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    @AfterEach
    void tearDown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("Test SessionFactory sudah ditutup setelah metode test.");
        }
    }

    @Test
    void testSaveAndFindById() {
        System.out.println("Menjalankan testSaveAndFindById...");
        // Tambahkan category saat membuat objek baru
        MasterModel newItem = new MasterModel("TEST001", "Obat Test Hibernate", "120", 25000.75, "Herbal"); 
        dao.save(newItem);

        Optional<MasterModel> foundItemOptional = dao.findById("TEST001");
        assertTrue(foundItemOptional.isPresent(), "Item seharusnya ditemukan setelah disimpan");
        
        MasterModel foundItem = foundItemOptional.get();
        assertEquals("Obat Test Hibernate", foundItem.getName(), "Nama item yang ditemukan harus sesuai");
        assertEquals("120", foundItem.getQuantity(), "Kuantitas item yang ditemukan harus sesuai");
        assertEquals(25000.75, foundItem.getPrice(), 0.001, "Price item yang ditemukan harus sesuai"); 
        assertEquals("Herbal", foundItem.getCategory(), "Category item yang ditemukan harus sesuai"); // Tambahkan assertion untuk category
        System.out.println("testSaveAndFindById selesai.");
    }

    @Test
    void testFindAllWhenEmpty() {
        System.out.println("Menjalankan testFindAllWhenEmpty...");
        List<MasterModel> allItems = dao.findAll();
        assertNotNull(allItems, "List tidak boleh null");
        assertTrue(allItems.isEmpty(), "Database seharusnya kosong di awal test ini setelah pembersihan manual"); 
        System.out.println("testFindAllWhenEmpty selesai.");
    }
    
    @Test
    void testDelete() {
        System.out.println("Menjalankan testDelete...");
        // Tambahkan category saat membuat objek
        MasterModel itemToDelete = new MasterModel("TEST002", "Item Untuk Dihapus", "10", 10000.0, "Obat Keras"); 
        dao.save(itemToDelete); 

        Optional<MasterModel> itemAda = dao.findById("TEST002");
        assertTrue(itemAda.isPresent(), "Item seharusnya ada sebelum dihapus");

        dao.delete(itemToDelete); 

        Optional<MasterModel> itemSudahDihapus = dao.findById("TEST002");
        assertFalse(itemSudahDihapus.isPresent(), "Item seharusnya sudah tidak ada setelah dihapus");
        System.out.println("testDelete selesai.");
    }
}
