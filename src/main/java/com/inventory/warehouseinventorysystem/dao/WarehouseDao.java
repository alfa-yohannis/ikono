package com.inventory.warehouseinventorysystem.dao;

import com.inventory.warehouseinventorysystem.model.Warehouse;
import java.util.List;

public interface WarehouseDao {
    Warehouse findById(int id);
    List<Warehouse> findAll();
    void save(Warehouse warehouse);
    void update(Warehouse warehouse);
    void deleteById(int id);
    Warehouse findByName(String name);
}