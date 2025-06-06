package edu.pradita.p14.javafx.controller;

import edu.pradita.p14.javafx.ShippingMerek;
import edu.pradita.p14.javafx.dao.ShippingMerekDao;
import edu.pradita.p14.javafx.dao.ShippingMerekDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class PengirimanMerekController {

    // Komponen dari Tabel dan Pencarian
    @FXML private TableView<ShippingMerek> tableView;
    @FXML private TableColumn<ShippingMerek, Integer> idColumn;
    @FXML private TableColumn<ShippingMerek, String> nameColumn;
    @FXML private TableColumn<ShippingMerek, String> jenisColumn;
    @FXML private TableColumn<ShippingMerek, Double> hargaColumn;
    @FXML private TextField searchField;

    // PERBAIKAN: Tambahkan FXML untuk form input dan tombol
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField jenisField;
    @FXML private TextField hargaField;
    @FXML private Button addNewButton;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;

    private ShippingMerekDao shippingMerekDao;

    @FXML
    public void initialize() {
        shippingMerekDao = new ShippingMerekDaoImpl();

        // Mengatur kolom tabel
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        jenisColumn.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        hargaColumn.setCellValueFactory(new PropertyValueFactory<>("harga"));

        // PERBAIKAN: Buat field ID tidak bisa diedit dan tambahkan listener
        idField.setEditable(false);
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showMerekDetails(newValue));

        // Memuat data awal
        loadData();
    }

    /**
     * Menampilkan detail dari baris yang dipilih ke dalam form.
     */
    private void showMerekDetails(ShippingMerek merek) {
        if (merek != null) {
            idField.setText(String.valueOf(merek.getId()));
            nameField.setText(merek.getName());
            jenisField.setText(merek.getJenis());
            hargaField.setText(String.valueOf(merek.getHarga()));
        } else {
            idField.setText("");
            nameField.setText("");
            jenisField.setText("");
            hargaField.setText("");
        }
    }

    // PERBAIKAN: Implementasi logika untuk Tambah, Simpan, Hapus

    @FXML
    public void handleAddNew(ActionEvent actionEvent) {
        tableView.getSelectionModel().clearSelection();
        showMerekDetails(null); // Kosongkan form
    }

    @FXML
    public void handleSave(ActionEvent actionEvent) {
        if (nameField.getText().isEmpty() || jenisField.getText().isEmpty() || hargaField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Semua kolom wajib diisi.");
            return;
        }

        try {
            double harga = Double.parseDouble(hargaField.getText());
            ShippingMerek merek;

            if (idField.getText().isEmpty()) { // Jika ID kosong, berarti data baru
                merek = new ShippingMerek(nameField.getText(), harga, jenisField.getText());
                shippingMerekDao.save(merek);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data baru berhasil disimpan.");
            } else { // Jika ID ada, berarti edit data
                merek = tableView.getSelectionModel().getSelectedItem();
                merek.setName(nameField.getText());
                merek.setJenis(jenisField.getText());
                merek.setHarga(harga);
                shippingMerekDao.update(merek);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil diperbarui.");
            }
            loadData();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Harga harus berupa angka.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error!", "Gagal menyimpan data: " + e.getMessage());
        }
    }

    @FXML
    public void handleDelete(ActionEvent actionEvent) {
        ShippingMerek selectedMerek = tableView.getSelectionModel().getSelectedItem();
        if (selectedMerek != null) {
            try {
                shippingMerekDao.delete(selectedMerek.getId());
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data telah dihapus.");
                loadData();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Database Error!", "Gagal menghapus data: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Pilihan", "Pilih data yang akan dihapus.");
        }
    }

    private void loadData() {
        try {
            List<ShippingMerek> merekList = shippingMerekDao.findAll();
            tableView.setItems(FXCollections.observableArrayList(merekList));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Data Error", "Gagal memuat data dari database: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        try {
            String keyword = searchField.getText();
            if (keyword == null || keyword.trim().isEmpty()) {
                loadData();
            } else {
                List<ShippingMerek> resultList = shippingMerekDao.findByName(keyword);
                tableView.setItems(FXCollections.observableArrayList(resultList));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Search Error", "Gagal melakukan pencarian: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        loadData();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}