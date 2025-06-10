package com.inventory.warehouseinventorysystem.dao;

import com.inventory.warehouseinventorysystem.model.Product;
import java.util.List;

public interface ProductDao {
    Product findById(int id);
    List<Product> findAll();
    List<Product> findByWarehouseId(int warehouseId);
    void save(Product product);
    void update(Product product);
    void delete(Product product);
    Product findBySku(String sku); // Contoh metode kustom
}