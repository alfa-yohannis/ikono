package org.example.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.data.entity.Pembelian;
import org.example.data.entity.Product;
import org.example.data.entity.ReturPembelian;
import org.example.data.repository.ReturRepository;
import org.example.service.NotificationDecorator;
import org.example.service.ReturService;
import org.example.service.ReturServiceInterface;

import java.util.List;
import java.util.Observer; // Menggunakan Observer dari java.util
import java.util.stream.Collectors;

/**
 * Controller untuk antarmuka pengguna Form Retur Pembelian.
 * Mengimplementasikan Observer untuk merespons perubahan data dari repository.
 */
@SuppressWarnings("deprecation") // Observer dan Observable sudah deprecated, namun masih digunakan di sini
public class ReturPembelianController implements Observer {

    // Injeksi komponen FXML
    @FXML private ComboBox<Pembelian> comboIDPembelian;
    @FXML private ComboBox<Product> comboProduk;
    @FXML private TextField textJumlah;
    @FXML private TextArea textAlasan;

    // Dependensi ke repository dan service
    private final ReturRepository returRepository = new ReturRepository();
    // Menggunakan Decorator Pattern untuk service
    private final ReturServiceInterface returService = new NotificationDecorator(new ReturService(returRepository));

    /**
     * Metode inisialisasi yang dipanggil setelah semua field FXML diinjeksi.
     */
    @FXML
    public void initialize() {
        returRepository.addObserver(this); // Mendaftarkan controller sebagai observer ke repository
        setupComboBoxConverters();
        loadPembelianData();

        // Menambahkan listener untuk perubahan pilihan pada comboIDPembelian
        comboIDPembelian.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.getProducts() != null) {
                // Mengisi comboProduk berdasarkan produk yang ada di pembelian terpilih
                comboProduk.setItems(FXCollections.observableArrayList(newVal.getProducts()));
            } else {
                comboProduk.getItems().clear(); // Kosongkan jika tidak ada pembelian terpilih
            }
        });
    }

    /**
     * Mengatur StringConverter untuk ComboBox agar menampilkan teks yang benar.
     */
    private void setupComboBoxConverters() {
        comboIDPembelian.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Pembelian pembelian) {
                return pembelian == null ? null : pembelian.getIdPembelian();
            }
            @Override
            public Pembelian fromString(String string) { return null; /* Tidak digunakan untuk konversi balik */ }
        });

        comboProduk.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Product product) {
                return product == null ? null : product.getName();
            }
            @Override
            public Product fromString(String string) { return null; /* Tidak digunakan untuk konversi balik */ }
        });
    }

    /**
     * Memuat data pembelian dari repository dan menampilkannya di ComboBox.
     */
    private void loadPembelianData() {
        try {
            List<Pembelian> pembelianList = returRepository.findAllPembelian();
            if (pembelianList != null) {
                comboIDPembelian.setItems(FXCollections.observableList(pembelianList));
            } else {
                 comboIDPembelian.getItems().clear();
                 showAlert(Alert.AlertType.WARNING, "Data Kosong", "Tidak ada data pembelian yang ditemukan.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Kesalahan Database", "Gagal memuat data pembelian: " + e.getMessage());
        }
    }

    /**
     * Handler untuk tombol Simpan.
     * Memvalidasi input dan menyimpan data retur jika valid.
     */
    @FXML
    public void handlerSimpan() {
        Pembelian selectedPembelian = comboIDPembelian.getValue();
        Product selectedProduct = comboProduk.getValue();
        String jumlahStr = textJumlah.getText();
        String alasan = textAlasan.getText();

        // Validasi input
        if (selectedPembelian == null || selectedProduct == null || jumlahStr.isBlank() || alasan.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan Validasi", "Mohon lengkapi semua field yang wajib diisi.");
            return;
        }

        int jumlah;
        try {
            jumlah = Integer.parseInt(jumlahStr);
            if (jumlah <= 0) {
                showAlert(Alert.AlertType.ERROR, "Kesalahan Validasi", "Jumlah retur harus lebih besar dari 0.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan Validasi", "Jumlah retur harus berupa angka.");
            return;
        }

        // Membuat objek ReturPembelian
        ReturPembelian retur = new ReturPembelian();
        retur.setPembelian(selectedPembelian);
        retur.setProduct(selectedProduct);
        retur.setJumlah(jumlah);
        retur.setAlasanRetur(alasan);

        try {
            returService.save(retur); // Menyimpan data melalui service
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Retur pembelian berhasil disimpan.");
            resetForm(); // Mengosongkan form setelah berhasil disimpan
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Kesalahan Penyimpanan", "Gagal menyimpan retur pembelian: " + e.getMessage());
        }
    }

    /**
     * Handler untuk tombol Batal.
     * Mengosongkan semua field input pada form.
     */
    @FXML
    public void handlerBatal() {
        resetForm();
    }

    /**
     * Handler untuk tombol Tutup.
     * Menutup window aplikasi.
     */
    @FXML
    public void handlerTutup() {
        Stage stage = (Stage) comboIDPembelian.getScene().getWindow();
        stage.close();
    }

    /**
     * Mengosongkan semua field input pada form.
     */
    private void resetForm() {
        comboIDPembelian.getSelectionModel().clearSelection();
        comboProduk.getItems().clear(); // Kosongkan juga item di comboProduk
        comboProduk.getSelectionModel().clearSelection();
        textJumlah.clear();
        textAlasan.clear();
        // Fokus kembali ke field pertama (opsional)
        comboIDPembelian.requestFocus();
    }

    /**
     * Menampilkan dialog Alert.
     * @param alertType Tipe alert (ERROR, INFORMATION, WARNING, dll.)
     * @param title Judul alert.
     * @param message Pesan yang ditampilkan dalam alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Tidak menggunakan header
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Metode yang dipanggil ketika subjek (ReturRepository) mengirim notifikasi.
     * @param o Objek yang diamati (ReturRepository).
     * @param arg Argumen yang dikirim bersama notifikasi (misalnya, pesan status).
     */
    @Override
    public void update(java.util.Observable o, Object arg) {
        System.out.println("Controller menerima notifikasi: " + arg);
        // Jika notifikasi menandakan ada data baru, muat ulang data
        // Pastikan pembaruan UI dilakukan di JavaFX Application Thread
        if ("RETUR_BARU_DISIMPAN".equals(arg) || "DATA_PEMBELIAN_BERUBAH".equals(arg)) {
            Platform.runLater(this::loadPembelianData);
        }
    }
}