package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load FXML file from resources
            URL fxmlUrl = getClass().getResource("/org/example/ui/PembeliView.fxml");
            if (fxmlUrl == null) {
                logger.error("Tidak dapat menemukan file FXML: PembeliView.fxml");
                System.err.println("Tidak dapat menemukan file FXML. Pastikan path sudah benar di folder resources.");
                return;
            }
            Parent root = FXMLLoader.load(fxmlUrl);

            Scene scene = new Scene(root);

            primaryStage.setTitle("Manajemen Data Pembeli (Hibernate)");
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(event -> HibernateUtil.shutdown()); // Shutdown Hibernate on close
            primaryStage.show();
            logger.info("Aplikasi berhasil dimulai.");

        } catch (IOException e) {
            logger.error("Gagal memuat FXML atau memulai aplikasi.", e);
            // Handle exception (e.g., show an error dialog)
        } catch (Exception e) {
            logger.error("Terjadi kesalahan tidak terduga saat memulai aplikasi.", e);
        }
    }

    public static void main(String[] args) {
        // Initialize Hibernate SessionFactory (optional, bisa juga lazy init)
        // HibernateUtil.getSessionFactory();
        launch(args);
    }
}