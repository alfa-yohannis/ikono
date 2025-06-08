package org.example.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.SalesReturn;
import org.example.patterns.decorator.BasicReturnProcess;
import org.example.patterns.decorator.NotificationReturnDecorator;
import org.example.patterns.decorator.PriorityReturnDecorator;
import org.example.patterns.decorator.ReturnProcess;
import org.example.patterns.observer.Observer; // Observer dari paket pola desain
import org.example.service.SalesReturnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SalesReturnForm implements Observer { // Implement Observer untuk update UI
    private static final Logger logger = LoggerFactory.getLogger(SalesReturnForm.class);

    private final SalesReturnService salesReturnService;

    // Komponen UI
    private TextField rmaField, receiptField, customerField, itemIdField, descriptionField, quantityField;
    private ComboBox<String> reasonComboBox, refundComboBox;
    private TextArea commentsArea;
    private TableView<SalesReturn> salesReturnTableView; // Menggunakan TableView untuk tampilan yang lebih baik
    private Label statusLabel;
    private Button submitButton, updateButton, deleteButton, clearButton;
    private SalesReturn selectedSalesReturn = null;

    public SalesReturnForm(SalesReturnService salesReturnService) {
        this.salesReturnService = salesReturnService;
        this.salesReturnService.registerObserver(this); // Daftarkan UI sebagai observer
        logger.info("SalesReturnForm UI diinisialisasi dan terdaftar sebagai observer.");
    }

    public void display(Stage primaryStage) {
        primaryStage.setTitle("Form Retur Penjualan (SOLID & Patterns)");

        GridPane formGrid = createFormGrid(); // Memisahkan pembuatan grid form

        // Tombol-tombol
        submitButton = new Button("Simpan Retur Baru");
        submitButton.setOnAction(e -> handleSubmit());

        updateButton = new Button("Perbarui Retur Terpilih");
        updateButton.setOnAction(e -> handleUpdate());
        updateButton.setDisable(true);

        deleteButton = new Button("Hapus Retur Terpilih");
        deleteButton.setOnAction(e -> handleDelete());
        deleteButton.setDisable(true);

        clearButton = new Button("Bersihkan Form");
        clearButton.setOnAction(e -> clearFieldsAndSelection());

        HBox buttonPane = new HBox(10, submitButton, updateButton, deleteButton, clearButton);
        buttonPane.setPadding(new Insets(10,0,0,0)); // Padding di atas tombol

        // Status Label
        statusLabel = new Label("Status: Siap");

        // TableView untuk menampilkan data retur
        salesReturnTableView = createSalesReturnTableView();
        loadSalesReturnsData(); // Memuat data awal

        // Listener untuk TableView
        salesReturnTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedSalesReturn = newVal; // newVal bisa null jika tidak ada yang dipilih
            if (selectedSalesReturn != null) {
                populateForm(selectedSalesReturn);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                submitButton.setDisable(true);
                statusLabel.setText("Status: Retur ID " + selectedSalesReturn.getId() + " dipilih.");
            } else {
                clearFieldsAndSelection(); // Otomatis men-disable tombol update/delete dan enable submit
            }
        });

        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(
            new Label("Form Input Data Retur:"), formGrid,
            buttonPane,
            new Separator(),
            new Label("Daftar Retur Penjualan:"), salesReturnTableView,
            statusLabel
        );
        VBox.setVgrow(salesReturnTableView, Priority.ALWAYS); // Agar TableView bisa expand

        Scene scene = new Scene(mainLayout, 900, 750);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            logger.info("Window ditutup, menghapus UI dari observer list service.");
            salesReturnService.removeObserver(this); // Hapus observer saat window ditutup
        });
        primaryStage.show();
        logger.info("UI SalesReturnForm berhasil ditampilkan.");
    }

    private GridPane createFormGrid() {
        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);

        // Baris 0: RMA Number
        grid.add(new Label("Nomor RMA:"), 0, 0);
        rmaField = new TextField();
        rmaField.setPromptText("cth: RMA2023001");
        grid.add(rmaField, 1, 0);

        // Baris 1: Receipt Number
        grid.add(new Label("Nomor Nota:"), 0, 1);
        receiptField = new TextField();
        receiptField.setPromptText("cth: NOTA/07/23/00123");
        grid.add(receiptField, 1, 1);

        // Baris 2: Customer Name
        grid.add(new Label("Nama Pelanggan:"), 0, 2);
        customerField = new TextField();
        customerField.setPromptText("Nama Lengkap Pelanggan");
        grid.add(customerField, 1, 2);

        // Baris 3: Item ID
        grid.add(new Label("ID Barang:"), 0, 3);
        itemIdField = new TextField();
        itemIdField.setPromptText("cth: BRG001-XL-RED");
        grid.add(itemIdField, 1, 3);

        // Baris 4: Description
        grid.add(new Label("Deskripsi Barang:"), 0, 4);
        descriptionField = new TextField();
        descriptionField.setPromptText("Deskripsi singkat barang yang diretur");
        grid.add(descriptionField, 1, 4);

        // Baris 5: Quantity
        grid.add(new Label("Kuantitas:"), 0, 5);
        quantityField = new TextField();
        quantityField.setPromptText("Jumlah barang");
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantityField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        grid.add(quantityField, 1, 5);

        // Baris 6: Return Reason
        grid.add(new Label("Alasan Retur:"), 0, 6);
        reasonComboBox = new ComboBox<>(FXCollections.observableArrayList(
            "Barang Rusak", "Salah Kirim Barang", "Tidak Sesuai Deskripsi", "Berubah Pikiran", "Ukuran Tidak Cocok", "Lainnya"));
        reasonComboBox.setPromptText("Pilih alasan");
        reasonComboBox.setMaxWidth(Double.MAX_VALUE);
        grid.add(reasonComboBox, 1, 6);

        // Baris 7: Refund Method
        grid.add(new Label("Metode Refund:"), 0, 7);
        refundComboBox = new ComboBox<>(FXCollections.observableArrayList(
            "Metode Pembayaran Asli", "Kredit Toko", "Tukar Barang", "Voucher Belanja"));
        refundComboBox.setPromptText("Pilih metode refund");
        refundComboBox.setMaxWidth(Double.MAX_VALUE);
        grid.add(refundComboBox, 1, 7);

        // Baris 8: Comments
        grid.add(new Label("Komentar Tambahan:"), 0, 8);
        commentsArea = new TextArea();
        commentsArea.setPromptText("Detail tambahan jika ada (misal: kerusakan spesifik, permintaan khusus)");
        commentsArea.setPrefRowCount(3);
        commentsArea.setWrapText(true);
        grid.add(commentsArea, 1, 8);
        GridPane.setVgrow(commentsArea, Priority.ALWAYS);

        return grid;
    }

    private TableView<SalesReturn> createSalesReturnTableView() {
        TableView<SalesReturn> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Kolom mengisi lebar tabel

        TableColumn<SalesReturn, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setMinWidth(40); idCol.setMaxWidth(60);


        TableColumn<SalesReturn, String> rmaCol = new TableColumn<>("No. RMA");
        rmaCol.setCellValueFactory(new PropertyValueFactory<>("rmaNumber"));
        rmaCol.setMinWidth(100);

        TableColumn<SalesReturn, String> customerCol = new TableColumn<>("Pelanggan");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerCol.setMinWidth(150);

        TableColumn<SalesReturn, String> itemCol = new TableColumn<>("ID Barang");
        itemCol.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        itemCol.setMinWidth(100);
        
        TableColumn<SalesReturn, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        qtyCol.setMinWidth(40); qtyCol.setMaxWidth(60);

        TableColumn<SalesReturn, String> reasonCol = new TableColumn<>("Alasan");
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("returnReason"));
        reasonCol.setMinWidth(150);
        
        tableView.getColumns().addAll(idCol, rmaCol, customerCol, itemCol, qtyCol, reasonCol);
        return tableView;
    }


    private void handleSubmit() {
        if (!validateInput()) return;
        logger.debug("Memulai proses submit retur baru.");

        try {
            SalesReturn salesReturn = new SalesReturn(
                    rmaField.getText().trim(),
                    receiptField.getText().trim(),
                    customerField.getText().trim(),
                    itemIdField.getText().trim(),
                    descriptionField.getText().trim(),
                    Integer.parseInt(quantityField.getText()),
                    reasonComboBox.getValue(),
                    refundComboBox.getValue(),
                    commentsArea.getText().trim()
            );

            // Menggunakan Decorator Pattern
            ReturnProcess process = new BasicReturnProcess();
            // Contoh: Jika ini adalah retur prioritas, tambahkan decorator
            if (customerField.getText().toLowerCase().contains("vip") ||
                (commentsArea.getText() != null && commentsArea.getText().toLowerCase().contains("prioritas"))) {
                process = new PriorityReturnDecorator(process);
                logger.info("Retur ditandai sebagai prioritas, menggunakan PriorityReturnDecorator.");
            }
            process = new NotificationReturnDecorator(process); // Selalu tambahkan notifikasi

            logger.info("Deskripsi Proses Retur yang akan dijalankan: {}", process.getDescription());
            process.handleReturn(salesReturn); // Menjalankan proses yang sudah di-decorate

            SalesReturn createdReturn = salesReturnService.createReturn(salesReturn);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data retur penjualan berhasil disimpan dengan ID: " + createdReturn.getId());
            clearFieldsAndSelection();
            // Data akan di-load ulang oleh observer
        } catch (NumberFormatException e) {
            logger.error("Error input kuantitas: {}", e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error Input", "Kuantitas harus berupa angka yang valid.");
        } catch (IllegalArgumentException e) {
            logger.error("Error validasi data: {}", e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error Validasi", e.getMessage());
        } catch (Exception e) {
            logger.error("Gagal menyimpan data retur: {}", e.getMessage(), e);
            showAlert(Alert.AlertType.ERROR, "Error Sistem", "Gagal menyimpan data: " + e.getMessage());
        }
    }

     private void handleUpdate() {
        if (selectedSalesReturn == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih data retur yang akan diperbarui dari daftar.");
            return;
        }
        if (!validateInput()) return;
        logger.debug("Memulai proses update untuk retur ID: {}", selectedSalesReturn.getId());

        try {
            selectedSalesReturn.setRmaNumber(rmaField.getText().trim());
            selectedSalesReturn.setReceiptNumber(receiptField.getText().trim());
            selectedSalesReturn.setCustomerName(customerField.getText().trim());
            selectedSalesReturn.setItemId(itemIdField.getText().trim());
            selectedSalesReturn.setDescription(descriptionField.getText().trim());
            selectedSalesReturn.setQuantity(Integer.parseInt(quantityField.getText()));
            selectedSalesReturn.setReturnReason(reasonComboBox.getValue());
            selectedSalesReturn.setRefundMethod(refundComboBox.getValue());
            selectedSalesReturn.setComments(commentsArea.getText().trim());

            salesReturnService.updateReturn(selectedSalesReturn);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data retur ID " + selectedSalesReturn.getId() + " berhasil diperbarui.");
            clearFieldsAndSelection();
        } catch (NumberFormatException e) {
            logger.error("Error input kuantitas saat update: {}", e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error Input", "Kuantitas harus berupa angka.");
        } catch (IllegalArgumentException e) {
            logger.error("Error validasi data saat update: {}", e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error Validasi", e.getMessage());
        } catch (Exception e) {
            logger.error("Gagal memperbarui data retur ID {}: {}", selectedSalesReturn.getId(), e.getMessage(), e);
            showAlert(Alert.AlertType.ERROR, "Error Sistem", "Gagal memperbarui data: " + e.getMessage());
        }
    }

    private void handleDelete() {
        if (selectedSalesReturn == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih data retur yang akan dihapus dari daftar.");
            return;
        }
        logger.debug("Memulai proses hapus untuk retur ID: {}", selectedSalesReturn.getId());

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Apakah Anda yakin ingin menghapus retur dengan RMA: " + selectedSalesReturn.getRmaNumber() + " (ID: " + selectedSalesReturn.getId() + ")?",
                ButtonType.YES, ButtonType.NO);
        confirmationDialog.setTitle("Konfirmasi Hapus");
        confirmationDialog.setHeaderText(null); // Tidak perlu header

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                salesReturnService.deleteReturnById(selectedSalesReturn.getId());
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data retur ID " + selectedSalesReturn.getId() + " berhasil dihapus.");
                clearFieldsAndSelection();
            } catch (Exception e) {
                logger.error("Gagal menghapus data retur ID {}: {}", selectedSalesReturn.getId(), e.getMessage(), e);
                showAlert(Alert.AlertType.ERROR, "Error Sistem", "Gagal menghapus data: " + e.getMessage());
            }
        } else {
            logger.debug("Penghapusan retur ID {} dibatalkan oleh pengguna.", selectedSalesReturn.getId());
        }
    }


    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();
        if (rmaField.getText() == null || rmaField.getText().trim().isEmpty()) errors.append("- Nomor RMA tidak boleh kosong.\n");
        if (receiptField.getText() == null || receiptField.getText().trim().isEmpty()) errors.append("- Nomor Nota tidak boleh kosong.\n");
        if (customerField.getText() == null || customerField.getText().trim().isEmpty()) errors.append("- Nama Pelanggan tidak boleh kosong.\n");
        if (itemIdField.getText() == null || itemIdField.getText().trim().isEmpty()) errors.append("- ID Barang tidak boleh kosong.\n");
        if (descriptionField.getText() == null || descriptionField.getText().trim().isEmpty()) errors.append("- Deskripsi Barang tidak boleh kosong.\n");
        if (quantityField.getText() == null || quantityField.getText().trim().isEmpty()) {
            errors.append("- Kuantitas tidak boleh kosong.\n");
        } else {
            try {
                int qty = Integer.parseInt(quantityField.getText());
                if (qty <= 0) errors.append("- Kuantitas harus lebih besar dari 0.\n");
            } catch (NumberFormatException e) {
                errors.append("- Kuantitas harus berupa angka yang valid.\n");
            }
        }
        if (reasonComboBox.getValue() == null) errors.append("- Alasan Retur harus dipilih.\n");
        if (refundComboBox.getValue() == null) errors.append("- Metode Refund harus dipilih.\n");

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", errors.toString());
            return false;
        }
        return true;
    }

    private void populateForm(SalesReturn sr) {
        rmaField.setText(sr.getRmaNumber());
        receiptField.setText(sr.getReceiptNumber());
        customerField.setText(sr.getCustomerName());
        itemIdField.setText(sr.getItemId());
        descriptionField.setText(sr.getDescription());
        quantityField.setText(String.valueOf(sr.getQuantity()));
        reasonComboBox.setValue(sr.getReturnReason());
        refundComboBox.setValue(sr.getRefundMethod());
        commentsArea.setText(sr.getComments() != null ? sr.getComments() : "");
        logger.trace("Form diisi dengan data dari retur ID: {}", sr.getId());
    }

    private void clearFieldsAndSelection() {
        rmaField.clear();
        receiptField.clear();
        customerField.clear();
        itemIdField.clear();
        descriptionField.clear();
        quantityField.clear();
        reasonComboBox.getSelectionModel().clearSelection();
        reasonComboBox.setPromptText("Pilih alasan");
        refundComboBox.getSelectionModel().clearSelection();
        refundComboBox.setPromptText("Pilih metode refund");
        commentsArea.clear();

        selectedSalesReturn = null;
        salesReturnTableView.getSelectionModel().clearSelection();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        submitButton.setDisable(false);
        statusLabel.setText("Status: Siap (Form dibersihkan).");
        logger.trace("Form dibersihkan dan seleksi direset.");
    }

    private void loadSalesReturnsData() {
        logger.debug("Memulai pemuatan data retur ke TableView.");
        try {
            List<SalesReturn> returns = salesReturnService.getAllReturns();
            ObservableList<SalesReturn> observableReturns = FXCollections.observableArrayList(returns);
            // Pastikan update UI dilakukan di thread JavaFX
            Platform.runLater(() -> {
                 salesReturnTableView.setItems(observableReturns);
                 statusLabel.setText("Status: Data berhasil dimuat. Jumlah retur: " + returns.size());
                 logger.info("Data retur berhasil dimuat ke TableView, jumlah: {}", returns.size());
            });
        } catch (Exception e) {
            logger.error("Gagal memuat data retur ke TableView: {}", e.getMessage(), e);
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal memuat data retur: " + e.getMessage());
                statusLabel.setText("Status: Gagal memuat data.");
            });
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        // Pastikan alert ditampilkan di JavaFX Application Thread
        if (Platform.isFxApplicationThread()) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(alertType);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            });
        }
    }

    // Implementasi metode update dari Observer
    @Override
    public void update(SalesReturn salesReturn, String action) {
        logger.info("[UI Observer] Menerima notifikasi dari service: Aksi '{}' pada retur ID '{}'. Memuat ulang data.",
            action, salesReturn != null ? salesReturn.getId() : "N/A");
        // Ketika service memberitahu ada perubahan, muat ulang data di TableView
        Platform.runLater(() -> {
            loadSalesReturnsData();
            String message = "Notifikasi Sistem: ";
            if (salesReturn != null) {
                 message += "Retur RMA '" + salesReturn.getRmaNumber() + "' telah di-" + action.toLowerCase() + ".";
            } else {
                message += "Sebuah aksi ("+ action.toLowerCase() +") telah terjadi pada data.";
            }
            statusLabel.setText(message);
            // Pertimbangkan untuk tidak menampilkan alert untuk setiap update agar tidak mengganggu pengguna
            // showAlert(Alert.AlertType.INFORMATION, "Notifikasi Sistem", message);
        });
    }
}
