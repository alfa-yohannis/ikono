package edu.pradita.p14.javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class ShippingMerekApp extends Application {

    private TableView<ShippingMerek> table = new TableView<>();
    private TextField searchField = new TextField();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Shipping Merek Data");

        // Create columns for the TableView
        TableColumn<ShippingMerek, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<ShippingMerek, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<ShippingMerek, Double> hargaColumn = new TableColumn<>("Harga");
        hargaColumn.setCellValueFactory(new PropertyValueFactory<>("harga"));

        TableColumn<ShippingMerek, String> jenisColumn = new TableColumn<>("Jenis");
        jenisColumn.setCellValueFactory(new PropertyValueFactory<>("jenis"));

        table.getColumns().addAll(idColumn, nameColumn, hargaColumn, jenisColumn);

        loadDataFromDatabase("");

        // Create search field and button
        searchField.setPromptText("Search by name...");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> loadDataFromDatabase(searchField.getText()));

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addDataToDatabase());

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateSelectedData());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteSelectedData());

        // Create layout for controls
        HBox controlBox = new HBox(10, searchField, searchButton, addButton, updateButton, deleteButton);
        controlBox.setPadding(new Insets(10));

        VBox tableBox = new VBox(10, table);
        tableBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(controlBox);
        root.setCenter(tableBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadDataFromDatabase(String keyword) {
        String url = "jdbc:mysql://localhost:3306/pradita";
        String username = "alfa";
        String password = "1234";

        String query = "SELECT * FROM shippingmerek";
        if (keyword != null && !keyword.isEmpty()) {
            query += " WHERE name LIKE ?";
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            if (keyword != null && !keyword.isEmpty()) {
                statement.setString(1, "%" + keyword + "%");
            }

            ResultSet resultSet = statement.executeQuery();

            table.getItems().clear();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double harga = resultSet.getDouble("harga");
                String jenis = resultSet.getString("jenis");

                ShippingMerek shippingMerek = new ShippingMerek(id, name, harga, jenis);
                table.getItems().add(shippingMerek);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addDataToDatabase() {
        Dialog<ShippingMerek> dialog = createShippingMerekDialog("Add Shipping Merek");
        dialog.showAndWait().ifPresent(data -> {
            String url = "jdbc:mysql://localhost:3306/shippingmerek";
            String username = "root";
            String password = "1234";

            String query = "INSERT INTO shippingmerek (name, harga, jenis) VALUES (?, ?, ?)";

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, data.getName());
                statement.setDouble(2, data.getHarga());
                statement.setString(3, data.getJenis());
                statement.executeUpdate();
                loadDataFromDatabase("");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateSelectedData() {
        ShippingMerek selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select an item to update.");
            return;
        }

        Dialog<ShippingMerek> dialog = createShippingMerekDialog("Update Shipping Merek", selected);
        dialog.showAndWait().ifPresent(data -> {
            String url = "jdbc:mysql://localhost:3306/shippingmerek";
            String username = "root";
            String password = "1234";

            String query = "UPDATE shippingmerek SET name = ?, harga = ?, jenis = ? WHERE id = ?";

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, data.getName());
                statement.setDouble(2, data.getHarga());
                statement.setString(3, data.getJenis());
                statement.setInt(4, selected.getId());
                statement.executeUpdate();
                loadDataFromDatabase("");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void deleteSelectedData() {
        ShippingMerek selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select an item to delete.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this item?", ButtonType.YES, ButtonType.NO);
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                String url = "jdbc:mysql://localhost:3306/shippingmerek";
                String username = "root";
                String password = "1234";

                String query = "DELETE FROM shippingmerek WHERE id = ?";

                try (Connection connection = DriverManager.getConnection(url, username, password);
                     PreparedStatement statement = connection.prepareStatement(query)) {

                    statement.setInt(1, selected.getId());
                    statement.executeUpdate();
                    loadDataFromDatabase("");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Dialog<ShippingMerek> createShippingMerekDialog(String title) {
        return createShippingMerekDialog(title, null);
    }

    private Dialog<ShippingMerek> createShippingMerekDialog(String title, ShippingMerek data) {
        Dialog<ShippingMerek> dialog = new Dialog<>();
        dialog.setTitle(title);

        TextField nameField = new TextField(data != null ? data.getName() : "");
        TextField hargaField = new TextField(data != null ? String.valueOf(data.getHarga()) : "");
        TextField jenisField = new TextField(data != null ? data.getJenis() : "");

        VBox dialogContent = new VBox(10, new Label("Name"), nameField, new Label("Harga"), hargaField, new Label("Jenis"), jenisField);
        dialogContent.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String name = nameField.getText();
                String hargaText = hargaField.getText();
                String jenis = jenisField.getText();

                if (name.isEmpty() || hargaText.isEmpty() || jenis.isEmpty()) {
                    showAlert("Input Error", "All fields are required.");
                    return null;
                }

                try {
                    double harga = Double.parseDouble(hargaText);
                    return new ShippingMerek(data != null ? data.getId() : 0, name, harga, jenis);
                } catch (NumberFormatException e) {
                    showAlert("Input Error", "Harga must be a valid number.");
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static class ShippingMerek {
        private final int id;
        private final String name;
        private final double harga;
        private final String jenis;

        public ShippingMerek(int id, String name, double harga, String jenis) {
            this.id = id;
            this.name = name;
            this.harga = harga;
            this.jenis = jenis;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getHarga() {
            return harga;
        }

        public String getJenis() {
            return jenis;
        }
    }
}
