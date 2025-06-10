package edu.pradita.uas.category.controller;

import edu.pradita.uas.category.model.Category;
import edu.pradita.uas.category.model.CategoryObserver;
import edu.pradita.uas.category.service.CategoryService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller untuk CategoryView.fxml.
 * Bertanggung jawab atas logika UI.
 * Bertindak sebagai Observer untuk perubahan data.
 */
public class CategoryController implements Initializable, CategoryObserver {

    // FXML Components
    @FXML private TextField searchField;
    @FXML private TableView<Category> tableView;
    @FXML private TableColumn<Category, Integer> idColumn;
    @FXML private TableColumn<Category, String> nameColumn;
    @FXML private TableColumn<Category, String> parentNameColumn;
    @FXML private TextField categoryIdField;
    @FXML private TextField categoryNameField;
    @FXML private ComboBox<Category> parentCategoryComboBox;

    private CategoryService categoryService;
    private final ObservableList<Category> categoryList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.categoryService = new CategoryService();
        this.categoryService.addObserver(this); // Daftarkan controller ini sebagai observer

        setupTableColumns();
        setupTableSelectionListener();

        // Muat data awal dari database
        refreshTableData();
    }

    /**
     * Metode ini dipanggil oleh CategoryService setiap kali ada perubahan data.
     * Implementasi dari interface CategoryObserver.
     */
    @Override
    public void onDataChanged() {
        // Operasi UI harus dijalankan di JavaFX Application Thread
        Platform.runLater(this::refreshTableData);
    }

    private void refreshTableData() {
        List<Category> categoriesFromDb = categoryService.getAllCategories();
        categoryList.setAll(categoriesFromDb);
        tableView.setItems(categoryList);

        // Refresh juga ComboBox
        parentCategoryComboBox.setItems(categoryList);

        System.out.println("Table and ComboBox refreshed with data from database.");
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        parentNameColumn.setCellValueFactory(cellData -> {
            Category parent = cellData.getValue().getParent();
            return new SimpleStringProperty(parent != null ? parent.getName() : "-");
        });
    }

    private void setupTableSelectionListener() {
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateForm(newSelection);
                    }
                }
        );
    }

    private void populateForm(Category category) {
        categoryIdField.setText(String.valueOf(category.getId()));
        categoryNameField.setText(category.getName());
        parentCategoryComboBox.setValue(category.getParent());
    }

    @FXML
    private void clearFormAndSelection() {
        tableView.getSelectionModel().clearSelection();
        categoryIdField.clear();
        categoryNameField.clear();
        parentCategoryComboBox.setValue(null);
        searchField.clear();
        tableView.setItems(categoryList);
    }

    @FXML
    private void handleAddCategory() {
        String name = categoryNameField.getText();
        if (name == null || name.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Category name cannot be empty.");
            return;
        }

        Category parent = parentCategoryComboBox.getValue();
        Category newCategory = new Category(name, parent);

        categoryService.saveCategory(newCategory);

        clearFormAndSelection();
        showAlert(Alert.AlertType.INFORMATION, "Success", "New category has been saved to database.");
    }

    @FXML
    private void handleUpdateCategory() {
        Category selectedCategory = tableView.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a category to update.");
            return;
        }

        String newName = categoryNameField.getText();
        Category newParent = parentCategoryComboBox.getValue();

        if (selectedCategory.equals(newParent)) {
            showAlert(Alert.AlertType.ERROR, "Logic Error", "A category cannot be its own parent.");
            return;
        }

        selectedCategory.setName(newName);
        selectedCategory.setParent(newParent);

        categoryService.updateCategory(selectedCategory);

        clearFormAndSelection();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Category has been updated in database.");
    }

    @FXML
    private void handleDeleteCategory() {
        Category selectedCategory = tableView.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a category to delete.");
            return;
        }

        if (categoryService.isCategoryParent(selectedCategory.getId())) {
            showAlert(Alert.AlertType.ERROR, "Deletion Error",
                    "Cannot delete category '" + selectedCategory.getName() + "' because it is a parent to other categories.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete '" + selectedCategory.getName() + "'?",
                ButtonType.YES, ButtonType.NO);
        confirmation.setTitle("Confirm Deletion");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            categoryService.deleteCategory(selectedCategory.getId());
            clearFormAndSelection();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Category deleted successfully from database.");
        }
    }

    @FXML
    private void handleSearchCategory() {
        // Implementasi pencarian bisa dibuat lebih efisien dengan query ke DB,
        // tapi untuk kesederhanaan, kita filter dari list yang ada.
        String keyword = searchField.getText().toLowerCase().trim();
        ObservableList<Category> filteredList = categoryList.filtered(
                category -> category.getName().toLowerCase().contains(keyword)
        );
        tableView.setItems(filteredList);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
