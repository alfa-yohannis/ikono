package uas.transaksi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;

import uas.transaksi.dao.*; // Import interface DAO (tipe datanya masih dipakai)
import uas.transaksi.data.*;
import uas.transaksi.service.MasterDataService;
import uas.transaksi.service.PengirimanService;
import uas.transaksi.event.DataChangePublisher; // Import DataChangePublisher

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TransaksiController {

    @FXML private TabPane mainTabPane;

    // Service Layer Instances (akan di-inject dari Transaksi.java)
    private MasterDataService masterDataService;
    private PengirimanService pengirimanService;

    private DataChangePublisher dataChangePublisher; // Deklarasi Publisher

    // Controller untuk tab-tab terpisah
    private LihatTransaksiController lihatTransaksiController;
    private TambahTransaksiController tambahTransaksiController;


    public TransaksiController() {
        // Konstruktor kosong
    }

    // Metode untuk meng-inject Service Layer dari Transaksi.java
    public void setServices(MasterDataService masterDataService, PengirimanService pengirimanService) {
        this.masterDataService = masterDataService;
        this.pengirimanService = pengirimanService;

        // Setelah Service Layer tersedia, inject ke controller tab
        if (lihatTransaksiController != null) {
            lihatTransaksiController.setPengirimanService(pengirimanService);
        }
        if (tambahTransaksiController != null) {
            tambahTransaksiController.setServices(masterDataService, pengirimanService);
            tambahTransaksiController.setMainTabPane(mainTabPane);
        }
    }

    // Metode untuk meng-inject DataChangePublisher dari Transaksi.java
    public void setDataChangePublisher(DataChangePublisher dataChangePublisher) {
        this.dataChangePublisher = dataChangePublisher;

        // Setelah publisher tersedia, inject ke controller tab yang relevan
        if (lihatTransaksiController != null) {
            lihatTransaksiController.setDataChangePublisher(dataChangePublisher);
        }
        if (tambahTransaksiController != null) {
            tambahTransaksiController.setDataChangePublisher(dataChangePublisher);
        }
    }

    @FXML
    public void initialize() {
        loadTabContent(); // Memuat FXML untuk setiap tab dan mendapatkan controllernya
    }

    private void loadTabContent() {
        try {
            // Memuat konten untuk tab "Lihat Data Transaksi"
            FXMLLoader lihatLoader = new FXMLLoader(getClass().getResource("/uas/transaksi/LihatTransaksiView.fxml"));
            VBox lihatContent = lihatLoader.load();
            lihatTransaksiController = lihatLoader.getController(); // Dapatkan instance controller tab
            mainTabPane.getTabs().get(0).setContent(lihatContent);

            // Memuat konten untuk tab "Tambah Transaksi"
            FXMLLoader tambahLoader = new FXMLLoader(getClass().getResource("/uas/transaksi/TambahTransaksiView.fxml"));
            VBox tambahContent = tambahLoader.load();
            tambahTransaksiController = tambahLoader.getController(); // Dapatkan instance controller tab
            mainTabPane.getTabs().get(1).setContent(tambahContent);

        } catch (IOException e) {
            System.err.println("Error loading tab content: " + e.getMessage());
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Gagal memuat tampilan tab: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }
}