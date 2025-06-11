package com.example.uas.service;

import java.util.List;

import com.example.uas.dao.ProductDAO;
import com.example.uas.model.Product;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
} 