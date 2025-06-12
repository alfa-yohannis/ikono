package KoreksiStokOutput;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class KoreksiStokController {

    @FXML private ComboBox<Barang> cmbBarang;
    @FXML private Label lblNamaBarang;
    @FXML private Label lblStokSaatIni;
    @FXML private Label lblSatuan;
    @FXML private TextField txtJumlahKoreksi;
    @FXML private Button btnKoreksi;

    private StokService stokService; // Dependensi ke service
    private ObservableList<Barang> listBarang = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        this.stokService = new StokService(HibernateUtil.getSessionFactory());
        loadDataBarang();

        cmbBarang.setItems(listBarang);
        cmbBarang.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                tampilkanDetailBarang(newVal);
            }
        });

        btnKoreksi.setOnAction(event -> koreksiStok());
    }

    private void loadDataBarang() {
        try {
            List<Barang> barangList = stokService.getAllBarang();
            listBarang.setAll(barangList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat data barang: " + e.getMessage());
        }
    }

    private void tampilkanDetailBarang(Barang barang) {
        lblNamaBarang.setText("Nama Barang: " + barang.getNamaBarang());
        lblStokSaatIni.setText("Stok Saat Ini: " + barang.getStok());
        lblSatuan.setText("Satuan: " + barang.getSatuan());
    }

    private void koreksiStok() {
        Barang selectedBarang = cmbBarang.getSelectionModel().getSelectedItem();
        if (selectedBarang == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih barang terlebih dahulu.");
            return;
        }

        int jumlahKoreksi;
        try {
            jumlahKoreksi = Integer.parseInt(txtJumlahKoreksi.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Jumlah koreksi harus berupa angka yang valid.");
            return;
        }

        if (jumlahKoreksi <= 0 || jumlahKoreksi > selectedBarang.getStok()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Jumlah koreksi tidak valid atau melebihi stok.");
            return;
        }

        try {
            // Panggil service untuk melakukan update ke database
            stokService.koreksiStok(selectedBarang, jumlahKoreksi);

            // Jika berhasil, update UI
            selectedBarang.setStok(selectedBarang.getStok() - jumlahKoreksi);
            tampilkanDetailBarang(selectedBarang); // Baris ini sudah cukup untuk me-refresh label stok

            // Baris yang error sudah dihapus dari sini

            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Stok berhasil dikoreksi.");
            txtJumlahKoreksi.clear();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}