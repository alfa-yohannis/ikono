package com.inventory.warehouseinventorysystem.observer;

import com.inventory.warehouseinventorysystem.model.Product;

public interface InventorySubject {
    void registerObserver(InventoryObserver observer);
    void removeObserver(InventoryObserver observer);
    void notifyObservers(Product product, String actionMessage);
}