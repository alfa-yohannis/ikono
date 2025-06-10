package com.inventory.warehouseinventorysystem.dao;

public interface DaoFactory {
    WarehouseDao getWarehouseDao();
    ProductDao getProductDao();
}