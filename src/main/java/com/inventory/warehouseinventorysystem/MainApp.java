package com.inventory.warehouseinventorysystem;

import com.inventory.warehouseinventorysystem.observer.LoggingObserver;
import com.inventory.warehouseinventorysystem.observer.ProductEventManager;
import com.inventory.warehouseinventorysystem.util.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) {
        ProductEventManager eventManager = ProductEventManager.getInstance();
        eventManager.registerObserver(new LoggingObserver());
        logger.info("LoggingObserver registered.");

        // 2. Muat tampilan utama FXML
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/inventory/warehouseinventorysystem/view/MainView.fxml")));

            Scene scene = new Scene(root, 900, 650);

            primaryStage.setTitle("Warehouse Inventory Management System");
            primaryStage.setScene(scene);
            primaryStage.show();
            logger.info("Application started successfully. MainView loaded.");

        } catch (IOException e) {
            logger.error("Failed to load MainView.fxml:", e); // Menggunakan logger
            showErrorDialog("Application Error", "Could not load the main application window. Please check logs.");
        } catch (NullPointerException e) {
            logger.error("Failed to find MainView.fxml. Check the path in getResource(): /com/inventory/warehouseinventorysystem/view/MainView.fxml", e); // Menggunakan logger
            showErrorDialog("Application Error", "FXML file not found. Ensure path is correct.");
        } catch (Exception e) {
            logger.error("An unexpected error occurred during application start:", e); // Menggunakan logger untuk error umum
            showErrorDialog("Critical Error", "An unexpected error occurred. The application might not work correctly.");
        }
    }

    @Override
    public void stop() throws Exception {
        logger.info("Application is shutting down..."); // Menggunakan logger
        HibernateUtil.shutdown();
        super.stop();
        logger.info("Application has shut down."); // Menggunakan logger
    }

    private void showErrorDialog(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}