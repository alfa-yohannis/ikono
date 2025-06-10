package edu.pradita.uas.category;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Kelas utama untuk meluncurkan aplikasi JavaFX.
 * Tanggung jawab: Hanya untuk memulai aplikasi. (SRP)
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Path menuju file FXML di dalam folder resources.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/pradita/uas/category/view/CategoryView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            primaryStage.setTitle("Category Management (UAS)");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("FATAL: Gagal memuat FXML. Pastikan path sudah benar dan file ada di 'src/main/resources'.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
