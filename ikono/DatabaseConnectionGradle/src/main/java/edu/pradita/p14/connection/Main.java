package edu.pradita.p14.connection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Pastikan path FXML benar merujuk ke file di folder resources
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/edu/pradita/p14/connection/DatabaseConnection.fxml")); // Path disesuaikan untuk struktur resources
        Scene scene = new Scene(fxmlLoader.load()); //
        primaryStage.setTitle("Database Connection Settings"); //
        primaryStage.setScene(scene); //
        primaryStage.show(); //
    }

    public static void main(String[] args) {
        launch(args); //
    }
}