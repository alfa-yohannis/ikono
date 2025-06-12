package KoreksiStokOutput;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StokServiceTest {

    private static SessionFactory sessionFactory;
    private StokService stokService;

    @BeforeAll
    static void setupAll() {
        try {
            sessionFactory = new Configuration().configure("hibernate.test.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            fail("Gagal membuat SessionFactory untuk testing. Error: " + ex.getMessage());
        }
    }

    @BeforeEach
    void setupEach() {
        // Membersihkan data sebelum setiap tes
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM LogKoreksiStok").executeUpdate();
            session.createQuery("DELETE FROM Barang").executeUpdate();
            transaction.commit();
        }

        // Memasukkan data dummy baru
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Barang barang1 = new Barang();
            barang1.setNamaBarang("Buku Uji Coba");
            barang1.setStok(100);
            barang1.setSatuan("Pcs");
            session.save(barang1);
        }

        stokService = new StokService(sessionFactory);
    }

    @AfterAll
    static void tearDownAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    @DisplayName("Harus Berhasil Melakukan Koreksi Stok dan Membuat Log")
    void testKoreksiStok_SuccessAndLogged() throws Exception {
        // Arrange
        Barang barangToUpdate;
        try (Session session = sessionFactory.openSession()) {
            barangToUpdate = session.createQuery("from Barang where namaBarang = 'Buku Uji Coba'", Barang.class).uniqueResult();
        }
        assertNotNull(barangToUpdate, "Barang untuk diuji harusnya ada.");
        int idBarangUji = barangToUpdate.getIdBarang();

        // Act
        stokService.koreksiStok(barangToUpdate, 10);

        // Assert
        try (Session session = sessionFactory.openSession()) {
            // 1. Cek stok barang
            Barang barangAfterUpdate = session.get(Barang.class, idBarangUji);
            assertEquals(90, barangAfterUpdate.getStok());

            // 2. Cek apakah log sudah dibuat
            // =======================================================
            // PERBAIKAN FINAL: Hapus casting (long)
            // =======================================================
            LogKoreksiStok log = session.createQuery("from LogKoreksiStok where barang.id = :id", LogKoreksiStok.class)
                    .setParameter("id", idBarangUji)
                    .uniqueResult();
            assertNotNull(log, "Log untuk koreksi stok seharusnya dibuat.");
            assertEquals(100, log.getStokLama());
            assertEquals(90, log.getStokBaru());
        }
    }
}