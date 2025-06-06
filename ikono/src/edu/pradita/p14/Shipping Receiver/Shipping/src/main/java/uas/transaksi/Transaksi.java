package uas.transaksi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uas.transaksi.factory.DaoFactory;
import uas.transaksi.dao.*; // Import interface DAO
import uas.transaksi.data.*;
import uas.transaksi.util.HibernateUtil;
import uas.transaksi.service.MasterDataService;
import uas.transaksi.service.PengirimanService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.NoResultException;

public class Transaksi extends Application {

    // Gunakan interface DAO
    private ISenderDao senderDao;
    private IReceiverDao receiverDao;
    private IShipmentDao shipmentDao;
    private IShippingMerekDao shippingMerekDao;

    // Service Layer
    private MasterDataService masterDataService;
    private PengirimanService pengirimanService;

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("GUI: Memulai inisialisasi Hibernate SessionFactory...");
        HibernateUtil.getSessionFactory();
        System.out.println("GUI: Hibernate SessionFactory siap.");

        // Inisialisasi implementasi DAO konkret
        senderDao = new SenderDao();
        receiverDao = new ReceiverDao();
        shipmentDao = new ShipmentDao();
        shippingMerekDao = new ShippingMerekDao();

        // Inisialisasi Service Layer dengan DAO yang sudah di-inject (DIP)
        masterDataService = new MasterDataService(senderDao, receiverDao, shippingMerekDao);
        pengirimanService = new PengirimanService(senderDao, receiverDao, shipmentDao);

        try {
            // Sekarang panggil Service Layer untuk inisialisasi data
            if (masterDataService.getSenderById(1) == null) {
                masterDataService.addSender(new SenderData(1, "Budi", "Jakarta"));
            }
            if (masterDataService.getSenderById(2) == null) {
                masterDataService.addSender(new SenderData(2, "Ani", "Surabaya"));
            }

            if (masterDataService.getReceiverById(101) == null) {
                masterDataService.addReceiver(new ReceiverData(101, "Charlie", "Bandung"));
            }
            if (masterDataService.getReceiverById(102) == null) {
                masterDataService.addReceiver(new ReceiverData(102, "Dina", "Medan"));
            }

            if (masterDataService.getAllShippingMereks().isEmpty()) {
                masterDataService.addShippingMerek(new ShippingMerekData("JNE Regular", 15000, "Regular"));
                masterDataService.addShippingMerek(new ShippingMerekData("TIKI Express", 28000, "Express"));
            }

            // Untuk Shipment awal, gunakan Service Layer
            // Gunakan getAllShipments() dari pengirimanService
            if (pengirimanService.getAllShipments().stream().noneMatch(s -> "XYZ-001".equals(s.getShipmentCode()))) {
                pengirimanService.createShipment("XYZ-001", 1, 101, LocalDate.now(), "pending");
                System.out.println("GUI: Menambahkan Shipment awal XYZ-001.");
            } else {
                System.out.println("GUI: Shipment XYZ-001 sudah ada, tidak ditambahkan lagi.");
            }

        } catch (Exception e) {
            System.err.println("GUI: Gagal mengisi data awal secara keseluruhan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("GUI: Metode start() dimulai. Mencoba memuat TransaksiView.fxml.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uas/transaksi/TransaksiView.fxml"));
            Parent root = loader.load();

            TransaksiController mainController = loader.getController();
            // Inject Service Layer ke main controller (DIP)
            mainController.setServices(masterDataService, pengirimanService);

            Scene scene = new Scene(root);

            primaryStage.setTitle("Aplikasi Transaksi Pengiriman");
            primaryStage.setScene(scene);
            primaryStage.show();
            System.out.println("GUI: Jendela utama seharusnya sudah ditampilkan.");

        } catch (Exception e) {
            System.err.println("GUI: Error saat memuat FXML atau memulai aplikasi!");
            System.err.println("Pastikan semua file FXML ada di lokasi yang benar (src/main/resources/uas/transaksi/) dan tidak ada kesalahan sintaks.");
            System.err.println("Detail Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("GUI: Aplikasi GUI ditutup. Menutup koneksi database...");
        HibernateUtil.shutdown();
        System.out.println("GUI: Koneksi database ditutup.");
    }
}