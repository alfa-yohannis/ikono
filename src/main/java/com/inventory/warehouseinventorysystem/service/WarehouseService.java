package com.inventory.warehouseinventorysystem.service;

import com.inventory.warehouseinventorysystem.dao.DaoFactory;
import com.inventory.warehouseinventorysystem.dao.WarehouseDao;
import com.inventory.warehouseinventorysystem.model.Warehouse;

// Import untuk SLF4J Logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WarehouseService {

    // Deklarasi SLF4J Logger
    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);
    private final WarehouseDao warehouseDao;

    public WarehouseService(DaoFactory daoFactory) {
        this.warehouseDao = daoFactory.getWarehouseDao();
    }

    public Warehouse getWarehouseById(int id) {
        logger.debug("Fetching warehouse by ID: {}", id);
        return warehouseDao.findById(id);
    }

    public List<Warehouse> getAllWarehouses() {
        logger.debug("Fetching all warehouses.");
        return warehouseDao.findAll();
    }

    public void saveWarehouse(Warehouse warehouse) {
        logger.info("Attempting to save/update warehouse: {}", warehouse.getName());
        if (warehouse.getName() == null || warehouse.getName().trim().isEmpty()) {
            logger.error("Validation failed: Warehouse name cannot be empty.");
            throw new IllegalArgumentException("Warehouse name cannot be empty.");
        }

        Warehouse existingWarehouse = warehouseDao.findByName(warehouse.getName());
        if (existingWarehouse != null && existingWarehouse.getId() != warehouse.getId()) {
            logger.error("Validation failed: Warehouse with name '{}' already exists.", warehouse.getName());
            throw new IllegalArgumentException("Warehouse with name '" + warehouse.getName() + "' already exists.");
        }

        if (warehouse.getId() == 0) {
            warehouseDao.save(warehouse);
            logger.info("New warehouse saved: {}", warehouse.getName());
        } else {
            warehouseDao.update(warehouse);
            logger.info("Existing warehouse updated: {}", warehouse.getName());
        }
    }

    public void deleteWarehouse(int warehouseId) {
        logger.info("Attempting to delete warehouse with ID: {}", warehouseId);
        // Logika bisnis tambahan sebelum delete (jika ada) bisa ditaruh di sini.
        // Pemeriksaan apakah gudang ada atau tidak dan apakah memiliki produk,
        // sekarang ditangani atau di-log di dalam DAO deleteById.
        try {
            warehouseDao.deleteById(warehouseId); // Memanggil metode DAO yang baru
            logger.info("Successfully requested deletion for warehouse ID: {}", warehouseId);
        } catch (RuntimeException e) {
            logger.error("Service layer error during warehouse deletion for ID: {}. Error: {}", warehouseId, e.getMessage());
            // Melempar kembali exception agar controller bisa menangani dan menampilkan pesan ke pengguna
            throw new RuntimeException("Failed to delete warehouse. " + e.getMessage(), e);
        }
    }

    public Warehouse getWarehouseByName(String name) {
        logger.debug("Fetching warehouse by name: {}", name);
        return warehouseDao.findByName(name);
    }
}