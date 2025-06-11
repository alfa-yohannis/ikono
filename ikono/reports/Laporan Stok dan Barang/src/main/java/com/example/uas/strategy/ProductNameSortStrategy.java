package com.example.uas.strategy;

import com.example.uas.model.Product;

public class ProductNameSortStrategy implements SortStrategy {
    @Override
    public int compare(Product p1, Product p2) {
        return p1.getName().compareTo(p2.getName());
    }
} 