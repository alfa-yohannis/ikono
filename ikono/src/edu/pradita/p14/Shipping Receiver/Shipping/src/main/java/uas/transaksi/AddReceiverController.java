package uas.transaksi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import uas.transaksi.service.MasterDataService;
import uas.transaksi.data.ReceiverData;
import javafx.scene.control.Alert;

public class AddReceiverController {

    @FXML private TextField txtReceiverId;
    @FXML private TextField txtReceiverName;
    @FXML private TextField txtReceiverAddress;
    @FXML private Label lblStatus;

    private MasterDataService masterDataService;

    public void setMasterDataService(MasterDataService masterDataService) {
        this.masterDataService = masterDataService;
    }

    @FXML
    private void handleSaveReceiver(ActionEvent event) {
        try {
            int id;
            try {
                id = Integer.parseInt(txtReceiverId.getText());
            } catch (NumberFormatException e) {
                lblStatus.setText("ID Penerima harus angka.");
                lblStatus.setStyle("-fx-text-fill: red;");
                return;
            }

            String name = txtReceiverName.getText();
            String address = txtReceiverAddress.getText();

            if (name.isEmpty() || address.isEmpty()) {
                lblStatus.setText("Nama dan Alamat tidak boleh kosong.");
                lblStatus.setStyle("-fx-text-fill: red;");
                return;
            }

            if (masterDataService.getReceiverById(id) != null) {
                lblStatus.setText("ID Penerima sudah ada.");
                lblStatus.setStyle("-fx-text-fill: red;");
                return;
            }

            ReceiverData newReceiver = new ReceiverData(id, name, address);
            masterDataService.addReceiver(newReceiver);
            lblStatus.setText("Penerima berhasil disimpan!");
            lblStatus.setStyle("-fx-text-fill: green;");

            txtReceiverId.clear();
            txtReceiverName.clear();
            txtReceiverAddress.clear();

        } catch (Exception e) {
            lblStatus.setText("Error saat menyimpan: " + e.getMessage());
            lblStatus.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}