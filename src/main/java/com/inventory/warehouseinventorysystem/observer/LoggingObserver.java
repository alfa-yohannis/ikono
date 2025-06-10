package com.inventory.warehouseinventorysystem.observer;

import com.inventory.warehouseinventorysystem.model.Product;

public class LoggingObserver implements InventoryObserver {

    @Override
    public void update(Product product, String actionMessage) {
        // Mencetak log sederhana ke konsol
        // Di aplikasi nyata, ini bisa menulis ke file log, mengirim ke sistem logging, dll.
        System.out.println("[LOG_OBSERVER] Action: " + actionMessage +
                " | Product ID: " + (product != null ? product.getId() : "N/A") +
                ", Name: " + (product != null ? product.getName() : "N/A") +
                ", SKU: " + (product != null ? product.getSku() : "N/A"));
    }
}