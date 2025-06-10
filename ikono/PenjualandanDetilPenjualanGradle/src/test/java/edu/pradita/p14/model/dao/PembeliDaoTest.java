package edu.pradita.p14.model.dao;

import edu.pradita.p14.model.entitas.Pembeli;
import edu.pradita.p14.model.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import java.lang.reflect.Field; // Import baru

import static org.junit.jupiter.api.Assertions.*;

class PembeliDaoTest {

    private static SessionFactory sessionFactory;
    private PembeliDao pembeliDao;

    @BeforeAll
    static void setup() {
        sessionFactory = HibernateUtil.getSessionFactory();
        System.out.println("SessionFactory untuk tes PembeliDao dibuat.");
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("SessionFactory untuk tes PembeliDao ditutup.");

            // Reset singleton agar kelas tes lain bisa menginisialisasi ulang.
            try {
                Field field = HibernateUtil.class.getDeclaredField("sessionFactory");
                field.setAccessible(true);
                field.set(null, null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.err.println("Gagal mereset SessionFactory: " + e.getMessage());
            }
        }
    }

    @BeforeEach
    void setupThis() {
        pembeliDao = new PembeliDao();
    }

    @Test
    @DisplayName("Simpan pembeli baru dan pastikan data tersimpan")
    void testSaveOrUpdate_NewPembeli() {
        Pembeli pembeliBaru = new Pembeli();
        pembeliBaru.setId("CTEST01");
        pembeliBaru.setNama("Customer Uji Coba");
        pembeliBaru.setTelepon("081299999999");

        pembeliDao.saveOrUpdate(pembeliBaru);

        Pembeli pembeliDariDb = pembeliDao.findById("CTEST01");

        assertNotNull(pembeliDariDb, "Pembeli seharusnya ditemukan di database");
        assertEquals("Customer Uji Coba", pembeliDariDb.getNama(), "Nama pembeli tidak sesuai");
    }

    @Test
    @DisplayName("Cari pembeli dengan ID yang tidak ada, harus mengembalikan null")
    void testFindById_NotFound() {
        Pembeli pembeliDariDb = pembeliDao.findById("TIDAK_ADA");
        assertNull(pembeliDariDb, "Seharusnya mengembalikan null jika pembeli tidak ditemukan");
    }

    @Test
    @DisplayName("Update data pembeli yang sudah ada")
    void testSaveOrUpdate_UpdatePembeli() {
        Pembeli pembeliAwal = new Pembeli();
        pembeliAwal.setId("CTEST02");
        pembeliAwal.setNama("Nama Awal");
        pembeliDao.saveOrUpdate(pembeliAwal);

        Pembeli pembeliUpdate = new Pembeli();
        pembeliUpdate.setId("CTEST02");
        pembeliUpdate.setNama("Nama Setelah Update");
        pembeliDao.saveOrUpdate(pembeliUpdate);

        Pembeli pembeliDariDb = pembeliDao.findById("CTEST02");
        assertNotNull(pembeliDariDb);
        assertEquals("Nama Setelah Update", pembeliDariDb.getNama(), "Nama pembeli seharusnya sudah terupdate");
    }
}
