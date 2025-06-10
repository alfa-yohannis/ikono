package com.inventory.warehouseinventorysystem.controller;

import com.inventory.warehouseinventorysystem.adapter.LegacyProductDataProvider;
import com.inventory.warehouseinventorysystem.adapter.ProductAdapter;
import com.inventory.warehouseinventorysystem.dao.DaoFactory;
import com.inventory.warehouseinventorysystem.dao.HibernateDaoFactory;
import com.inventory.warehouseinventorysystem.model.Product;
import com.inventory.warehouseinventorysystem.model.Warehouse;
import com.inventory.warehouseinventorysystem.observer.ProductEventManager; // Import untuk ProductEventManager
import com.inventory.warehouseinventorysystem.service.ProductService;
import com.inventory.warehouseinventorysystem.service.WarehouseService;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class MainViewController {

    private static final Logger logger = LoggerFactory.getLogger(MainViewController.class);

    @FXML private BorderPane mainBorderPane;
    @FXML private MenuItem menuItemExit;
    @FXML private MenuItem menuItemManageWarehouses;
    @FXML private MenuItem menuItemManageProducts;
    @FXML private MenuItem menuItemImportLegacyProduct;
    @FXML private MenuItem menuItemAbout;
    @FXML private ListView<Warehouse> warehouseListView;
    @FXML private Button addWarehouseButton;
    @FXML private Button editWarehouseButton;
    @FXML private Button deleteWarehouseButton;
    @FXML private Label selectedWarehouseLabel;
    @FXML private TableView<Product> productTableView;
    @FXML private TableColumn<Product, String> productSkuColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productQuantityColumn;
    @FXML private TableColumn<Product, BigDecimal> productPriceColumn;
    @FXML private Button addProductButton;
    @FXML private Button editProductButton;
    @FXML private Button deleteProductButton;
    @FXML private Label statusLabel;

    private ObservableList<Warehouse> observableWarehouseList = FXCollections.observableArrayList();
    private ObservableList<Product> observableProductList = FXCollections.observableArrayList();

    private WarehouseService warehouseService;
    private ProductService productService;
    private DaoFactory daoFactory;

    @FXML
    public void initialize() {
        logger.info("Initializing MainViewController...");
        try {
            this.daoFactory = new HibernateDaoFactory();
            this.warehouseService = new WarehouseService(daoFactory);
            // Inisialisasi ProductService dengan ProductEventManager
            this.productService = new ProductService(daoFactory, ProductEventManager.getInstance());
            logger.info("Services initialized.");

            setupWarehouseListView();
            logger.info("Warehouse ListView setup complete.");

            setupProductTableView();
            logger.info("Product TableView setup complete.");

            loadWarehouses();
            logger.info("Initial warehouse data loaded.");

            warehouseListView.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> handleWarehouseSelection(newValue)
            );

            productTableView.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> updateButtonStates()
            );

            updateButtonStates();

            statusLabel.setText("Application initialized. Ready.");
            logger.info("MainViewController initialization complete.");

        } catch (Exception e) {
            logger.error("Error during MainViewController initialization:", e);
            statusLabel.setText("Error: Application failed to initialize correctly. Please see logs.");
            showErrorAlert("Initialization Error", "Could not initialize the application view components. " + e.getMessage());
        }
    }

    private void setupWarehouseListView() {
        warehouseListView.setItems(observableWarehouseList);
        warehouseListView.setCellFactory(param -> new ListCell<Warehouse>() {
            @Override
            protected void updateItem(Warehouse item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });
    }

    private void setupProductTableView() {
        productSkuColumn.setCellValueFactory(new PropertyValueFactory<>("sku"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productTableView.setItems(observableProductList);
    }

    private void loadWarehouses() {
        try {
            List<Warehouse> warehouses = warehouseService.getAllWarehouses();
            Warehouse selectedBeforeReload = warehouseListView.getSelectionModel().getSelectedItem();
            int selectedIndexBeforeReload = warehouseListView.getSelectionModel().getSelectedIndex();

            if (warehouses != null) {
                observableWarehouseList.setAll(warehouses);
                logger.debug("Loaded {} warehouses.", warehouses.size());
            } else {
                logger.warn("getAllWarehouses() returned null. No warehouses to display.");
                observableWarehouseList.clear();
            }

            if (!observableWarehouseList.isEmpty()) {
                if (selectedBeforeReload != null) {
                    final Warehouse finalSelectedBeforeReload = selectedBeforeReload;
                    Optional<Warehouse> reselected = observableWarehouseList.stream()
                            .filter(wh -> wh.getId() == finalSelectedBeforeReload.getId())
                            .findFirst();
                    if (reselected.isPresent()) {
                        warehouseListView.getSelectionModel().select(reselected.get());
                    } else if (selectedIndexBeforeReload >= 0 && selectedIndexBeforeReload < observableWarehouseList.size()) {
                        warehouseListView.getSelectionModel().select(selectedIndexBeforeReload);
                    } else {
                        warehouseListView.getSelectionModel().selectFirst();
                    }
                } else {
                    warehouseListView.getSelectionModel().selectFirst();
                }
            } else {
                handleWarehouseSelection(null);
            }
        } catch (Exception e) {
            logger.error("Failed to load warehouses from service.", e);
            statusLabel.setText("Error: Failed to load warehouses.");
            showErrorAlert("Load Error", "Failed to load warehouses: " + e.getMessage());
        }
    }

    private void handleWarehouseSelection(Warehouse selectedWarehouse) {
        observableProductList.clear();
        if (selectedWarehouse != null) {
            logger.info("Warehouse selected: ID = {}, Name = {}", selectedWarehouse.getId(), selectedWarehouse.getName());
            selectedWarehouseLabel.setText("Products in: " + selectedWarehouse.getName());
            try {
                List<Product> products = productService.getProductsByWarehouseId(selectedWarehouse.getId());
                if (products != null) {
                    observableProductList.setAll(products);
                    logger.debug("Loaded {} products for warehouse '{}'.", products.size(), selectedWarehouse.getName());
                } else {
                    logger.warn("getProductsByWarehouseId() returned null for warehouse: {}", selectedWarehouse.getName());
                }
            } catch (Exception e) {
                logger.error("Failed to load products for warehouse: " + selectedWarehouse.getName(), e);
                statusLabel.setText("Error: Failed to load products for " + selectedWarehouse.getName());
                showErrorAlert("Load Error", "Failed to load products: " + e.getMessage());
            }
        } else {
            logger.info("No warehouse selected.");
            selectedWarehouseLabel.setText("Products in: (No Warehouse Selected)");
        }
        updateButtonStates();
    }

    private void updateButtonStates() {
        boolean warehouseSelected = warehouseListView.getSelectionModel().getSelectedItem() != null;
        boolean productSelected = productTableView.getSelectionModel().getSelectedItem() != null;

        editWarehouseButton.setDisable(!warehouseSelected);
        deleteWarehouseButton.setDisable(!warehouseSelected);
        addProductButton.setDisable(!warehouseSelected);
        editProductButton.setDisable(!productSelected);
        deleteProductButton.setDisable(!productSelected);
        logger.trace("Button states updated. WarehouseSelected: {}, ProductSelected: {}", warehouseSelected, productSelected);
    }

    @FXML
    void handleAddWarehouseAction(ActionEvent event) {
        logger.debug("Add Warehouse action triggered.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/warehouseinventorysystem/view/WarehouseDialog.fxml"));
            DialogPane dialogPane = loader.load();

            WarehouseDialogController dialogController = loader.getController();
            dialogController.setWarehouse(null);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add New Warehouse");
            if (mainBorderPane.getScene() != null && mainBorderPane.getScene().getWindow() != null) {
                dialog.initOwner(mainBorderPane.getScene().getWindow());
            }

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Warehouse newWarehouse = dialogController.processResults();
                if (newWarehouse != null) {
                    try {
                        warehouseService.saveWarehouse(newWarehouse);
                        loadWarehouses();
                        statusLabel.setText("Warehouse '" + newWarehouse.getName() + "' added successfully.");
                        logger.info("Warehouse '{}' added successfully.", newWarehouse.getName());
                        observableWarehouseList.stream()
                                .filter(wh -> wh.getName().equals(newWarehouse.getName())) // Asumsi nama unik, atau cari by ID jika sudah di-return service
                                .findFirst()
                                .ifPresent(warehouseListView.getSelectionModel()::select);
                    } catch (IllegalArgumentException e) {
                        logger.error("Error saving new warehouse: {}", e.getMessage());
                        showErrorAlert("Save Error", e.getMessage());
                    } catch (Exception e) {
                        logger.error("Could not save new warehouse due to an unexpected error.", e);
                        showErrorAlert("Save Error", "An unexpected error occurred while saving the warehouse: " + e.getMessage());
                    }
                }
            } else {
                logger.debug("Add Warehouse dialog cancelled.");
            }
        } catch (IOException e) {
            logger.error("Failed to load WarehouseDialog.fxml", e);
            showErrorAlert("Dialog Error", "Could not open the warehouse dialog: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error in handleAddWarehouseAction", e);
            showErrorAlert("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void handleEditWarehouseAction(ActionEvent event) {
        logger.debug("Edit Warehouse action triggered.");
        Warehouse selectedWarehouse = warehouseListView.getSelectionModel().getSelectedItem();

        if (selectedWarehouse == null) {
            showErrorAlert("No Selection", "Please select a warehouse to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/warehouseinventorysystem/view/WarehouseDialog.fxml"));
            DialogPane dialogPane = loader.load();

            WarehouseDialogController dialogController = loader.getController();
            dialogController.setWarehouse(selectedWarehouse);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Warehouse");
            if (mainBorderPane.getScene() != null && mainBorderPane.getScene().getWindow() != null) {
                dialog.initOwner(mainBorderPane.getScene().getWindow());
            }

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Warehouse editedWarehouse = dialogController.processResults();
                if (editedWarehouse != null) {
                    try {
                        warehouseService.saveWarehouse(editedWarehouse);
                        loadWarehouses();
                        statusLabel.setText("Warehouse '" + editedWarehouse.getName() + "' updated successfully.");
                        logger.info("Warehouse '{}' updated successfully.", editedWarehouse.getName());
                        final int finalEditedId = editedWarehouse.getId();
                        observableWarehouseList.stream()
                                .filter(wh -> wh.getId() == finalEditedId)
                                .findFirst()
                                .ifPresent(warehouseListView.getSelectionModel()::select);
                    } catch (IllegalArgumentException e) {
                        logger.error("Error updating warehouse: {}", e.getMessage());
                        showErrorAlert("Update Error", e.getMessage());
                    } catch (Exception e) {
                        logger.error("Could not update warehouse due to an unexpected error.", e);
                        showErrorAlert("Update Error", "An unexpected error occurred while updating the warehouse: " + e.getMessage());
                    }
                }
            } else {
                logger.debug("Edit Warehouse dialog cancelled.");
            }
        } catch (IOException e) {
            logger.error("Failed to load WarehouseDialog.fxml for editing", e);
            showErrorAlert("Dialog Error", "Could not open the warehouse dialog for editing: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error in handleEditWarehouseAction", e);
            showErrorAlert("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteWarehouseAction(ActionEvent event) {
        logger.debug("Delete Warehouse action triggered.");
        Warehouse selectedWarehouse = warehouseListView.getSelectionModel().getSelectedItem();

        if (selectedWarehouse == null) {
            showErrorAlert("No Selection", "Please select a warehouse to delete.");
            return;
        }

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirm Deletion");
        confirmationDialog.setHeaderText("Delete Warehouse: " + selectedWarehouse.getName());
        confirmationDialog.setContentText("Are you sure you want to delete this warehouse? \n" +
                "WARNING: Deleting a warehouse will also delete ALL associated products (if cascade is set up this way). This action cannot be undone.");
        if (mainBorderPane.getScene() != null && mainBorderPane.getScene().getWindow() != null) {
            confirmationDialog.initOwner(mainBorderPane.getScene().getWindow());
        }

        Optional<ButtonType> result = confirmationDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                logger.info("User confirmed deletion for warehouse ID: {}, Name: {}", selectedWarehouse.getId(), selectedWarehouse.getName());
                warehouseService.deleteWarehouse(selectedWarehouse.getId());
                loadWarehouses();
                statusLabel.setText("Warehouse '" + selectedWarehouse.getName() + "' deleted successfully.");
                logger.info("Warehouse '{}' deleted successfully.", selectedWarehouse.getName());
            } catch (IllegalArgumentException e) {
                logger.error("Error deleting warehouse (not found or other validation): {}", e.getMessage());
                showErrorAlert("Delete Error", e.getMessage());
            } catch (Exception e) {
                logger.error("Could not delete warehouse '" + selectedWarehouse.getName() + "' due to an unexpected error.", e);
                showErrorAlert("Delete Error", "An unexpected error occurred while deleting the warehouse: " + e.getMessage());
            }
        } else {
            logger.debug("User cancelled warehouse deletion.");
            statusLabel.setText("Warehouse deletion cancelled.");
        }
    }

    @FXML
    void handleAddProductAction(ActionEvent event) {
        logger.debug("Add Product action triggered.");
        Warehouse selectedWarehouse = warehouseListView.getSelectionModel().getSelectedItem();

        if (selectedWarehouse == null) {
            showErrorAlert("No Warehouse Selected", "Please select a warehouse first to add a product to.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/warehouseinventorysystem/view/ProductDialog.fxml"));
            DialogPane dialogPane = loader.load();

            ProductDialogController dialogController = loader.getController();
            dialogController.setWarehouses(observableWarehouseList);
            dialogController.setProductAndSelectedWarehouse(null, selectedWarehouse);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add New Product to " + selectedWarehouse.getName());
            if (mainBorderPane.getScene() != null && mainBorderPane.getScene().getWindow() != null) {
                dialog.initOwner(mainBorderPane.getScene().getWindow());
            }

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Product newProduct = dialogController.processResults();
                if (newProduct != null) {
                    try {
                        productService.saveProduct(newProduct);
                        handleWarehouseSelection(selectedWarehouse);
                        statusLabel.setText("Product '" + newProduct.getName() + "' added successfully to " + selectedWarehouse.getName());
                        logger.info("Product '{}' added successfully to warehouse '{}'.", newProduct.getName(), selectedWarehouse.getName());
                    } catch (IllegalArgumentException e) {
                        logger.error("Error saving new product: {}", e.getMessage());
                        showErrorAlert("Save Error", e.getMessage());
                    } catch (Exception e) {
                        logger.error("Could not save new product due to an unexpected error.", e);
                        showErrorAlert("Save Error", "An unexpected error occurred while saving the product: " + e.getMessage());
                    }
                }
            } else {
                logger.debug("Add Product dialog cancelled.");
            }
        } catch (IOException e) {
            logger.error("Failed to load ProductDialog.fxml", e);
            showErrorAlert("Dialog Error", "Could not open the product dialog: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error in handleAddProductAction", e);
            showErrorAlert("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void handleEditProductAction(ActionEvent event) {
        logger.debug("Edit Product action triggered.");
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        Warehouse currentWarehouse = warehouseListView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            showErrorAlert("No Product Selected", "Please select a product to edit.");
            return;
        }
        if (currentWarehouse == null) {
            showErrorAlert("Error", "No current warehouse context for editing product. Please re-select warehouse.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/warehouseinventorysystem/view/ProductDialog.fxml"));
            DialogPane dialogPane = loader.load();

            ProductDialogController dialogController = loader.getController();
            dialogController.setWarehouses(observableWarehouseList);
            dialogController.setProductAndSelectedWarehouse(selectedProduct, selectedProduct.getWarehouse());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Product: " + selectedProduct.getName());
            if (mainBorderPane.getScene() != null && mainBorderPane.getScene().getWindow() != null) {
                dialog.initOwner(mainBorderPane.getScene().getWindow());
            }

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Product editedProduct = dialogController.processResults();
                if (editedProduct != null) {
                    try {
                        productService.saveProduct(editedProduct);
                        Warehouse productOriginalWarehouse = selectedProduct.getWarehouse(); // Simpan referensi sebelum diedit
                        Warehouse productNewWarehouse = editedProduct.getWarehouse();

                        // Jika produk dipindahkan ke gudang lain, kita mungkin perlu refresh gudang lama dan baru.
                        // Untuk kesederhanaan, kita refresh gudang yang sedang dipilih.
                        // Jika gudang yang dipilih adalah gudang lama, produk akan hilang dari daftar itu.
                        // Jika gudang yang dipilih adalah gudang baru, produk akan muncul/diupdate di sana.
                        handleWarehouseSelection(currentWarehouse);

                        if (productOriginalWarehouse != null && productNewWarehouse != null && productOriginalWarehouse.getId() != productNewWarehouse.getId()) {
                            statusLabel.setText("Product '" + editedProduct.getName() + "' updated and moved to " + productNewWarehouse.getName());
                            logger.info("Product '{}' updated and moved to warehouse '{}'.", editedProduct.getName(), productNewWarehouse.getName());
                        } else {
                            statusLabel.setText("Product '" + editedProduct.getName() + "' updated successfully.");
                            logger.info("Product '{}' updated successfully.", editedProduct.getName());
                        }
                    } catch (IllegalArgumentException e) {
                        logger.error("Error updating product: {}", e.getMessage());
                        showErrorAlert("Update Error", e.getMessage());
                    } catch (Exception e) {
                        logger.error("Could not update product due to an unexpected error.", e);
                        showErrorAlert("Update Error", "An unexpected error occurred while updating the product: " + e.getMessage());
                    }
                }
            } else {
                logger.debug("Edit Product dialog cancelled.");
            }
        } catch (IOException e) {
            logger.error("Failed to load ProductDialog.fxml for editing", e);
            showErrorAlert("Dialog Error", "Could not open the product dialog for editing: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error in handleEditProductAction", e);
            showErrorAlert("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteProductAction(ActionEvent event) {
        logger.debug("Delete Product action triggered.");
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        Warehouse currentWarehouse = warehouseListView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            showErrorAlert("No Product Selected", "Please select a product to delete.");
            return;
        }
        if (currentWarehouse == null) {
            showErrorAlert("Error", "No current warehouse context. Please re-select the warehouse.");
            return;
        }

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirm Deletion");
        confirmationDialog.setHeaderText("Delete Product: " + selectedProduct.getName() + " (SKU: " + selectedProduct.getSku() + ")");
        confirmationDialog.setContentText("Are you sure you want to delete this product? This action cannot be undone.");
        if (mainBorderPane.getScene() != null && mainBorderPane.getScene().getWindow() != null) {
            confirmationDialog.initOwner(mainBorderPane.getScene().getWindow());
        }

        Optional<ButtonType> result = confirmationDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                logger.info("User confirmed deletion for product ID: {}, Name: {}", selectedProduct.getId(), selectedProduct.getName());
                productService.deleteProduct(selectedProduct.getId());
                handleWarehouseSelection(currentWarehouse);
                statusLabel.setText("Product '" + selectedProduct.getName() + "' deleted successfully.");
                logger.info("Product '{}' deleted successfully.", selectedProduct.getName());
            } catch (IllegalArgumentException e) {
                logger.error("Error deleting product (not found or other validation): {}", e.getMessage());
                showErrorAlert("Delete Error", e.getMessage());
            } catch (Exception e) {
                logger.error("Could not delete product '" + selectedProduct.getName() + "' due to an unexpected error.", e);
                showErrorAlert("Delete Error", "An unexpected error occurred while deleting the product: " + e.getMessage());
            }
        } else {
            logger.debug("User cancelled product deletion.");
            statusLabel.setText("Product deletion cancelled.");
        }
    }

    @FXML
    void handleImportLegacyProductAction(ActionEvent event) {
        logger.info("Import Legacy Product (Demo) action triggered.");
        statusLabel.setText("Attempting to import legacy product data...");

        LegacyProductDataProvider legacyProvider = new LegacyProductDataProvider();
        ProductAdapter adapter = new ProductAdapter(legacyProvider);
        String legacySkuToImport = "LEGACY-001";
        Product adaptedProduct = adapter.getProduct(legacySkuToImport);

        if (adaptedProduct != null) {
            String message = String.format(
                    "Legacy Product Data Adapted Successfully:\n\n" +
                            "Name: %s\nSKU: %s\nQuantity: %d\nPrice: %.2f\n" +
                            "(Note: Warehouse is not set by this adapter example)",
                    adaptedProduct.getName(), adaptedProduct.getSku(),
                    adaptedProduct.getQuantity(), adaptedProduct.getPrice()
            );
            logger.info(message.replace("\n\n", " ").replace("\n", ", "));

            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Legacy Product Import (Demo)");
            infoAlert.setHeaderText("Adapted Product Details");
            infoAlert.setContentText(message);
            if (mainBorderPane.getScene() != null && mainBorderPane.getScene().getWindow() != null) {
                infoAlert.initOwner(mainBorderPane.getScene().getWindow());
            }
            infoAlert.showAndWait();
            statusLabel.setText("Legacy product '" + adaptedProduct.getName() + "' data displayed.");
        } else {
            String errorMessage = "Could not find or adapt legacy product data for SKU: " + legacySkuToImport;
            logger.warn(errorMessage);
            showErrorAlert("Import Failed", errorMessage);
            statusLabel.setText("Legacy product import failed.");
        }
    }

    @FXML void handleManageWarehousesAction(ActionEvent event) { logger.debug("Manage Warehouses action triggered."); statusLabel.setText("Manage Warehouses clicked (Not implemented yet)."); }
    @FXML void handleManageProductsAction(ActionEvent event) { logger.debug("Manage Products action triggered."); statusLabel.setText("Manage Products clicked (Not implemented yet)."); }
    @FXML void handleAboutAction(ActionEvent event) { logger.debug("About action triggered."); statusLabel.setText("About clicked (Not implemented yet)."); }

    @FXML
    void handleExitAction(ActionEvent event) {
        logger.info("Exit action triggered.");
        Platform.exit();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (mainBorderPane.getScene() != null && mainBorderPane.getScene().getWindow() != null) {
            alert.initOwner(mainBorderPane.getScene().getWindow());
        }
        alert.showAndWait();
    }
}