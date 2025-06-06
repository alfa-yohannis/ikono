package uas.transaksi;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;

import uas.transaksi.data.ShipmentData;
import uas.transaksi.data.SenderData;
import uas.transaksi.data.ReceiverData;
import uas.transaksi.service.PengirimanService;
import uas.transaksi.event.DataChangeListener;
import uas.transaksi.event.DataChangePublisher;

import java.sql.Date;
import java.util.List;

public class LihatTransaksiController implements DataChangeListener { // Implement interface

    @FXML private TableView<ShipmentData> shipmentTable;
    @FXML private TableColumn<ShipmentData, Integer> colId;
    @FXML private TableColumn<ShipmentData, String> colShipmentCode;
    @FXML private TableColumn<ShipmentData, String> colSender;
    @FXML private TableColumn<ShipmentData, String> colReceiver;
    @FXML private TableColumn<ShipmentData, Date> colShippingDate;
    @FXML private TableColumn<ShipmentData, String> colStatus;
    @FXML private Label lblLihatTransaksiStatus;

    private PengirimanService pengirimanService;
    private DataChangePublisher dataChangePublisher;
    private ObservableList<ShipmentData> shipmentDataList;

    public LihatTransaksiController() {}

    // Metode untuk meng-inject Service Layer
    public void setPengirimanService(PengirimanService pengirimanService) {
        this.pengirimanService = pengirimanService;
        // handleRefreshData(null); // Dipanggil setelah publisher diset
    }

    // Metode untuk meng-inject DataChangePublisher
    public void setDataChangePublisher(DataChangePublisher dataChangePublisher) {
        this.dataChangePublisher = dataChangePublisher;
        this.dataChangePublisher.addListener(this); // Daftarkan diri sebagai listener
        handleRefreshData(null); // Refresh data segera setelah publisher diset
    }

    @Override
    public void onDataChanged() {
        System.out.println("LihatTransaksiController: Menerima notifikasi data berubah, merefresh tabel.");
        handleRefreshData(null); // Panggil metode refresh
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colShipmentCode.setCellValueFactory(new PropertyValueFactory<>("shipmentCode"));

        colSender.setCellValueFactory(cellData -> {
            SenderData sender = cellData.getValue().getSender();
            return new ReadOnlyStringWrapper(sender != null ? sender.getName() : "N/A");
        });
        colReceiver.setCellValueFactory(cellData -> {
            ReceiverData receiver = cellData.getValue().getReceiver();
            return new ReadOnlyStringWrapper(receiver != null ? receiver.getName() : "N/A");
        });
        colShippingDate.setCellValueFactory(new PropertyValueFactory<>("shippingDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        shipmentDataList = FXCollections.observableArrayList();
        shipmentTable.setItems(shipmentDataList);
    }

    @FXML
    private void handleRefreshData(ActionEvent event) {
        try {
            if (pengirimanService == null) {
                lblLihatTransaksiStatus.setText("Service belum diinisialisasi.");
                return;
            }
            shipmentDataList.clear();
            List<ShipmentData> shipments = pengirimanService.getAllShipments();
            if (shipments != null) {
                shipmentDataList.addAll(shipments);
                lblLihatTransaksiStatus.setText("Data berhasil dimuat. Total: " + shipments.size() + " pengiriman.");
            } else {
                lblLihatTransaksiStatus.setText("Tidak ada data pengiriman.");
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error saat me-refresh data pengiriman: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }
}