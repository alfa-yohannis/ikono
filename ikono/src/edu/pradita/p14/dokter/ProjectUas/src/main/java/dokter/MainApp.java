package dokter;

import dokter.util.HibernateUtil; // Import for shutdown
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        DoctorController controller = new DoctorController();

        Scene scene = new Scene(controller.getView(), 1000, 700); // Adjusted size
        primaryStage.setScene(scene);
        primaryStage.setTitle("Master Data Dokter");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        HibernateUtil.shutdown(); // Gracefully close Hibernate SessionFactory
        super.stop();
    }
} 