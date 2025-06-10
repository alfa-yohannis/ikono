package com.inventory.warehouseinventorysystem.observer;

import com.inventory.warehouseinventorysystem.model.Product;
import java.util.ArrayList;
import java.util.List;
import com.inventory.warehouseinventorysystem.observer.ProductEventManager;

public class ProductEventManager implements InventorySubject {
    private static ProductEventManager instance; // Untuk Singleton
    private final List<InventoryObserver> observers;

    // Private constructor untuk mencegah instansiasi langsung (Singleton)
    private ProductEventManager() {
        observers = new ArrayList<>();
    }

    // Metode static untuk mendapatkan instance Singleton
    public static synchronized ProductEventManager getInstance() {
        if (instance == null) {
            instance = new ProductEventManager();
        }
        return instance;
    }

    @Override
    public void registerObserver(InventoryObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(InventoryObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Product product, String actionMessage) {
        // Iterasi pada salinan list untuk menghindari ConcurrentModificationException
        // jika observer mencoba memodifikasi list saat sedang diiterasi (misalnya, removeObserver).
        List<InventoryObserver> observersToNotify = new ArrayList<>(this.observers);
        for (InventoryObserver observer : observersToNotify) {
            observer.update(product, actionMessage);
        }
    }

    // Metode spesifik untuk memicu notifikasi dari ProductService
    public void productAdded(Product product) {
        notifyObservers(product, "PRODUCT_ADDED");
    }

    public void productUpdated(Product product) {
        notifyObservers(product, "PRODUCT_UPDATED");
    }

    public void productDeleted(Product product) {
        // Untuk produk yang dihapus, informasi warehouse mungkin penting sebelum di-null kan
        // atau jika produknya sendiri yang diteruskan.
        notifyObservers(product, "PRODUCT_DELETED");
    }
}