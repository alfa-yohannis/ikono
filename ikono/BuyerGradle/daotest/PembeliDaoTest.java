package org.example.dao;

import org.example.model.Pembeli;
import org.hibernate.Session; 
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import java.net.URL; // Ditambahkan untuk pemeriksaan resource
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PembeliDaoTest {

    private PembeliDao pembeliDao;

    static class TestHibernateUtil {
        private static SessionFactory sf = null;

        public static SessionFactory getSessionFactory() {
            if (sf == null || sf.isClosed()) { 
                try {
                    Configuration configuration = new Configuration();
                    
                    // --- DEBUGGING: Periksa apakah file konfigurasi ditemukan ---
                    URL configFileUrl = TestHibernateUtil.class.getClassLoader().getResource("hibernate.cfg.test.xml");
                    if (configFileUrl == null) {
                        System.err.println("KESALAHAN FATAL: hibernate.cfg.test.xml tidak ditemukan di classpath tes!");
                        throw new RuntimeException("hibernate.cfg.test.xml tidak ditemukan. Pastikan ada di src/test/resources.");
                    }
                    System.out.println("INFO Tes: Menggunakan file konfigurasi Hibernate: " + configFileUrl.getPath());
                    // --- Akhir Debugging ---

                    configuration.configure("hibernate.cfg.test.xml"); // Seharusnya mencari di root classpath tes
                    
                    sf = configuration.buildSessionFactory();
                    System.out.println("INFO Tes: Test SessionFactory berhasil dibuat.");
                } catch (Throwable ex) { // Tangkap Throwable untuk error yang lebih luas
                    System.err.println("--- KESALAHAN INISIALISASI HIBERNATE UNTUK TES ---");
                    System.err.println("Pembuatan SessionFactory awal gagal. Jenis Exception: " + ex.getClass().getName());
                    System.err.println("Pesan Exception: " + ex.getMessage());
                    System.err.println("Stack Trace Lengkap dari Exception Asli:");
                    ex.printStackTrace(System.err); // Cetak stack trace asli ke System.err
                    System.err.println("--- AKHIR KESALAHAN INISIALISASI HIBERNATE ---");
                    throw new ExceptionInInitializerError(ex); // Bungkus dan lempar ulang
                }
            }
            return sf;
        }

        public static void shutdown() {
            if (sf != null && !sf.isClosed()) {
                sf.close();
                sf = null; 
                System.out.println("INFO Tes: Test SessionFactory ditutup.");
            }
        }
    }

    @BeforeAll
    static void setUpBeforeClass() {
        // SessionFactory akan diinisialisasi saat pertama kali dibutuhkan
    }

    @BeforeEach
    void setUp() {
        // Inisialisasi DAO dengan implementasi anonim yang menggunakan TestHibernateUtil
        pembeliDao = new PembeliDaoImpl() {
            @Override
            protected SessionFactory getSessionFactory() { 
                return TestHibernateUtil.getSessionFactory(); // Ini akan memicu inisialisasi sf jika null
            }
        };

        // Bersihkan tabel sebelum setiap tes
        try (Session session = TestHibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.createQuery("DELETE FROM Pembeli").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.err.println("Error selama pembersihan pengaturan tes (setUp @BeforeEach): " + e.getMessage());
            e.printStackTrace(System.err);
            // Jika ini kritis, Anda mungkin ingin melempar RuntimeException
            // throw new RuntimeException("Gagal membersihkan database untuk tes", e);
        }
    }

    @AfterEach
    void tearDownAfterEach() {
        // Tidak ada tindakan khusus di sini, @AfterAll akan menangani shutdown SessionFactory
    }

    @AfterAll
    static void tearDownAfterClass() {
        TestHibernateUtil.shutdown();
    }

    @Test
    void saveAndFindById() {
        Pembeli pembeli = new Pembeli("Test User", "Test Address", "Test City", "123", "081", "test@example.com", Pembeli.JenisKelamin.L, Pembeli.StatusPembeli.Aktif);
        pembeliDao.save(pembeli);
        assertNotEquals(0, pembeli.getIdPembeli(), "ID harus digenerate setelah save.");

        Optional<Pembeli> found = pembeliDao.findById(pembeli.getIdPembeli());
        assertTrue(found.isPresent(), "Pembeli harus ditemukan berdasarkan ID.");
        assertEquals("Test User", found.get().getNamaLengkap());
    }

    @Test
    void findAll_WhenEmpty_ShouldReturnEmptyList() {
        List<Pembeli> allPembeli = pembeliDao.findAll();
        assertTrue(allPembeli.isEmpty(), "Harus mengembalikan list kosong jika tidak ada pembeli.");
    }

    @Test
    void findAll_WithData_ShouldReturnList() {
        Pembeli p1 = new Pembeli("User A", "Addr A", "City A", "111", "081A", "a@ex.com", Pembeli.JenisKelamin.L, Pembeli.StatusPembeli.Aktif);
        Pembeli p2 = new Pembeli("User B", "Addr B", "City B", "222", "081B", "b@ex.com", Pembeli.JenisKelamin.P, Pembeli.StatusPembeli.Non_Aktif);
        pembeliDao.save(p1);
        pembeliDao.save(p2);

        List<Pembeli> allPembeli = pembeliDao.findAll();
        assertEquals(2, allPembeli.size(), "Harus mengembalikan list dengan 2 pembeli.");
    }
    
    @Test
    void update() {
        Pembeli pembeli = new Pembeli("Initial Name", "Initial Address", "Initial City", "123", "081", "initial@example.com", Pembeli.JenisKelamin.L, Pembeli.StatusPembeli.Aktif);
        pembeliDao.save(pembeli);

        int id = pembeli.getIdPembeli();
        Pembeli toUpdate = pembeliDao.findById(id).orElseThrow(() -> new AssertionError("Pembeli tidak ditemukan untuk update"));
        toUpdate.setNamaLengkap("Updated Name");
        toUpdate.setKota("Updated City");
        pembeliDao.update(toUpdate);

        Pembeli updated = pembeliDao.findById(id).orElseThrow(() -> new AssertionError("Pembeli yang diupdate tidak ditemukan"));
        assertEquals("Updated Name", updated.getNamaLengkap());
        assertEquals("Updated City", updated.getKota());
    }

    @Test
    void deleteById() {
        Pembeli pembeli = new Pembeli("To Delete", "Delete Address", "Delete City", "123", "081", "delete@example.com", Pembeli.JenisKelamin.L, Pembeli.StatusPembeli.Aktif);
        pembeliDao.save(pembeli);
        int id = pembeli.getIdPembeli();

        assertTrue(pembeliDao.findById(id).isPresent(), "Pembeli harus ada sebelum delete.");
        pembeliDao.deleteById(id);
        assertFalse(pembeliDao.findById(id).isPresent(), "Pembeli seharusnya tidak ada setelah delete.");
    }
}