package edu.pradita.p14;

import edu.pradita.p14.model.util.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Kelas utama untuk menjalankan aplikasi JavaFX.
 */
public class Main extends Application {

    /**
     * Method start adalah titik masuk utama untuk semua aplikasi JavaFX.
     * @param primaryStage Stage utama yang disediakan oleh platform JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Memuat file FXML dari folder resources.
            // Pastikan path-nya benar sesuai struktur proyek Anda.
            Parent root = FXMLLoader.load(getClass().getResource("/edu/pradita/p14/view/FormTransaksi.fxml"));

            // 2. Membuat Scene baru dengan root dari FXML.
            Scene scene = new Scene(root);

            // 3. Mengatur judul window (Stage).
            primaryStage.setTitle("Aplikasi Kasir Penjualan");

            // 4. Mencegah window di-resize agar tata letak tidak rusak.
            primaryStage.setResizable(false);

            // 5. Menetapkan Scene ke Stage.
            primaryStage.setScene(scene);

            // 6. Menampilkan Stage.
            primaryStage.show();

        } catch (IOException e) {
            // Menangani error jika file FXML tidak ditemukan atau gagal dimuat.
            e.printStackTrace();
            System.err.println("Gagal memuat file FXML. Pastikan file 'FormTransaksi.fxml' ada di package yang benar.");
        }
    }

    /**
     * Method stop dipanggil saat aplikasi akan ditutup.
     * Ini adalah tempat yang tepat untuk melepaskan resource,
     * seperti menutup koneksi database.
     */
    @Override
    public void stop() {
        System.out.println("Menutup koneksi Hibernate...");
        HibernateUtil.shutdown();
    }


    /**
     * Method main yang akan dieksekusi pertama kali.
     * @param args Argumen baris perintah.
     */
    public static void main(String[] args) {
        // Meluncurkan aplikasi JavaFX.
        launch(args);
    }
}
