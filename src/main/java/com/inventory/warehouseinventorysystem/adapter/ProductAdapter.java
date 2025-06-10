package com.inventory.warehouseinventorysystem.adapter;

import com.inventory.warehouseinventorysystem.model.Product; // Pastikan import model.Product benar
import java.math.BigDecimal;
import java.util.Map;

public class ProductAdapter {
    private LegacyProductDataProvider legacyProvider;

    public ProductAdapter(LegacyProductDataProvider legacyProvider) {
        this.legacyProvider = legacyProvider;
    }

    public Product getProduct(String legacySku) { // Pastikan metode ini ada
        Map<String, Object> legacyData = legacyProvider.getLegacyProductData(legacySku);
        if (legacyData == null) {
            return null;
        }

        Product product = new Product();
        product.setName((String) legacyData.get("productName"));
        product.setSku((String) legacyData.get("itemCode"));
        product.setQuantity((Integer) legacyData.get("stockCount"));
        try {
            product.setPrice(new BigDecimal((String) legacyData.get("unitPrice")));
        } catch (NumberFormatException e) {
            // Anda bisa menggunakan logger di sini jika sudah ada
            System.err.println("Error parsing price for legacy product: " + legacyData.get("productName") + " - " + legacyData.get("unitPrice"));
            product.setPrice(BigDecimal.ZERO);
        }
        // Warehouse tidak di-set oleh adapter ini
        return product;
    }
}