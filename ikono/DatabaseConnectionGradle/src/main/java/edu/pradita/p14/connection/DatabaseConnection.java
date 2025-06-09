package edu.pradita.p14.connection;

import edu.pradita.p14.util.HibernateUtil;
import edu.pradita.p14.util.strategy.DatabaseStrategy;
import edu.pradita.p14.util.strategy.MySqlStrategy;
import edu.pradita.p14.util.strategy.PostgreSqlStrategy;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DatabaseConnection {

    @FXML private ComboBox<DatabaseStrategy> dbTypeComboBox;
    @FXML private TextField hostField; //
    @FXML private TextField portField; //
    @FXML private TextField usernameField; //
    @FXML private PasswordField passwordField; //
    @FXML private TextField databaseField; //
    @FXML private Label infoLabel; //
    // fx:id connectButton tidak digunakan di kode controller ini, tapi ada di FXML
    // @FXML private Button connectButton;

    @FXML
    public void initialize() {
        dbTypeComboBox.setItems(FXCollections.observableArrayList(
                new MySqlStrategy(),
                new PostgreSqlStrategy()
        ));
        dbTypeComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void connectToDatabase(MouseEvent event) { //
        String host = hostField.getText(); //
        String port = portField.getText(); //
        String username = usernameField.getText(); //
        String password = passwordField.getText(); //
        String database = databaseField.getText(); //
        DatabaseStrategy selectedStrategy = dbTypeComboBox.getValue();

        if (selectedStrategy == null) {
            setInfoLabel("Pilih tipe database terlebih dahulu.", "red");
            return;
        }

        HibernateUtil.shutdown(); // Selalu tutup factory lama sebelum buat baru

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory(
                    selectedStrategy, host, port, database, username, password);

            try (Session session = sessionFactory.openSession()) {
                if (session.isOpen()) {
                    setInfoLabel("Koneksi Berhasil!", "green");
                } else {
                    throw new Exception("Sesi gagal dibuka.");
                }
            }
        } catch (Exception e) {
            setInfoLabel("Koneksi Gagal: " + e.getMessage(), "red");
            // e.printStackTrace(); // Baik untuk debugging saat pengembangan
        }
    }

    private void setInfoLabel(String message, String color) {
        infoLabel.setText(message); //
        infoLabel.setStyle("-fx-text-fill: " + color + ";"); //
        infoLabel.setVisible(true); //
    }
}