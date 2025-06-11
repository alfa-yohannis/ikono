package com.example.uas.strategy;

import com.example.uas.model.Product;

public class ProductPriceSortStrategy implements SortStrategy {
    @Override
    public int compare(Product p1, Product p2) {
        return Double.compare(p1.getPrice(), p2.getPrice());
    }
} 