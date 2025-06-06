package edu.pradita.p14.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class MainController {

    @FXML private MenuItem menuItemOpenItem;
    @FXML private MenuItem menuItemOpenSalesOrder;
    @FXML private MenuItem menuItemOpenShippingMerek; // Pastikan fx:id ini ada di FXML utama
    @FXML private BorderPane centerPane;

    private IForm currentForm;

    @FXML
    private void openItem() {
        loadForm("Item.fxml");
    }

    @FXML
    private void openSalesOrder() {
        loadForm("SalesOrder.fxml");
    }

    @FXML
    private void openShippingMerekView() {
        // Memanggil file FXML yang sesuai
        loadForm("PengirimanMerek.fxml");
    }

    /**
     * Memuat file FXML ke tengah BorderPane utama.
     * @param fxmlFile Nama file FXML yang akan dimuat.
     */
    private void loadForm(String fxmlFile) {
        try {
            // Path FXML diasumsikan berada di dalam direktori yang sama dengan Main.fxml
            String fullPath = "/edu/pradita/p14/javafx/" + fxmlFile;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fullPath));

            // Verifikasi apakah resource FXML ditemukan sebelum memuat
            if (loader.getLocation() == null) {
                throw new IOException("Tidak dapat menemukan file FXML: " + fullPath);
            }

            Parent formRoot = loader.load();
            centerPane.setCenter(formRoot);

            // Cek apakah controller yang baru dimuat mengimplementasikan IForm
            Object controller = loader.getController();
            if (controller instanceof IForm) {
                currentForm = (IForm) controller;
            } else {
                currentForm = null;
            }
        } catch (IOException e) {
            System.err.println("Gagal memuat form: " + fxmlFile);
            e.printStackTrace();
            // Di aplikasi nyata, sebaiknya tampilkan dialog kesalahan kepada pengguna.
        }
    }

    @FXML
    public void handleAbout(ActionEvent actionEvent) {
        // Logika untuk menu "About"
    }

    @FXML
    public void handleExit(ActionEvent actionEvent) {
        // Menutup aplikasi
        System.exit(0);
    }

    @FXML
    public void printReport(ActionEvent actionEvent) {
        // Logika untuk cetak laporan
    }

    @FXML
    public void addUser(ActionEvent actionEvent) {
        // Logika untuk tambah user
    }

    @FXML
    public void deleteUser(ActionEvent actionEvent) {
        // Logika untuk hapus user
    }
}