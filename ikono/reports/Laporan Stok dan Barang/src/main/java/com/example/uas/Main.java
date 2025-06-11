package com.example.uas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.uas.model.Product;
import com.example.uas.service.ProductService;
import com.example.uas.strategy.ProductIdSortStrategy;
import com.example.uas.strategy.ProductNameSortStrategy;
import com.example.uas.strategy.ProductPriceSortStrategy;
import com.example.uas.strategy.SortStrategy;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private ObservableList<Product> data = FXCollections.observableArrayList();
    private ProductService productService;
    private TableView<Product> tableView;
    private Map<String, SortStrategy> sortStrategies;
    private Label stockAlertLabel;
    private ComboBox<String> monthComboBox;
    private ComboBox<String> yearComboBox;
    private ObservableList<Product> originalData;

    private static final int LOW_STOCK_THRESHOLD = 20;
    private static final int OVERSTOCK_THRESHOLD = 100;

    @Override
    public void start(Stage primaryStage) {
        try {
            productService = new ProductService();

            sortStrategies = new HashMap<>();
            sortStrategies.put("Product ID", new ProductIdSortStrategy());
            sortStrategies.put("Name", new ProductNameSortStrategy());
            sortStrategies.put("Price", new ProductPriceSortStrategy());

            primaryStage.setTitle("Laporan Stok dan Barang");

            Label reportLabel = new Label("Laporan Stok dan Barang");
            stockAlertLabel = new Label();
            stockAlertLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");

            tableView = new TableView<>();

            TableColumn<Product, Integer> colProductId = new TableColumn<>("Product ID");
            colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));

            TableColumn<Product, String> colName = new TableColumn<>("Name");
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<Product, String> colDescription = new TableColumn<>("Description");
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

            TableColumn<Product, Double> colPrice = new TableColumn<>("Price");
            colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

            TableColumn<Product, Integer> colSupplierId = new TableColumn<>("Supplier ID");
            colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));

            TableColumn<Product, String> colCreatedAt = new TableColumn<>("Created At");
            colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

            TableColumn<Product, Integer> colStock = new TableColumn<>("Stock");
            colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

            tableView.getColumns().addAll(colProductId, colName, colDescription, colPrice, colSupplierId, colCreatedAt, colStock);

            Button refreshButton = new Button("Refresh Data");

            refreshButton.setOnAction(e -> {
                loadDataFromDatabase();
            });

            HBox crudButtons = new HBox(10, refreshButton);
            crudButtons.setPadding(new Insets(10));

            // Add Monthly Filter Section
            Label monthLabel = new Label("Filter Bulan:");
            monthComboBox = new ComboBox<>();
            monthComboBox.getItems().addAll(
                "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                "Juli", "Agustus", "September", "Oktober", "November", "Desember"
            );
            
            yearComboBox = new ComboBox<>();
            yearComboBox.getItems().addAll("2024", "2023", "2022");
            yearComboBox.setValue("2024");
            
            Button searchButton = new Button("Tampilkan");
            Button resetButton = new Button("Reset");

            HBox monthFilterBox = new HBox(10);
            monthFilterBox.setAlignment(Pos.CENTER_LEFT);
            monthFilterBox.getChildren().addAll(monthLabel, monthComboBox, yearComboBox, searchButton, resetButton);
            monthFilterBox.setPadding(new Insets(10));

            searchButton.setOnAction(e -> {
                String selectedMonth = monthComboBox.getValue();
                String selectedYear = yearComboBox.getValue();

                if (selectedMonth == null) {
                    showError("Error", "Pilih bulan terlebih dahulu!");
                    return;
                }

                int monthNumber = getMonthNumber(selectedMonth);
                
                ObservableList<Product> filteredData = FXCollections.observableArrayList();
                for (Product product : originalData) {
                    String dateStr = product.getCreatedAt();
                    if (dateStr != null && !dateStr.isEmpty()) {
                        try {
                            LocalDateTime productDate = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            if (productDate.getMonthValue() == monthNumber && 
                                productDate.getYear() == Integer.parseInt(selectedYear)) {
                                filteredData.add(product);
                            }
                        } catch (Exception ex) {
                            System.err.println("Error parsing date: " + dateStr);
                        }
                    }
                }
                
                tableView.setItems(filteredData);
                updateStockAlerts(filteredData);
            });

            resetButton.setOnAction(e -> {
                monthComboBox.setValue(null);
                yearComboBox.setValue("2024");
                tableView.setItems(originalData);
                updateStockAlerts(originalData);
            });

            originalData = FXCollections.observableArrayList();
            
            loadDataFromDatabase();

            VBox vbox = new VBox(10);
            vbox.getChildren().addAll(
                reportLabel,
                crudButtons,
                monthFilterBox,
                tableView,
                stockAlertLabel
            );
            vbox.setPadding(new Insets(10));

            Scene scene = new Scene(vbox, 900, 700);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            showError("Application Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadDataFromDatabase() {
        List<Product> products = productService.getAllProducts();
        data.clear();
        originalData.clear();
        if (products != null) {
            data.addAll(products);
            originalData.addAll(products);
        }
        tableView.setItems(data);
        updateStockAlerts();
    }

    private void updateStockAlerts() {
        updateStockAlerts(this.data);
    }

    private void updateStockAlerts(ObservableList<Product> products) {
        StringBuilder alerts = new StringBuilder();
        
        for (Product product : products) {
            if (product.getStock() < LOW_STOCK_THRESHOLD) {
                alerts.append("Stok ").append(product.getName()).append(" menipis (Sisa: ").append(product.getStock()).append(")!\n");
            } else if (product.getStock() > OVERSTOCK_THRESHOLD) {
                alerts.append("Stok ").append(product.getName()).append(" berlebih (Sisa: ").append(product.getStock()).append(")!\n");
            }
        }

        if (alerts.length() == 0) {
            stockAlertLabel.setText("Semua stok dalam kondisi baik.");
            stockAlertLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
        } else {
            stockAlertLabel.setText("PERINGATAN STOK:\n" + alerts.toString());
            stockAlertLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private int getMonthNumber(String monthName) {
        switch (monthName.toLowerCase()) {
            case "januari": return 1;
            case "februari": return 2;
            case "maret": return 3;
            case "april": return 4;
            case "mei": return 5;
            case "juni": return 6;
            case "juli": return 7;
            case "agustus": return 8;
            case "september": return 9;
            case "oktober": return 10;
            case "november": return 11;
            case "desember": return 12;
            default: return 1;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}