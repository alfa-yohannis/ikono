package com.inventory.warehouseinventorysystem.observer;

import com.inventory.warehouseinventorysystem.model.Product;

public interface InventoryObserver {
    void update(Product product, String actionMessage); // actionMessage bisa "PRODUCT_ADDED", "PRODUCT_UPDATED", dll.
}