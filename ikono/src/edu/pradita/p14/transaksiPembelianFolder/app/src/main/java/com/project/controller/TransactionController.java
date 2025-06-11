[media pointer="file-service://file-NHofuTk42Lopd9JpmSyZub"]
package com.project.controller;

import com.project.model.PurchaseDetail;
import com.project.model.PurchaseTransaction;
import com.project.service.TransactionService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class TransactionController {

    private TableView<PurchaseDetail> table;
    private TextField purchaseDateField, totalAmountField, paymentStatusField, paymentMethodField, vendorIDField;
    private TextField productIDField, quantityField, unitPriceField, subtotalField;
    private final TransactionService service = new TransactionService();

    public VBox getView() {
        table = new TableView<>();

        TableColumn<PurchaseDetail, Integer> purchaseIDColumn = new TableColumn<>("Purchase ID");
        purchaseIDColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getTransaction().getPurchaseID()).asObject());

        TableColumn<PurchaseDetail, String> purchaseDateColumn = new TableColumn<>("Purchase Date");
        purchaseDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTransaction().getPurchaseDate()));

        TableColumn<PurchaseDetail, Double> totalAmountColumn = new TableColumn<>("Total Amount");
        totalAmountColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getTransaction().getTotalAmount()).asObject());

        TableColumn<PurchaseDetail, String> paymentStatusColumn = new TableColumn<>("Payment Status");
        paymentStatusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTransaction().getPaymentStatus()));

        TableColumn<PurchaseDetail, String> paymentMethodColumn = new TableColumn<>("Payment Method");
        paymentMethodColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTransaction().getPaymentMethod()));

        TableColumn<PurchaseDetail, Integer> vendorIDColumn = new TableColumn<>("Vendor ID");
        vendorIDColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getTransaction().getVendorID()).asObject());

        TableColumn<PurchaseDetail, Integer> productIDColumn = new TableColumn<>("Product ID");
        productIDColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getProductID()).asObject());

        TableColumn<PurchaseDetail, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getQuantity()).asObject());

        TableColumn<PurchaseDetail, Double> unitPriceColumn = new TableColumn<>("Unit Price");
        unitPriceColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getUnitPrice()).asObject());

        TableColumn<PurchaseDetail, Double> subtotalColumn = new TableColumn<>("Subtotal");
        subtotalColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getSubtotal()).asObject());

        table.getColumns().addAll(purchaseIDColumn, purchaseDateColumn, totalAmountColumn, paymentStatusColumn,
                paymentMethodColumn, vendorIDColumn, productIDColumn, quantityColumn, unitPriceColumn, subtotalColumn);

        purchaseDateField = new TextField();
        totalAmountField = new TextField();
        paymentStatusField = new TextField();
        paymentMethodField = new TextField();
        vendorIDField = new TextField();
        productIDField = new TextField();
        quantityField = new TextField();
        unitPriceField = new TextField();
        subtotalField = new TextField();

        Button addButton = new Button("Add Purchase Transaction");
        addButton.setOnAction(e -> addTransaction());

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(table,
                new Label("Purchase Date:"), purchaseDateField,
                new Label("Total Amount:"), totalAmountField,
                new Label("Payment Status:"), paymentStatusField,
                new Label("Payment Method:"), paymentMethodField,
                new Label("Vendor ID:"), vendorIDField,
                new Label("Product ID:"), productIDField,
                new Label("Quantity:"), quantityField,
                new Label("Unit Price:"), unitPriceField,
                new Label("Subtotal:"), subtotalField,
                addButton);

        table.setItems(service.getAllDetails());
        return vbox;
    }

    private void addTransaction() {
        PurchaseTransaction pt = new PurchaseTransaction();
        pt.setPurchaseDate(purchaseDateField.getText());
        pt.setTotalAmount(Double.parseDouble(totalAmountField.getText()));
        pt.setPaymentStatus(paymentStatusField.getText());
        pt.setPaymentMethod(paymentMethodField.getText());
        pt.setVendorID(Integer.parseInt(vendorIDField.getText()));

        PurchaseDetail pd = new PurchaseDetail();
        pd.setProductID(Integer.parseInt(productIDField.getText()));
        pd.setQuantity(Integer.parseInt(quantityField.getText()));
        pd.setUnitPrice(Double.parseDouble(unitPriceField.getText()));
        pd.setSubtotal(Double.parseDouble(subtotalField.getText()));
        pd.setTransaction(pt);

        pt.getDetails().add(pd);

        service.saveTransaction(pt);
        table.setItems(service.getAllDetails());
    }
}
