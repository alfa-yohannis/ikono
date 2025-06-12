package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;

import java.util.List;

public class App extends Application {

    private TableView<MasterModel> tableView;
    private final ObservableList<MasterModel> data = FXCollections.observableArrayList();

    private TextField txtId;
    private TextField txtName;
    private TextField txtQuantity;
    private TextField txtPrice;
    // Ganti TextField txtCategory menjadi ComboBox<String> cmbCategory
    private ComboBox<String> cmbCategory; 

    private MasterModelDao masterModelDao;
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    // Daftar kategori yang akan kita gunakan
    private final ObservableList<String> categoryOptions = 
        FXCollections.observableArrayList(
            "Obat Bebas",
            "Obat Keras",
            "Alat Kesehatan",
            "Suplemen",
            "Herbal",
            "Lainnya" // Tambahkan opsi lain jika perlu
        );

    @Override
    public void init() throws Exception {
        super.init();
        MasterModelDao realDao = new MasterModelDaoImpl(sessionFactory);
        this.masterModelDao = new LoggingMasterModelDaoDecorator(realDao);
        System.out.println("MasterModelDao telah diinisialisasi dengan Logging Decorator.");
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        setupTable();
        setupForm(); // cmbCategory akan di-setup di sini
        setupTableSelectionListener();
        
        HBox formLayout = new HBox(10, 
                new Label("ID:"), txtId, 
                new Label("Name:"), txtName, 
                new Label("Quantity:"), txtQuantity,
                new Label("Price:"), txtPrice,
                new Label("Category:"), cmbCategory // Gunakan cmbCategory
        );
        HBox buttonLayout = new HBox(10, createAddButton(), createEditButton(), createDeleteButton());
        formLayout.setPadding(new Insets(0, 0, 10, 0));

        root.getChildren().addAll(new Label("Master Data (All Patterns with Category ComboBox)"), tableView, formLayout, buttonLayout);

        Scene scene = new Scene(root, 1100, 500); 
        primaryStage.setTitle("Master Data (Category ComboBox)");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        loadData(); 
    }

    @Override
    public void stop() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            HibernateUtil.shutdown();
        }
    }

    private void setupTable() {
        tableView = new TableView<>();
        TableColumn<MasterModel, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(100);

        TableColumn<MasterModel, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setPrefWidth(200); 

        TableColumn<MasterModel, String> colQuantity = new TableColumn<>("Quantity");
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colQuantity.setPrefWidth(100);

        TableColumn<MasterModel, Double> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPrice.setPrefWidth(100);

        TableColumn<MasterModel, String> colCategory = new TableColumn<>("Category");
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colCategory.setPrefWidth(150);

        tableView.getColumns().addAll(colId, colName, colQuantity, colPrice, colCategory);
        tableView.setItems(data);
        VBox.setVgrow(tableView, javafx.scene.layout.Priority.ALWAYS);
    }

    private void setupForm() {
        txtId = new TextField();
        txtId.setPromptText("Enter ID");
        txtName = new TextField();
        txtName.setPromptText("Enter Name");
        txtQuantity = new TextField();
        txtQuantity.setPromptText("Enter Quantity");
        txtPrice = new TextField();
        txtPrice.setPromptText("Enter Price");
        
        // Inisialisasi dan isi ComboBox untuk Category
        cmbCategory = new ComboBox<>(categoryOptions);
        cmbCategory.setPromptText("Select Category");
        cmbCategory.setPrefWidth(150); // Sesuaikan lebar jika perlu
    }

    private void setupTableSelectionListener() {
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtId.setText(newSelection.getId());
                txtName.setText(newSelection.getName());
                txtQuantity.setText(newSelection.getQuantity());
                txtPrice.setText(newSelection.getPrice() != null ? String.valueOf(newSelection.getPrice()) : "");
                // Set nilai ComboBox berdasarkan kategori item yang dipilih
                cmbCategory.setValue(newSelection.getCategory()); 
            } else {
                clearForm();
            }
        });
    }

    private void clearForm() {
        txtId.clear();
        txtName.clear();
        txtQuantity.clear();
        txtPrice.clear();
        cmbCategory.setValue(null); // Bersihkan pilihan ComboBox
        tableView.getSelectionModel().clearSelection();
    }

    private Button createAddButton() {
        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(e -> {
            String id = txtId.getText();
            String name = txtName.getText();
            String quantity = txtQuantity.getText();
            String priceStr = txtPrice.getText();
            String category = cmbCategory.getValue(); // Ambil nilai dari ComboBox

            if (id.isEmpty() || name.isEmpty() || quantity.isEmpty() || priceStr.isEmpty() || category == null || category.isEmpty()) {
                showAlert("Input Error", "Semua field harus diisi, termasuk kategori.");
                return;
            }
            try {
                Double price = Double.parseDouble(priceStr);
                MasterModel newItem = new MasterModel(id, name, quantity, price, category); 
                Command addCommand = new AddMasterModelCommand(masterModelDao, newItem);
                
                addCommand.execute();
                data.add(newItem); 
                clearForm();
            } catch (NumberFormatException nfe) {
                showAlert("Input Error", "Price harus berupa angka yang valid.");
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Gagal menyimpan data: " + ex.getMessage());
            }
        });
        return btnAdd;
    }

    private Button createEditButton() {
        Button btnEdit = new Button("Edit");
        btnEdit.setOnAction(e -> {
            MasterModel selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                showAlert("Selection Error", "Pilih item yang ingin diubah.");
                return;
            }
            String name = txtName.getText();
            String quantity = txtQuantity.getText();
            String priceStr = txtPrice.getText();
            String category = cmbCategory.getValue(); // Ambil nilai dari ComboBox

            if (name.isEmpty() || quantity.isEmpty() || priceStr.isEmpty() || category == null || category.isEmpty()) {
                showAlert("Input Error", "Semua field (Name, Quantity, Price, Category) harus diisi untuk update.");
                return;
            }
            try {
                Double price = Double.parseDouble(priceStr);
                MasterModel updatedItem = new MasterModel(selectedItem.getId(), name, quantity, price, category); 
                Command editCommand = new EditMasterModelCommand(masterModelDao, updatedItem);
                
                editCommand.execute();
                loadData(); 
                clearForm();
            } catch (NumberFormatException nfe) {
                showAlert("Input Error", "Price harus berupa angka yang valid.");
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Gagal mengubah data: " + ex.getMessage());
            }
        });
        return btnEdit;
    }

    private Button createDeleteButton() {
        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> {
            MasterModel selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                showAlert("Selection Error", "Pilih item yang ingin dihapus.");
                return;
            }
            Command deleteCommand = new DeleteMasterModelCommand(masterModelDao, selectedItem);
            try {
                deleteCommand.execute();
                data.remove(selectedItem); 
                clearForm();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Gagal menghapus data: " + ex.getMessage());
                loadData(); 
            }
        });
        return btnDelete;
    }
    
    private void loadData() {
        try {
            List<MasterModel> items = masterModelDao.findAll(); 
            final List<MasterModel> finalItems = items; 
            javafx.application.Platform.runLater(() -> {
                data.setAll(finalItems);
            });
        } catch (Exception e) {
            e.printStackTrace();
            final String errorMessage = e.getMessage();
            javafx.application.Platform.runLater(() -> {
                 showAlert("Database Error", "Gagal memuat data: " + errorMessage);
            });
        }
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
