package com.example.uas.strategy;

import com.example.uas.model.Product;

public class ProductIdSortStrategy implements SortStrategy {
    @Override
    public int compare(Product p1, Product p2) {
        return Integer.compare(p1.getProductId(), p2.getProductId());
    }
} 