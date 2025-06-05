package com.pelunasan.ui;

import com.pelunasan.model.Hutang;
import com.pelunasan.service.HutangService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PelunasanHutangView extends Application {

    private final HutangService service = new HutangService();
    private final ObservableList<Hutang> daftarHutang = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        TextField namaField = new TextField();
        namaField.setPromptText("Nama");

        TextField jumlahField = new TextField();
        jumlahField.setPromptText("Jumlah");

        Button tambahBtn = new Button("Tambah Hutang");
        tambahBtn.setOnAction(e -> {
            try {
                String nama = namaField.getText();
                double jumlah = Double.parseDouble(jumlahField.getText());
                service.tambahHutang(nama, jumlah);
                perbaruiTabel();
                namaField.clear();
                jumlahField.clear();
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        TableView<Hutang> tableView = new TableView<>(daftarHutang);
        TableColumn<Hutang, String> namaCol = new TableColumn<>("Nama");
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<Hutang, Double> jumlahCol = new TableColumn<>("Jumlah");
        jumlahCol.setCellValueFactory(new PropertyValueFactory<>("jumlah"));

        TableColumn<Hutang, Boolean> lunasCol = new TableColumn<>("Lunas");
        lunasCol.setCellValueFactory(new PropertyValueFactory<>("lunas"));

        tableView.getColumns().addAll(namaCol, jumlahCol, lunasCol);

        Button lunasBtn = new Button("Tandai Lunas");
        lunasBtn.setOnAction(e -> {
            Hutang selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                service.tandaiHutangLunas(selected.getId());
                perbaruiTabel();
            }
        });

        Button hapusBtn = new Button("Hapus");
        hapusBtn.setOnAction(e -> {
            Hutang selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                service.hapusHutang(selected.getId());
                perbaruiTabel();
            }
        });

        HBox inputBox = new HBox(10, namaField, jumlahField, tambahBtn);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER);

        HBox controlBox = new HBox(10, lunasBtn, hapusBtn);
        controlBox.setPadding(new Insets(10));
        controlBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, inputBox, tableView, controlBox);
        root.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setTitle("Aplikasi Pelunasan Hutang");
        primaryStage.show();

        perbaruiTabel();
    }

    private void perbaruiTabel() {
        daftarHutang.setAll(service.ambilSemuaHutang());
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        service.tutup();
    }

    public static void main(String[] args) {
        launch();
    }
}
