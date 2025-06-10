// Lokasi: src/main/java/org/example/entity/Product.java
package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductID")
    private Integer ProductID;

    @Column(name = "ProductName")
    private String ProductName;

    @Column(name = "Category")
    private String Category;

    @Column(name = "Stock")
    private Integer Stock;

    @Column(name = "UnitPrice")
    private Double UnitPrice;

    // Constructors
    public Product() {}

    // Getters and Setters
    public Integer getProductID() { return ProductID; }
    public void setProductID(Integer productID) { ProductID = productID; }
    public String getProductName() { return ProductName; }
    public void setProductName(String productName) { ProductName = productName; }
    public String getCategory() { return Category; }
    public void setCategory(String category) { Category = category; }
    public Integer getStock() { return Stock; }
    public void setStock(Integer stock) { Stock = stock; }
    public Double getUnitPrice() { return UnitPrice; }
    public void setUnitPrice(Double unitPrice) { UnitPrice = unitPrice; }
}
