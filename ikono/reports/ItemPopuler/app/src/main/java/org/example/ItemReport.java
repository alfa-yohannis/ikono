// ItemReport.java
package org.example;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ItemReport {
    private final SimpleStringProperty productName;
    private final SimpleIntegerProperty quantity;

    public ItemReport(String productName, int quantity) {
        this.productName = new SimpleStringProperty(productName);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }
}