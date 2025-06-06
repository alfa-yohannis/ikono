package edu.pradita.p14.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.pradita.p14.util.HibernateUtil; // Import HibernateUtil

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // PERBAIKAN: Menggunakan path absolut dari root classpath
        // Tanda "/" di awal sangat penting
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/pradita/p14/javafx/Main.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Aplikasi POS");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Menutup koneksi Hibernate saat aplikasi ditutup
        HibernateUtil.shutdown();
    }
}
