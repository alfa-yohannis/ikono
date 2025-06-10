package com.inventory.warehouseinventorysystem.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "warehouses")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "location", length = 255)
    private String location;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    public Warehouse() {
    }

    public Warehouse(String name, String location) {
        this.name = name;
        this.location = location;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    public void addProduct(Product product) {
        products.add(product);
        product.setWarehouse(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.setWarehouse(null);
    }

    @Override
    public String toString() {
        return name + (location != null && !location.isEmpty() ? " (" + location + ")" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        if (id == 0 && warehouse.id == 0) {
            return Objects.equals(name, warehouse.name) &&
                    Objects.equals(location, warehouse.location);
        }
        return id != 0 && id == warehouse.id; // Compare by ID if persisted
    }

    @Override
    public int hashCode() {
        if (id == 0) { // For new, unpersisted entities
            return Objects.hash(name, location);
        }
        return Objects.hash(id); // For persisted entities
    }
}