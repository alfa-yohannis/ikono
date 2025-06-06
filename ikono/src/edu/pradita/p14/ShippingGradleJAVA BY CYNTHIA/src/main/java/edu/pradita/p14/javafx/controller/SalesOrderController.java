package edu.pradita.p14.javafx.controller;

// Semua import Anda akan ada di sini
// Contoh:
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;


// Pastikan ada kurung kurawal pembuka di sini
public class SalesOrderController {

    // Deklarasi semua variabel dan komponen FXML Anda ada di sini
    @FXML
    private TableView salesOrderTable;

    @FXML
    private Button saveButton;


    // Semua metode (method) Anda ada di sini
    public void initialize() {
        // Kode untuk inisialisasi
    }

    @FXML
    private void handleSaveAction() {
        // Kode untuk tombol simpan
        System.out.println("Tombol Simpan Diklik!");
    }

    // Metode lainnya...


} // <--- PERIKSA INI! Kemungkinan besar kurung kurawal penutup ini yang hilang.