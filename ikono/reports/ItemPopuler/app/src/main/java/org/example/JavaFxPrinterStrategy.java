package org.example;

import javafx.collections.ObservableList;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

public class JavaFxPrinterStrategy<T> implements PrintStrategy<T> {
    private final AlertHandler alertHandler;

    public JavaFxPrinterStrategy(AlertHandler alertHandler) {
        this.alertHandler = alertHandler;
    }

    @Override
    public void print(TableView<T> table, ObservableList<ItemReport> data) { // data mungkin tidak digunakan di sini
        if (table == null || table.getScene() == null || table.getScene().getWindow() == null) {
            alertHandler.showAlert(Alert.AlertType.ERROR, "Print Error", "Tidak dapat mencetak: TableView tidak siap.");
            return;
        }
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(table.getScene().getWindow())) {
            boolean success = printerJob.printPage(table);
            if (success) {
                printerJob.endJob();
                alertHandler.showAlert(Alert.AlertType.INFORMATION, "Print Success", "Laporan berhasil dicetak.");
            } else {
                alertHandler.showAlert(Alert.AlertType.ERROR, "Print Failed", "Gagal mencetak laporan.");
            }
        } else {
            alertHandler.showAlert(Alert.AlertType.WARNING, "Print Cancelled", "Pencetakan dibatalkan.");
        }
    }
}