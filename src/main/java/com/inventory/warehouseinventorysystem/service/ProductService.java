package com.inventory.warehouseinventorysystem.service;

import com.inventory.warehouseinventorysystem.dao.DaoFactory;
import com.inventory.warehouseinventorysystem.dao.ProductDao;
import com.inventory.warehouseinventorysystem.model.Product;
// import com.inventory.warehouseinventorysystem.model.Warehouse; // Tidak digunakan secara langsung di sini, bisa dihapus jika tidak ada penggunaan lain
import com.inventory.warehouseinventorysystem.observer.ProductEventManager;

// Import untuk SLF4J Logger (jika Anda ingin menambahkannya di sini juga)
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class); // Opsional: logger untuk service
    private final ProductDao productDao;
    private final ProductEventManager eventManager;

    // CONSTRUCTOR DIMODIFIKASI: Menerima ProductEventManager
    public ProductService(DaoFactory daoFactory, ProductEventManager eventManager) {
        this.productDao = daoFactory.getProductDao();
        this.eventManager = eventManager; // Gunakan instance yang di-inject
    }

    public Product getProductById(int id) {
        logger.debug("Fetching product by ID: {}", id);
        return productDao.findById(id);
    }

    public List<Product> getAllProducts() {
        logger.debug("Fetching all products.");
        return productDao.findAll();
    }

    public List<Product> getProductsByWarehouseId(int warehouseId) {
        logger.debug("Fetching products for warehouse ID: {}", warehouseId);
        return productDao.findByWarehouseId(warehouseId);
    }

    public Product getProductBySku(String sku) {
        logger.debug("Fetching product by SKU: {}", sku);
        return productDao.findBySku(sku);
    }

    public void saveProduct(Product product) {
        logger.info("Attempting to save/update product: {}", product.getName());
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            logger.error("Validation failed: Product name cannot be empty.");
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (product.getSku() == null || product.getSku().trim().isEmpty()) {
            logger.error("Validation failed: Product SKU cannot be empty.");
            throw new IllegalArgumentException("Product SKU cannot be empty.");
        }
        if (product.getWarehouse() == null) {
            logger.error("Validation failed: Product must be assigned to a warehouse.");
            throw new IllegalArgumentException("Product must be assigned to a warehouse.");
        }
        if (product.getPrice() == null) { // Tambahan validasi harga
            logger.error("Validation failed: Product price cannot be null.");
            throw new IllegalArgumentException("Product price cannot be null.");
        }


        Product existingProductWithSku = productDao.findBySku(product.getSku());
        boolean isNewProduct = (product.getId() == 0);

        if (existingProductWithSku != null && (isNewProduct || existingProductWithSku.getId() != product.getId())) {
            logger.error("Validation failed: Product with SKU '{}' already exists.", product.getSku());
            throw new IllegalArgumentException("Product with SKU '" + product.getSku() + "' already exists.");
        }

        if (isNewProduct) {
            productDao.save(product);
            logger.info("New product saved: {}", product.getName());
            eventManager.productAdded(product);
        } else {
            productDao.update(product);
            logger.info("Existing product updated: {}", product.getName());
            eventManager.productUpdated(product);
        }
    }

    public void deleteProduct(int productId) {
        logger.info("Attempting to delete product with ID: {}", productId);
        Product product = productDao.findById(productId);
        if (product != null) {
            productDao.delete(product); // DAO akan menghapus objek Product
            logger.info("Product deleted: {}", product.getName());
            eventManager.productDeleted(product);
        } else {
            logger.warn("Product with ID {} not found for deletion.", productId);
            throw new IllegalArgumentException("Product with id " + productId + " not found.");
        }
    }
}