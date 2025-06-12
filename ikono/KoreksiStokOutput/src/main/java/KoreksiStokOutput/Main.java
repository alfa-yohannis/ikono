package KoreksiStokOutput;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application { // [cite: 56]

    @Override
    public void start(Stage primaryStage) throws Exception {
        // PERBAIKAN: Tambahkan "/" di awal path dan sertakan nama package
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/KoreksiStokOutput/KoreksiStok.fxml"));

        Parent root = loader.load(); // [cite: 57]
        primaryStage.setTitle("Koreksi Stok Output");
        primaryStage.setScene(new Scene(root));
        primaryStage.show(); // [cite: 58]
    }

    public static void main(String[] args) {
        launch(args);
    }
}