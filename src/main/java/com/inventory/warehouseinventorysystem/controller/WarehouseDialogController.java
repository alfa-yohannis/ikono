package com.inventory.warehouseinventorysystem.controller;

import com.inventory.warehouseinventorysystem.model.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class WarehouseDialogController {

    @FXML private TextField nameField;
    @FXML private TextField locationField;
    @FXML private DialogPane dialogPane; // Untuk mengakses tombol OK

    private Warehouse warehouse; // Untuk menampung gudang yang diedit atau yang baru dibuat

    public void initialize() {
        // Bisa tambahkan validasi atau listener di sini jika perlu
    }

    // Metode ini akan dipanggil dari MainViewController untuk mengisi data jika mode edit
    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
        if (warehouse != null) {
            nameField.setText(warehouse.getName());
            locationField.setText(warehouse.getLocation());
        } else {
            // Jika mode tambah, pastikan field kosong
            nameField.clear();
            locationField.clear();
        }
    }

    // Metode ini dipanggil saat pengguna menekan tombol OK di dialog
    // Ini akan dipanggil dari MainViewController setelah dialog ditutup.
    public Warehouse processResults() {
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Warehouse name cannot be empty.");
            return null; // Mengindikasikan validasi gagal
        }

        // Jika ini adalah dialog untuk gudang baru (warehouse == null awalnya)
        if (this.warehouse == null) {
            this.warehouse = new Warehouse(name, location);
        } else { // Jika ini adalah dialog untuk edit gudang yang sudah ada
            this.warehouse.setName(name);
            this.warehouse.setLocation(location);
        }
        return this.warehouse;
    }

    // Metode untuk menampilkan pesan error sederhana dalam dialog
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Set owner agar dialog selalu di atas window utama
        Window window = dialogPane.getScene().getWindow();
        alert.initOwner(window);
        alert.showAndWait();
    }
}