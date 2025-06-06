package uas.transaksi;

import org.hibernate.Session;
import org.junit.jupiter.api.*;

// Ganti import ini!
// import jakarta.transaction.Transaction; // HAPUS BARIS INI ATAU JADIKAN KOMENTAR

import org.hibernate.Transaction; // <--- GANTI DENGAN INI! (Pastikan org.hibernate.Transaction)

import uas.transaksi.dao.*;
import uas.transaksi.data.*;
import uas.transaksi.util.HibernateUtil; // Import HibernateUtil

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransaksiTest {

    // Instances dari semua DAO yang akan diuji
    private SenderDao senderDao;
    private ReceiverDao receiverDao;
    private ShipmentDao shipmentDao;
    private ShippingMerekDao shippingMerekDao;

    // Data dummy yang akan digunakan
    private SenderData sender1;
    private SenderData sender2;
    private ReceiverData receiver1;
    private ReceiverData receiver2;
    private ShippingMerekData merek1;
    private ShippingMerekData merek2;

    @BeforeAll
    void setUpTestDatabase() {
        System.out.println("Menyiapkan database untuk testing dengan Hibernate...");
        // Inisialisasi SessionFactory Hibernate untuk memastikan skema dibuat (jika hbm2ddl.auto=create-drop)
        HibernateUtil.getSessionFactory();
        System.out.println("Database dan skema tes siap (dikelola oleh Hibernate).");
    }

    @BeforeEach
    void setUp() {
        // Inisialisasi DAO sebelum setiap tes
        senderDao = new SenderDao();
        receiverDao = new ReceiverDao();
        shipmentDao = new ShipmentDao();
        shippingMerekDao = new ShippingMerekDao();

        // Kosongkan semua tabel sebelum setiap tes
        // Karena kita pakai Hibernate, kita bisa menghapus data melalui HQL
        // Hapus dalam urutan yang benar jika ada foreign key (Shipment dulu, baru Sender/Receiver)
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction(); // <--- HAPUS CASTING '(Transaction)' di sini!
            session.createQuery("DELETE FROM ShipmentData").executeUpdate();
            session.createQuery("DELETE FROM ReceiverData").executeUpdate();
            session.createQuery("DELETE FROM SenderData").executeUpdate();
            session.createQuery("DELETE FROM ShippingMerekData").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Gagal mengosongkan tabel sebelum tes: " + e.getMessage());
        }

        // Masukkan data awal menggunakan DAO Hibernate
        try {
            sender1 = new SenderData(1, "Budi", "Jakarta");
            sender2 = new SenderData(2, "Ani", "Surabaya");
            senderDao.saveSender(sender1);
            senderDao.saveSender(sender2);

            receiver1 = new ReceiverData(101, "Charlie", "Bandung");
            receiver2 = new ReceiverData(102, "Dina", "Medan");
            receiverDao.saveReceiver(receiver1);
            receiverDao.saveReceiver(receiver2);

            // Perhatian: ID merek1 dan merek2 akan otomatis di-generate karena ada @GeneratedValue(strategy = GenerationType.IDENTITY)
            merek1 = new ShippingMerekData("JNE Regular", 15000, "Regular");
            merek2 = new ShippingMerekData("TIKI Express", 28000, "Express");
            shippingMerekDao.saveShippingMerek(merek1);
            shippingMerekDao.saveShippingMerek(merek2);

            Date today = new Date(System.currentTimeMillis());
            ShipmentData initialShipment = new ShipmentData("XYZ-001", sender1, receiver1, today, "pending");
            shipmentDao.saveShipment(initialShipment);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Gagal mengisi data awal untuk tes: " + e.getMessage());
        }
    }

    // --- Tes untuk SenderDao ---
    @Test
    void testGetAllSenders_ShouldReturnTwoSenders() {
        List<SenderData> senders = senderDao.getAllSenders();
        assertNotNull(senders);
        assertEquals(2, senders.size());
        // Perhatikan urutan data yang diambil dari DB mungkin tidak terjamin tanpa ORDER BY
        // Untuk test, kita bisa asumsikan urutan berdasarkan ID atau memfilter
        assertTrue(senders.stream().anyMatch(s -> s.getName().equals("Budi") && s.getId() == 1));
        assertTrue(senders.stream().anyMatch(s -> s.getName().equals("Ani") && s.getId() == 2));
    }

    @Test
    void testGetSenderById_ShouldReturnCorrectSender() {
        SenderData foundSender = senderDao.getSenderById(1);
        assertNotNull(foundSender);
        assertEquals("Budi", foundSender.getName());
    }

    @Test
    void testUpdateSender_ShouldChangeSenderAddress() {
        sender1.setAddress("Yogyakarta");
        senderDao.updateSender(sender1);
        SenderData updatedSender = senderDao.getSenderById(sender1.getId());
        assertNotNull(updatedSender);
        assertEquals("Yogyakarta", updatedSender.getAddress());
    }

    @Test
    void testDeleteSender_ShouldDecreaseSenderCount() {
        senderDao.deleteSender(sender2.getId());
        List<SenderData> senders = senderDao.getAllSenders();
        assertEquals(1, senders.size());
        assertNull(senderDao.getSenderById(sender2.getId()));
    }

    // --- Tes untuk ReceiverDao ---
    @Test
    void testSaveReceiver_AndCheckExistence() {
        ReceiverData newReceiver = new ReceiverData(103, "Eko", "Yogyakarta");
        receiverDao.saveReceiver(newReceiver);
        assertTrue(receiverDao.isReceiverExist(103));
        ReceiverData fetchedReceiver = receiverDao.getReceiverById(103);
        assertNotNull(fetchedReceiver);
        assertEquals("Eko", fetchedReceiver.getName());
    }

    @Test
    void testIsReceiverExist_ShouldBeTrueForExisting() {
        assertTrue(receiverDao.isReceiverExist(101));
    }

    @Test
    void testIsReceiverExist_ShouldBeFalseForNonExisting() {
        assertFalse(receiverDao.isReceiverExist(999));
    }

    @Test
    void testGetAllReceivers_ShouldReturnTwoReceivers() {
        List<ReceiverData> receivers = receiverDao.getAllReceivers();
        assertNotNull(receivers);
        assertEquals(2, receivers.size());
    }

    // --- Tes untuk ShippingMerekDao ---
    @Test
    void testIsValidCourierByName_ShouldBeTrueForExisting() {
        assertTrue(shippingMerekDao.isValidCourierByName("JNE Regular"));
    }

    @Test
    void testIsValidCourierByName_ShouldBeFalseForNonExisting() {
        assertFalse(shippingMerekDao.isValidCourierByName("POS Kilat"));
    }

    @Test
    void testGetAllShippingMerek_ShouldReturnTwo() {
        List<ShippingMerekData> mereks = shippingMerekDao.getAllShippingMerek();
        assertNotNull(mereks);
        assertEquals(2, mereks.size());
    }

    @Test
    void testSaveNewShippingMerek_ShouldIncreaseCount() {
        ShippingMerekData newMerek = new ShippingMerekData("Wahana", 12000, "Reguler");
        shippingMerekDao.saveShippingMerek(newMerek);
        List<ShippingMerekData> mereks = shippingMerekDao.getAllShippingMerek();
        assertEquals(3, mereks.size());
    }

    // --- Tes untuk ShipmentDao ---
    @Test
    void testAddShipment_ShouldIncreaseShipmentCount() {
        Date today = new Date(System.currentTimeMillis());
        ShipmentData newShipment = new ShipmentData("ABC-002", sender2, receiver2, today, "delivered");
        shipmentDao.saveShipment(newShipment);

        List<ShipmentData> shipments = shipmentDao.getAllShipments();
        assertEquals(2, shipments.size(), "Jumlah shipment seharusnya menjadi 2 setelah ditambah.");

        ShipmentData fetchedShipment = shipments.stream()
                                                .filter(s -> "ABC-002".equals(s.getShipmentCode()))
                                                .findFirst()
                                                .orElse(null);
        assertNotNull(fetchedShipment);
        assertEquals("Ani", fetchedShipment.getSender().getName());
        assertEquals("Dina", fetchedShipment.getReceiver().getName());
    }

    @Test
    void testGetShipmentById_ShouldReturnCorrectShipmentWithRelations() {
        // Karena ID shippingmerek di-generate, kita perlu mengambil ID yang sebenarnya dari DB
        // Atau asumsikan ID 1 untuk shipment awal jika itu selalu kasusnya
        List<ShipmentData> allShipments = shipmentDao.getAllShipments();
        assertFalse(allShipments.isEmpty(), "Harusnya ada setidaknya satu shipment yang dimuat.");

        // Ambil shipment pertama yang diketahui
        ShipmentData initialShipmentFromDb = allShipments.stream()
                                                    .filter(s -> "XYZ-001".equals(s.getShipmentCode()))
                                                    .findFirst()
                                                    .orElse(null);
        assertNotNull(initialShipmentFromDb, "Shipment awal 'XYZ-001' harus ditemukan di DB.");


        ShipmentData fetchedShipment = shipmentDao.getShipmentById(initialShipmentFromDb.getId());

        assertNotNull(fetchedShipment);
        assertEquals("XYZ-001", fetchedShipment.getShipmentCode());
        assertEquals("Budi", fetchedShipment.getSender().getName()); // Memastikan relasi dimuat
        assertEquals("Charlie", fetchedShipment.getReceiver().getName()); // Memastikan relasi dimuat
    }


    @AfterAll
    void tearDownTestDatabase() {
        System.out.println("Membersihkan SessionFactory Hibernate dan database testing...");
        HibernateUtil.shutdown(); // Menutup SessionFactory Hibernate
        System.out.println("Database warehouses_id_test dibersihkan oleh Hibernate.");
    }
}