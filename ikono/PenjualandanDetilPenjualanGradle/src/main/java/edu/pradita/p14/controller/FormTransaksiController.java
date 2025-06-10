package edu.pradita.p14.controller;

import edu.pradita.p14.model.dao.PembeliDao;
import edu.pradita.p14.model.dao.ProdukDao;
import edu.pradita.p14.model.dao.TransaksiDao;
import edu.pradita.p14.model.entitas.DetailTransaksi;
import edu.pradita.p14.model.entitas.Pembeli;
import edu.pradita.p14.model.entitas.Produk;
import edu.pradita.p14.model.entitas.Transaksi;
import edu.pradita.p14.model.util.HargaDiskonStrategy;
import edu.pradita.p14.model.util.HargaNormalStrategy;
import edu.pradita.p14.model.util.HargaStrategy;
import edu.pradita.p14.model.util.HibernateUtil;
import edu.pradita.p14.view.TransaksiItem;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FormTransaksiController implements Initializable {

    // --- Deklarasi Komponen FXML ---
    @FXML private Button btnBatal, btnCari, btnSimpan, btnTambah, btnTutup;
    @FXML private ListView<Produk> listItems;
    @FXML private TableView<TransaksiItem> tableBarang;
    @FXML private TableColumn<TransaksiItem, String> colKode, colNama;
    @FXML private TableColumn<TransaksiItem, Integer> colJumlah;
    @FXML private TableColumn<TransaksiItem, Double> colSubtotal;
    @FXML private TextField textAutoGenerateIdTransaksi, textBayar, textCariBarang, textGetHarga, textInputIDBuyer,
            textJumlahBarang1, textKembalian, textKodeBarang, textNamaBarang, textNamaBuyer, textTeleponBuyer,
            textTotalHarga, textTotalDisplay;

    // --- Injeksi Dependensi DAO ---
    private final ProdukDao produkDao = new ProdukDao();
    private final PembeliDao pembeliDao = new PembeliDao();
    private final TransaksiDao transaksiDao = new TransaksiDao();

    // List untuk menampung data di TableView
    private final ObservableList<TransaksiItem> daftarBelanja = FXCollections.observableArrayList();
    private final ObservableList<Produk> masterProdukList = FXCollections.observableArrayList();

    // --- PERBAIKAN: Menggunakan Locale.forLanguageTag untuk menghindari deprecated API ---
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Konfigurasi kolom tabel
        colKode.setCellValueFactory(new PropertyValueFactory<>("kode"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaBarang"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        colSubtotal.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });

        tableBarang.setItems(daftarBelanja);
        textInputIDBuyer.setOnKeyReleased(this::handleCariPembeli);
        muatDataProduk();

        generateNewTransactionId();
    }

    private void muatDataProduk() {
        List<Produk> produkList = produkDao.getAllProduk();
        if (produkList != null) {
            masterProdukList.setAll(produkList);
            listItems.setItems(masterProdukList);
            listItems.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Produk item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item.getNama() == null) {
                        setText(null);
                    } else {
                        setText(item.getNama());
                    }
                }
            });
        }
    }

    @FXML
    void handleListClick(MouseEvent event) {
        Produk produkTerpilih = listItems.getSelectionModel().getSelectedItem();
        if (produkTerpilih != null) {
            textKodeBarang.setText(produkTerpilih.getId());
            textNamaBarang.setText(produkTerpilih.getNama());
            textGetHarga.setText(String.valueOf(produkTerpilih.getHarga()));
        }
    }

    @FXML
    void handleTambahClick(ActionEvent event) {
        try {
            Produk produk = produkDao.getProdukById(textKodeBarang.getText());
            int jumlah = Integer.parseInt(textJumlahBarang1.getText());

            if (produk == null || jumlah <= 0) return;

            HargaStrategy strategy = (jumlah > 5) ? new HargaDiskonStrategy() : new HargaNormalStrategy();

            daftarBelanja.add(new TransaksiItem(produk, jumlah, strategy));

            perbaruiTotalHarga();
            bersihkanInputBarang();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Input jumlah tidak valid.");
        }
    }

    @FXML
    void handleSimpanClick(ActionEvent event) {
        Pembeli pembeli = pembeliDao.findById(textInputIDBuyer.getText());
        if (pembeli == null && !textInputIDBuyer.getText().isEmpty()) {
            pembeli = new Pembeli();
            pembeli.setId(textInputIDBuyer.getText());
            pembeli.setNama(textNamaBuyer.getText());
            pembeli.setTelepon(textTeleponBuyer.getText());
            pembeliDao.saveOrUpdate(pembeli);
        }

        Transaksi transaksi = new Transaksi();
        transaksi.setId(textAutoGenerateIdTransaksi.getText());
        transaksi.setTanggalWaktu(LocalDateTime.now());
        transaksi.setPembeli(pembeli);

        List<DetailTransaksi> detailList = new ArrayList<>();
        for (TransaksiItem item : daftarBelanja) {
            DetailTransaksi detail = new DetailTransaksi();
            detail.setTransaksi(transaksi);
            detail.setProduk(produkDao.getProdukById(item.getKode()));
            detail.setJumlah(item.getJumlah());
            detail.setHargaSaatTransaksi(item.getSubtotal() / item.getJumlah());
            detailList.add(detail);
        }
        transaksi.setDetailTransaksis(detailList);

        if (transaksiDao.simpanTransaksi(transaksi)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Transaksi berhasil disimpan!");
            handleBatalClick(null);
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Transaksi gagal disimpan.");
        }
    }

    @FXML
    void handleCariClick(ActionEvent event) {
        String keyword = textCariBarang.getText().toLowerCase();
        if (keyword.isEmpty()) {
            listItems.setItems(masterProdukList);
        } else {
            List<Produk> hasilFilter = masterProdukList.stream()
                    .filter(p -> p.getNama().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
            listItems.setItems(FXCollections.observableArrayList(hasilFilter));
        }
    }

    private void handleCariPembeli(KeyEvent event) {
        Pembeli pembeli = pembeliDao.findById(textInputIDBuyer.getText());
        if (pembeli != null) {
            textNamaBuyer.setText(pembeli.getNama());
            textTeleponBuyer.setText(pembeli.getTelepon());
            textNamaBuyer.setEditable(false);
            textTeleponBuyer.setEditable(false);
        } else {
            textNamaBuyer.clear();
            textTeleponBuyer.clear();
            textNamaBuyer.setEditable(true);
            textTeleponBuyer.setEditable(true);
        }
    }

    private void perbaruiTotalHarga() {
        double total = daftarBelanja.stream().mapToDouble(TransaksiItem::getSubtotal).sum();
        String formattedTotal = currencyFormat.format(total);
        textTotalHarga.setText(formattedTotal);
        textTotalDisplay.setText(formattedTotal);
    }

    @FXML
    void handleBatalClick(ActionEvent event) {
        daftarBelanja.clear();
        bersihkanInputBarang();
        textInputIDBuyer.clear();
        textNamaBuyer.clear();
        textTeleponBuyer.clear();
        textBayar.clear();
        textKembalian.clear();
        perbaruiTotalHarga();
        generateNewTransactionId();
    }

    private void bersihkanInputBarang() {
        textKodeBarang.clear();
        textNamaBarang.clear();
        textGetHarga.clear();
        textJumlahBarang1.clear();
    }

    @FXML
    void handleTutupClick(ActionEvent event) {
        HibernateUtil.shutdown();
        Stage stage = (Stage) btnTutup.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleBayarKeyReleased(KeyEvent event) {
        try {
            double total = daftarBelanja.stream().mapToDouble(TransaksiItem::getSubtotal).sum();
            double bayar = Double.parseDouble(textBayar.getText());
            double kembalian = bayar - total;

            textKembalian.setText(currencyFormat.format(kembalian));

        } catch (NumberFormatException e) {
            textKembalian.setText("Input tidak valid");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void generateNewTransactionId() {
        String newId = "TRX-" + System.currentTimeMillis();
        textAutoGenerateIdTransaksi.setText(newId);
    }
}
