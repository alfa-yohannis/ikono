package com.example.uas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "supplier_id")
    private int supplierId;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "stock")
    private int stock;

    // Default constructor for Hibernate
    public Product() {
    }

    private Product(Builder builder) {
        this.productId = builder.productId;
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.supplierId = builder.supplierId;
        this.createdAt = builder.createdAt;
        this.stock = builder.stock;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    // Builder Class
    public static class Builder {
        private int productId;
        private String name;
        private String description;
        private double price;
        private int supplierId;
        private String createdAt;
        private int stock;

        public Builder productId(int productId) {
            this.productId = productId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder supplierId(int supplierId) {
            this.supplierId = supplierId;
            return this;
        }

        public Builder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder stock(int stock) {
            this.stock = stock;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
} 