package uas.transaksi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import uas.transaksi.service.MasterDataService;
import uas.transaksi.data.SenderData;
import javafx.scene.control.Alert;

public class AddSenderController {

    @FXML private TextField txtSenderId;
    @FXML private TextField txtSenderName;
    @FXML private TextField txtSenderAddress;
    @FXML private Label lblStatus;

    private MasterDataService masterDataService;

    public void setMasterDataService(MasterDataService masterDataService) {
        this.masterDataService = masterDataService;
    }

    @FXML
    private void handleSaveSender(ActionEvent event) {
        try {
            int id;
            try {
                id = Integer.parseInt(txtSenderId.getText());
            } catch (NumberFormatException e) {
                lblStatus.setText("ID Pengirim harus angka.");
                lblStatus.setStyle("-fx-text-fill: red;");
                return;
            }

            String name = txtSenderName.getText();
            String address = txtSenderAddress.getText();

            if (name.isEmpty() || address.isEmpty()) {
                lblStatus.setText("Nama dan Alamat tidak boleh kosong.");
                lblStatus.setStyle("-fx-text-fill: red;");
                return;
            }

            if (masterDataService.getSenderById(id) != null) {
                lblStatus.setText("ID Pengirim sudah ada.");
                lblStatus.setStyle("-fx-text-fill: red;");
                return;
            }

            SenderData newSender = new SenderData(id, name, address);
            masterDataService.addSender(newSender);
            lblStatus.setText("Pengirim berhasil disimpan!");
            lblStatus.setStyle("-fx-text-fill: green;");

            txtSenderId.clear();
            txtSenderName.clear();
            txtSenderAddress.clear();

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