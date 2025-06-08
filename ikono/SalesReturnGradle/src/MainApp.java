package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.patterns.observer.EmailNotificationObserver;
import org.example.patterns.observer.InventoryUpdaterObserver;
import org.example.repository.SalesReturnRepository;
import org.example.repository.SalesReturnRepositoryImpl;
import org.example.service.SalesReturnService;
import org.example.service.SalesReturnServiceImpl;
import org.example.ui.SalesReturnForm;
import org.example.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        logger.info("Aplikasi dimulai...");
        // Inisialisasi SessionFactory saat aplikasi dimulai (opsional, tapi bagus untuk menangkap error lebih awal)
        // Pemanggilan HibernateUtil.getSessionFactory() pertama kali akan memicu blok statis.
        try {
            HibernateUtil.getSessionFactory(); 
            logger.info("Inisialisasi Hibernate berhasil.");
        } catch (ExceptionInInitializerError e) {
            logger.error("Gagal menginisialisasi Hibernate saat start aplikasi: {}", e.getCause() != null ? e.getCause().getMessage() : e.getMessage(), e);
            // Pertimbangkan untuk keluar dari aplikasi atau menampilkan pesan error fatal ke pengguna
            // System.exit(1); // Atau cara yang lebih baik untuk menangani error fatal
            return; // Jangan launch jika Hibernate gagal
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Memulai JavaFX Application stage...");
        // Setup Dependency Injection secara manual (untuk kesederhanaan)
        // Di aplikasi besar, pertimbangkan framework DI seperti Spring atau Guice

        try {
            // 1. Buat Repository
            SalesReturnRepository salesReturnRepository = new SalesReturnRepositoryImpl();
            logger.debug("SalesReturnRepositoryImpl berhasil dibuat.");

            // 2. Buat Service dan inject repository
            SalesReturnService salesReturnService = new SalesReturnServiceImpl(salesReturnRepository);
            logger.debug("SalesReturnServiceImpl berhasil dibuat.");

            // 3. Daftarkan Observer ke Service
            InventoryUpdaterObserver inventoryObserver = new InventoryUpdaterObserver();
            EmailNotificationObserver emailObserver = new EmailNotificationObserver();
            salesReturnService.registerObserver(inventoryObserver);
            salesReturnService.registerObserver(emailObserver);
            logger.debug("InventoryObserver dan EmailObserver berhasil didaftarkan ke service.");
            // UI Form (SalesReturnForm) juga akan mendaftar sebagai observer di konstruktornya

            // 4. Buat UI Form dan inject service
            SalesReturnForm salesReturnForm = new SalesReturnForm(salesReturnService);
            logger.debug("SalesReturnForm UI berhasil dibuat.");

            // 5. Tampilkan UI
            salesReturnForm.display(primaryStage);
            logger.info("UI berhasil ditampilkan.");

        } catch (Exception e) {
            logger.error("Terjadi error saat start aplikasi JavaFX: {}", e.getMessage(), e);
            // Tampilkan pesan error ke pengguna jika UI gagal dibuat/ditampilkan
            // Alert errorDialog = new Alert(Alert.AlertType.ERROR);
            // errorDialog.setTitle("Error Aplikasi");
            // errorDialog.setHeaderText("Gagal memulai aplikasi.");
            // errorDialog.setContentText("Terjadi kesalahan fatal: " + e.getMessage());
            // errorDialog.showAndWait();
            // Platform.exit(); // Keluar dari aplikasi JavaFX
        }


        // Menambahkan shutdown hook untuk menutup SessionFactory Hibernate saat JVM berhenti
        // Ini adalah best-effort, stop() lebih direkomendasikan untuk aplikasi JavaFX
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Menjalankan shutdown hook JVM...");
            HibernateUtil.shutdown();
        }));
    }

    @Override
    public void stop() throws Exception {
        logger.info("Aplikasi berhenti, menutup resource...");
        // Panggil HibernateUtil.shutdown() di sini untuk memastikan
        // SessionFactory ditutup dengan benar saat aplikasi JavaFX ditutup.
        HibernateUtil.shutdown();
        super.stop(); // Panggil implementasi superclass
        logger.info("Semua resource berhasil ditutup. Aplikasi keluar.");
    }
}
