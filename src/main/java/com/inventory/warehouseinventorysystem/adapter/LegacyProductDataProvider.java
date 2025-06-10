package com.inventory.warehouseinventorysystem.adapter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class LegacyProductDataProvider {
    public Map<String, Object> getLegacyProductData(String legacySku) {
        if ("LEGACY-001".equals(legacySku)) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("productName", "Produk Lama A dari Legacy");
            productData.put("itemCode", "LEGACY-001");
            productData.put("stockCount", 75);
            productData.put("unitPrice", "125.50"); // Harga sebagai String
            return productData;
        }
        return null;
    }
}