package com.inventory.warehouseinventorysystem.controller;

import com.inventory.warehouseinventorysystem.model.Product;
import com.inventory.warehouseinventorysystem.model.Warehouse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter; // Untuk validasi input numerik
import javafx.stage.Window;
import javafx.util.StringConverter; // Untuk tampilan ComboBox

import java.math.BigDecimal;
import java.util.List;
import java.util.function.UnaryOperator; // Untuk filter TextFormatter

public class ProductDialogController {

    @FXML private TextField nameField;
    @FXML private TextField skuField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private ComboBox<Warehouse> warehouseComboBox;
    @FXML private DialogPane dialogPane;

    private Product product; // Untuk menampung produk yang diedit atau yang baru dibuat
    private ObservableList<Warehouse> warehouseList = FXCollections.observableArrayList();

    public void initialize() {
        // Konfigurasi ComboBox agar menampilkan nama gudang
        warehouseComboBox.setItems(warehouseList);
        warehouseComboBox.setConverter(new StringConverter<Warehouse>() {
            @Override
            public String toString(Warehouse warehouse) {
                return warehouse == null ? null : warehouse.toString(); // Menggunakan override toString() dari Warehouse
            }

            @Override
            public Warehouse fromString(String string) {
                // Tidak perlu implementasi karena kita tidak mengedit ComboBox secara langsung
                return warehouseList.stream().filter(wh -> wh.toString().equals(string)).findFirst().orElse(null);
            }
        });

        // Validasi sederhana untuk input numerik
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) { // Hanya izinkan digit
                return change;
            }
            return null;
        };
        quantityField.setTextFormatter(new TextFormatter<>(integerFilter));

        UnaryOperator<TextFormatter.Change> decimalFilter = change -> {
            String newText = change.getControlNewText();
            // Izinkan angka, satu titik desimal, dan hingga 2 angka di belakang koma
            if (newText.matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        };
        priceField.setTextFormatter(new TextFormatter<>(decimalFilter));
    }

    // Dipanggil dari MainViewController untuk mengisi daftar gudang di ComboBox
    public void setWarehouses(List<Warehouse> warehouses) {
        warehouseList.setAll(warehouses);
    }

    // Dipanggil dari MainViewController untuk mengisi data jika mode edit
    // atau untuk pre-select warehouse jika mode tambah
    public void setProductAndSelectedWarehouse(Product product, Warehouse selectedWarehouse) {
        this.product = product;
        if (product != null) { // Mode Edit
            nameField.setText(product.getName());
            skuField.setText(product.getSku());
            quantityField.setText(String.valueOf(product.getQuantity()));
            priceField.setText(product.getPrice().toPlainString());
            if (product.getWarehouse() != null) {
                warehouseComboBox.setValue(product.getWarehouse());
            } else if (selectedWarehouse != null) {
                // Jika produk tidak punya gudang (seharusnya tidak terjadi jika logic benar)
                // tapi ada gudang terpilih dari main view, gunakan itu.
                warehouseComboBox.setValue(selectedWarehouse);
            }
        } else { // Mode Tambah
            nameField.clear();
            skuField.clear();
            quantityField.clear();
            priceField.clear();
            if (selectedWarehouse != null) {
                warehouseComboBox.setValue(selectedWarehouse); // Pre-select gudang
            } else if (!warehouseList.isEmpty()){
                warehouseComboBox.getSelectionModel().selectFirst(); // Pilih gudang pertama jika ada
            }
        }
    }

    // Dipanggil dari MainViewController setelah dialog ditutup
    public Product processResults() {
        String name = nameField.getText().trim();
        String sku = skuField.getText().trim();
        String quantityStr = quantityField.getText().trim();
        String priceStr = priceField.getText().trim();
        Warehouse selectedWarehouse = warehouseComboBox.getSelectionModel().getSelectedItem();

        if (name.isEmpty() || sku.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty() || selectedWarehouse == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields including warehouse must be filled.");
            return null;
        }

        int quantity;
        BigDecimal price;

        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity < 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Quantity cannot be negative.");
                return null;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid quantity format. Please enter a number.");
            return null;
        }

        try {
            price = new BigDecimal(priceStr);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Price cannot be negative.");
                return null;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid price format. Please enter a valid decimal number (e.g., 123.45).");
            return null;
        }

        if (this.product == null) { // Mode Tambah
            this.product = new Product(name, sku, quantity, price, selectedWarehouse);
        } else { // Mode Edit
            this.product.setName(name);
            this.product.setSku(sku);
            this.product.setQuantity(quantity);
            this.product.setPrice(price);
            this.product.setWarehouse(selectedWarehouse);
        }
        return this.product;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Window window = dialogPane.getScene().getWindow();
        alert.initOwner(window);
        alert.showAndWait();
    }
}