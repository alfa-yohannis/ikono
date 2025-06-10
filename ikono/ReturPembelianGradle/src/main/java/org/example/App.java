package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.util.HibernateUtil; // Mengimpor utilitas Hibernate

/**
 * Kelas utama aplikasi JavaFX.
 * Bertanggung jawab untuk memulai aplikasi dan antarmuka pengguna.
 */
public class App extends Application {

    /**
     * Metode utama yang dipanggil saat aplikasi JavaFX dimulai.
     * @param primaryStage Stage utama aplikasi.
     * @throws Exception jika terjadi kesalahan saat memuat FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Memuat file FXML untuk antarmuka pengguna.
        // Pastikan path FXML benar dan file berada di folder resources yang sesuai.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/returpembelian.fxml"));
        Parent root = loader.load();

        // Mengatur judul window aplikasi.
        primaryStage.setTitle("Form Retur Pembelian");
        // Membuat scene baru dengan root dari FXML.
        primaryStage.setScene(new Scene(root));
        // Menampilkan stage.
        primaryStage.show();
    }

    /**
     * Metode yang dipanggil saat aplikasi ditutup.
     * Berguna untuk membersihkan resource, seperti menutup SessionFactory Hibernate.
     */
    @Override
    public void stop() {
        HibernateUtil.shutdown(); // Menutup SessionFactory Hibernate
        System.out.println("Aplikasi ditutup, Hibernate SessionFactory telah di-shutdown.");
    }

    /**
     * Metode main untuk menjalankan aplikasi.
     * @param args argumen baris perintah (tidak digunakan dalam aplikasi ini).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
