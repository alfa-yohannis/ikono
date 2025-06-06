// AlertHandler.java
package org.example;

import javafx.scene.control.Alert;

public class AlertHandler {
    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}