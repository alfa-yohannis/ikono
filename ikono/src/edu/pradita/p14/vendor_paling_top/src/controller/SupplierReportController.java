package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.SupplierReport;
import utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierReportController {

    @FXML
    private TableView<SupplierReport> supplierTable;

    @FXML
    private TableColumn<SupplierReport, String> supplierNameColumn;

    @FXML
    private TableColumn<SupplierReport, Double> totalPurchaseColumn;

    private ObservableList<SupplierReport> supplierList = FXCollections.observableArrayList();

    public void initialize() {
        supplierNameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        totalPurchaseColumn.setCellValueFactory(new PropertyValueFactory<>("totalPurchase"));

        loadSupplierData();
    }

    private void loadSupplierData() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "SELECT supplier_name, SUM(total_purchase) AS total FROM purchases GROUP BY supplier_name ORDER BY total DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                supplierList.add(new SupplierReport(
                        rs.getString("supplier_name"),
                        rs.getDouble("total")
                ));
            }
            supplierTable.setItems(supplierList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
