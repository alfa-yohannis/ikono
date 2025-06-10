package edu.pradita.p14.model.dao;

import edu.pradita.p14.model.entitas.Produk;
import edu.pradita.p14.model.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProdukDaoTest {

    private static SessionFactory sessionFactory;
    private ProdukDao produkDao;

    @BeforeAll
    static void setup() {
        // Menggunakan getSessionFactory() akan memuat konfigurasi dari src/test/resources
        // karena lingkungan tes dijalankan dari sana.
        sessionFactory = HibernateUtil.getSessionFactory();
        System.out.println("SessionFactory untuk tes dibuat.");
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("SessionFactory untuk tes ditutup.");
        }
    }

    @BeforeEach
    void setupThis() {
        // Membuat instance DAO baru untuk setiap tes untuk isolasi
        produkDao = new ProdukDao();
    }

    @Test
    @DisplayName("Simpan produk baru dan pastikan tersimpan dengan benar")
    void testSaveOrUpdate_NewProduk() {
        // Arrange: Siapkan data produk baru
        Produk produkBaru = new Produk("PTEST01", "Produk Uji Coba", 50000.0);

        // Act: Simpan produk
        produkDao.saveOrUpdate(produkBaru);

        // Assert: Cari produk yang baru disimpan dan verifikasi datanya
        Produk produkDariDb = produkDao.getProdukById("PTEST01");

        assertNotNull(produkDariDb, "Produk seharusnya ditemukan di database");
        assertEquals("Produk Uji Coba", produkDariDb.getNama(), "Nama produk tidak sesuai");
        assertEquals(50000.0, produkDariDb.getHarga(), "Harga produk tidak sesuai");
    }

    @Test
    @DisplayName("Ambil semua produk setelah beberapa produk disimpan")
    void testGetAllProduk() {
        // Arrange: Simpan beberapa produk
        produkDao.saveOrUpdate(new Produk("PTEST02", "Produk A", 100.0));
        produkDao.saveOrUpdate(new Produk("PTEST03", "Produk B", 200.0));

        // Act: Panggil method getAllProduk
        List<Produk> semuaProduk = produkDao.getAllProduk();

        // Assert: Pastikan jumlah produk yang dikembalikan benar
        assertNotNull(semuaProduk, "List produk tidak boleh null");
        // Ukuran mungkin lebih dari 2 jika tes lain berjalan sebelumnya tanpa isolasi penuh,
        // namun minimal harus ada 2 produk yang baru kita tambahkan.
        assertTrue(semuaProduk.size() >= 2, "Seharusnya ada minimal 2 produk di database");
    }
}
