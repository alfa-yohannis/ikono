package org.example;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert; // Diperlukan jika ada penanganan error default
import javafx.scene.control.TableView;

public class ReportPrinter {
    private PrintStrategy<ItemReport> printStrategy;
    private final AlertHandler alertHandler; // Untuk pesan error jika strategi tidak diset

    // Konstruktor menerima strategi awal dan AlertHandler
    public ReportPrinter(PrintStrategy<ItemReport> initialStrategy, AlertHandler alertHandler) {
        this.printStrategy = initialStrategy;
        this.alertHandler = alertHandler;
    }

    // Metode untuk mengubah strategi pencetakan saat runtime
    public void setPrintStrategy(PrintStrategy<ItemReport> printStrategy) {
        this.printStrategy = printStrategy;
    }

    // Metode print sekarang mendelegasikan ke strategi yang aktif
    // Membutuhkan TableView (untuk strategi UI) dan data (untuk strategi non-UI)
    public void print(TableView<ItemReport> tableView, ObservableList<ItemReport> reportData) {
        if (this.printStrategy == null) {
            if (this.alertHandler != null) {
                this.alertHandler.showAlert(Alert.AlertType.ERROR, "Print Error", "Strategi pencetakan belum diatur.");
            } else {
                System.err.println("Error: Strategi pencetakan belum diatur dan AlertHandler tidak tersedia.");
            }
            return;
        }
        // Delegasikan ke strategi yang sedang aktif
        this.printStrategy.print(tableView, reportData);
    }
}