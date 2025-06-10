package com.inventory.warehouseinventorysystem.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "sku", nullable = false, unique = true, length = 100)
    private String sku;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    public Product() {
    }

    public Product(String name, String sku, int quantity, BigDecimal price, Warehouse warehouse) {
        this.name = name;
        this.sku = sku;
        this.quantity = quantity;
        this.price = price;
        this.warehouse = warehouse;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }

    @Override
    public String toString() {
        return name + " (SKU: " + sku + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        if (id == 0 && product.id == 0) { // For new, unpersisted entities
            return Objects.equals(sku, product.sku); // SKU should be unique
        }
        return id != 0 && id == product.id; // Compare by ID if persisted
    }

    @Override
    public int hashCode() {
        if (id == 0) { // For new, unpersisted entities
            return Objects.hash(sku);
        }
        return Objects.hash(id); // For persisted entities
    }
}