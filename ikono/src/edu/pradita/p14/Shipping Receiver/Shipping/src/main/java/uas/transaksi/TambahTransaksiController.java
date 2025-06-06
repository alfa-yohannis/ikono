package uas.transaksi;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import uas.transaksi.data.*;
import uas.transaksi.service.MasterDataService;
import uas.transaksi.service.PengirimanService;
import uas.transaksi.event.DataChangePublisher;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class TambahTransaksiController {

    @FXML private TextField txtShipmentCode;
    @FXML private ComboBox<SenderData> cbPengirim;
    @FXML private ComboBox<ReceiverData> cbPenerima;
    @FXML private ComboBox<ShippingMerekData> cbMerekPengiriman;
    @FXML private DatePicker dpTanggalPengiriman;
    @FXML private ComboBox<String> cbStatus;
    @FXML private Label lblTambahTransaksiStatus;

    // Service Layer Instances
    private MasterDataService masterDataService;
    private PengirimanService pengirimanService;

    private TabPane mainTabPane;
    private DataChangePublisher dataChangePublisher;

    public TambahTransaksiController() {}

    public void setServices(MasterDataService masterDataService, PengirimanService pengirimanService) {
        this.masterDataService = masterDataService;
        this.pengirimanService = pengirimanService;
        populateComboBoxes();
    }

    public void setMainTabPane(TabPane mainTabPane) {
        this.mainTabPane = mainTabPane;
    }

    public void setDataChangePublisher(DataChangePublisher dataChangePublisher) {
        this.dataChangePublisher = dataChangePublisher;
    }

    @FXML
    public void initialize() {
        dpTanggalPengiriman.setValue(LocalDate.now());
    }

    private void populateComboBoxes() {
        try {
            cbPengirim.getItems().clear();
            List<SenderData> senders = masterDataService.getAllSenders();
            cbPengirim.getItems().addAll(senders);
            cbPengirim.setCellFactory(lv -> new ListCell<SenderData>() {
                @Override
                protected void updateItem(SenderData item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getName());
                }
            });
            cbPengirim.setButtonCell(new ListCell<SenderData>() {
                @Override
                protected void updateItem(SenderData item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getName());
                }
            });


            cbPenerima.getItems().clear();
            List<ReceiverData> receivers = masterDataService.getAllReceivers();
            cbPenerima.getItems().addAll(receivers);
            cbPenerima.setCellFactory(lv -> new ListCell<ReceiverData>() {
                @Override
                protected void updateItem(ReceiverData item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getName());
                }
            });
            cbPenerima.setButtonCell(new ListCell<ReceiverData>() {
                @Override
                protected void updateItem(ReceiverData item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getName());
                }
            });

            cbMerekPengiriman.getItems().clear();
            List<ShippingMerekData> mereks = masterDataService.getAllShippingMereks();
            cbMerekPengiriman.getItems().addAll(mereks);
            cbMerekPengiriman.setCellFactory(lv -> new ListCell<ShippingMerekData>() {
                @Override
                protected void updateItem(ShippingMerekData item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getName() + " (Rp" + String.format("%,.0f", item.getHarga()) + ")");
                }
            });
            cbMerekPengiriman.setButtonCell(new ListCell<ShippingMerekData>() {
                @Override
                protected void updateItem(ShippingMerekData item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getName() + " (Rp" + String.format("%,.0f", item.getHarga()) + ")");
                }
            });

            cbStatus.getSelectionModel().selectFirst();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error memuat data untuk ComboBoxes: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSimpanPengiriman(ActionEvent event) {
        try {
            String shipmentCode = txtShipmentCode.getText();
            SenderData selectedSender = cbPengirim.getValue();
            ReceiverData selectedReceiver = cbPenerima.getValue();
            LocalDate shippingLocalDate = dpTanggalPengiriman.getValue();
            String status = cbStatus.getValue();

            if (selectedSender == null || selectedReceiver == null || shippingLocalDate == null || status == null) {
                lblTambahTransaksiStatus.setText("Harap lengkapi semua kolom yang wajib diisi!");
                lblTambahTransaksiStatus.setStyle("-fx-text-fill: red;");
                return;
            }

            if (shipmentCode == null || shipmentCode.trim().isEmpty()) {
                shipmentCode = "SHP-" + System.currentTimeMillis();
            }

            pengirimanService.createShipment(
                shipmentCode,
                selectedSender.getId(),
                selectedReceiver.getId(),
                shippingLocalDate,
                status
            );

            lblTambahTransaksiStatus.setText("Pengiriman berhasil disimpan!");
            lblTambahTransaksiStatus.setStyle("-fx-text-fill: green;");

            txtShipmentCode.clear();
            cbPengirim.getSelectionModel().clearSelection();
            cbPenerima.getSelectionModel().clearSelection();
            cbMerekPengiriman.getSelectionModel().clearSelection();
            dpTanggalPengiriman.setValue(LocalDate.now());
            cbStatus.getSelectionModel().clearSelection();

            // PENTING: Beri tahu Observer bahwa data telah berubah
            if (dataChangePublisher != null) {
                dataChangePublisher.fireDataChanged();
            }

            // Opsional: Beralih ke tab "Lihat Data"
            if (mainTabPane != null && mainTabPane.getTabs().size() > 0) {
                mainTabPane.getSelectionModel().select(0);
            }
        } catch (IllegalArgumentException e) {
            lblTambahTransaksiStatus.setText("Validasi Error: " + e.getMessage());
            lblTambahTransaksiStatus.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            lblTambahTransaksiStatus.setText("Error saat menyimpan pengiriman: " + e.getMessage());
            lblTambahTransaksiStatus.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenTambahPengirim(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uas/transaksi/AddSenderDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Tambah Pengirim Baru");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(((Button)event.getSource()).getScene().getWindow());

            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            AddSenderController controller = loader.getController();
            controller.setMasterDataService(masterDataService);

            dialogStage.showAndWait();

            populateComboBoxes();
            if (dataChangePublisher != null) {
                dataChangePublisher.fireDataChanged();
            }

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Gagal membuka dialog Tambah Pengirim: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenTambahPenerima(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uas/transaksi/AddReceiverDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Tambah Penerima Baru");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(((Button)event.getSource()).getScene().getWindow());

            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            AddReceiverController controller = loader.getController();
            controller.setMasterDataService(masterDataService);

            dialogStage.showAndWait();

            populateComboBoxes();
            if (dataChangePublisher != null) {
                dataChangePublisher.fireDataChanged();
            }

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Gagal membuka dialog Tambah Penerima: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }
}