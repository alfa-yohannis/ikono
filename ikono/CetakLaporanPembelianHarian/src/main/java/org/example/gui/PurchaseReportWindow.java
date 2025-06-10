// Lokasi: src/main/java/org/example/gui/PurchaseReportWindow.java
package org.example.gui;

import org.example.entity.PurchaseDetail;
import org.example.entity.PurchaseTransaction;
import org.example.util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class PurchaseReportWindow {

    public void display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); // Blokir window lain saat ini terbuka
        window.setTitle("Laporan Pembelian Harian Produk");

        // Tabel untuk Transaksi Pembelian
        TableView<PurchaseTransaction> transactionTable = new TableView<>();
        TableColumn<PurchaseTransaction, Integer> idCol = new TableColumn<>("ID Pembelian");
        idCol.setCellValueFactory(new PropertyValueFactory<>("PurchaseID"));
        
        TableColumn<PurchaseTransaction, LocalDate> dateCol = new TableColumn<>("Tanggal");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("PurchaseDate"));
        
        TableColumn<PurchaseTransaction, String> vendorCol = new TableColumn<>("Vendor");
        vendorCol.setCellValueFactory(new PropertyValueFactory<>("vendorName")); // Menggunakan helper
        
        TableColumn<PurchaseTransaction, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("TotalAmount"));
        
        TableColumn<PurchaseTransaction, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("PaymentStatus"));

        transactionTable.getColumns().addAll(idCol, dateCol, vendorCol, totalCol, statusCol);
        
        // INI BAGIAN PERTAMA YANG DIPERBAIKI
        transactionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Tabel untuk Detail Pembelian
        TableView<PurchaseDetail> detailTable = new TableView<>();
        TableColumn<PurchaseDetail, String> productCol = new TableColumn<>("Nama Produk");
        productCol.setCellValueFactory(new PropertyValueFactory<>("productName")); // Menggunakan helper
        
        TableColumn<PurchaseDetail, Integer> qtyCol = new TableColumn<>("Jumlah");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        
        TableColumn<PurchaseDetail, Double> priceCol = new TableColumn<>("Harga Satuan");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("UnitPrice"));
        
        TableColumn<PurchaseDetail, Double> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setCellValueFactory(new PropertyValueFactory<>("Subtotal"));

        detailTable.getColumns().addAll(productCol, qtyCol, priceCol, subtotalCol);
        
        // INI BAGIAN KEDUA YANG DIPERBAIKI
        detailTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        // Memuat data detail saat transaksi dipilih
        transactionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ObservableList<PurchaseDetail> details = FXCollections.observableArrayList(newSelection.getDetails());
                detailTable.setItems(details);
            }
        });

        // Memuat data transaksi
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<PurchaseTransaction> query = session.createQuery("FROM PurchaseTransaction", PurchaseTransaction.class);
            List<PurchaseTransaction> transactions = query.list();
            transactionTable.setItems(FXCollections.observableArrayList(transactions));
        } catch (Exception e) {
            e.printStackTrace();
        }

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                new Label("Daftar Transaksi Pembelian"),
                transactionTable,
                new Label("Detail Pembelian (Klik baris transaksi di atas untuk melihat)"),
                detailTable
        );

        Scene scene = new Scene(layout, 800, 600);
        window.setScene(scene);
        window.showAndWait(); // Tampilkan window dan tunggu hingga ditutup
    }
}
