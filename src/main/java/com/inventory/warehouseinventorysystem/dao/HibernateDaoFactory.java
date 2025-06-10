package com.inventory.warehouseinventorysystem.dao;

import com.inventory.warehouseinventorysystem.dao.impl.ProductDaoImpl;
import com.inventory.warehouseinventorysystem.dao.impl.WarehouseDaoImpl;

// Concrete Factory
public class HibernateDaoFactory implements DaoFactory {

    @Override
    public WarehouseDao getWarehouseDao() {
        return new WarehouseDaoImpl();
    }

    @Override
    public ProductDao getProductDao() {
        return new ProductDaoImpl();
    }
}