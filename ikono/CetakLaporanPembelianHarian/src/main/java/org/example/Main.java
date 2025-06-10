// Lokasi: src/main/java/org/example/Main.java
package org.example;

import org.example.entity.Product;
import org.example.gui.PurchaseReportWindow;
import org.example.util.HibernateUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class Main extends Application {

    private TableView<Product> productTable;
    private final ObservableList<Product> productData = FXCollections.observableArrayList();
    private TextField nameInput, categoryInput, stockInput, priceInput;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Manajemen Produk & Laporan Pembelian Harian");

        BorderPane root = new BorderPane();

        // 1. Buat MenuBar
        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        // 2. Buat Tampilan Utama (Manajemen Produk)
        VBox mainContent = new VBox(10);
        mainContent.setPadding(new Insets(10));

        GridPane formPane = createFormPane();
        TableView<Product> table = createProductTable();
        Button refreshButton = new Button("Refresh Produk");
        refreshButton.setOnAction(e -> loadProducts());

        mainContent.getChildren().addAll(new Label("Cetak Laporan"), formPane, table, refreshButton);
        root.setCenter(mainContent);

        loadProducts();

        Scene scene = new Scene(root, 700, 550);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            HibernateUtil.shutdown();
        });
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu reportMenu = new Menu("Laporan");
        MenuItem purchaseReportItem = new MenuItem("Laporan Pembelian Harian");
        
        purchaseReportItem.setOnAction(e -> {
            PurchaseReportWindow reportWindow = new PurchaseReportWindow();
            reportWindow.display();
        });
        
        reportMenu.getItems().add(purchaseReportItem);
        menuBar.getMenus().add(reportMenu);
        return menuBar;
    }

    private GridPane createFormPane() {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        nameInput = new TextField();
        nameInput.setPromptText("Nama produk");
        categoryInput = new TextField();
        categoryInput.setPromptText("Kategori produk");
        stockInput = new TextField();
        stockInput.setPromptText("Jumlah stok");
        priceInput = new TextField();
        priceInput.setPromptText("Harga satuan");

        Button addButton = new Button("Tambah Produk");
        addButton.setOnAction(e -> addProduct());

        grid.add(new Label("Nama Produk:"), 0, 0); grid.add(nameInput, 1, 0);
        grid.add(new Label("Kategori:"), 0, 1);    grid.add(categoryInput, 1, 1);
        grid.add(new Label("Stok:"), 0, 2);         grid.add(stockInput, 1, 2);
        grid.add(new Label("Harga Satuan:"), 0, 3); grid.add(priceInput, 1, 3);
        grid.add(addButton, 1, 4);

        return grid;
    }

    private TableView<Product> createProductTable() {
        productTable = new TableView<>();
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ProductID"));

        TableColumn<Product, String> nameCol = new TableColumn<>("Nama Produk");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("ProductName"));
        nameCol.setMinWidth(200);

        TableColumn<Product, String> catCol = new TableColumn<>("Kategori");
        catCol.setCellValueFactory(new PropertyValueFactory<>("Category"));

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stok");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("Stock"));

        TableColumn<Product, Double> priceCol = new TableColumn<>("Harga Satuan");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("UnitPrice"));

        productTable.getColumns().addAll(idCol, nameCol, catCol, stockCol, priceCol);
        productTable.setItems(productData);

        // INI BAGIAN YANG DIPERBAIKI
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return productTable;
    }

    private void addProduct() {
        try {
            Product product = new Product();
            product.setProductName(nameInput.getText());
            product.setCategory(categoryInput.getText());
            product.setStock(Integer.parseInt(stockInput.getText()));
            product.setUnitPrice(Double.parseDouble(priceInput.getText()));

            Transaction tx = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                tx = session.beginTransaction();
                session.persist(product);
                tx.commit();
                loadProducts(); // Refresh tabel
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            // Handle error jika input bukan angka
            System.err.println("Input Stok atau Harga harus berupa angka.");
        }
    }

    private void loadProducts() {
        productData.clear();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> query = session.createQuery("FROM Product", Product.class);
            List<Product> products = query.list();
            productData.addAll(products);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
