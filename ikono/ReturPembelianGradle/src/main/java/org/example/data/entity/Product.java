package org.example.data.entity;

import jakarta.persistence.*;

/**
 * Entitas yang merepresentasikan tabel 'products' di database.
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Menggunakan auto-increment dari database
    @Column(name = "product_id")
    private int productId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    // Anda bisa menambahkan field lain seperti harga, stok, dll.
    // @Column(name = "price")
    // private BigDecimal price;

    // Konstruktor default (dibutuhkan oleh Hibernate)
    public Product() {
    }

    // Konstruktor dengan parameter
    public Product(String name) {
        this.name = name;
    }

    // Getter dan Setter
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Override toString untuk kemudahan debugging dan tampilan di ComboBox (jika tidak menggunakan StringConverter)
    @Override
    public String toString() {
        return "Product{" +
               "productId=" + productId +
               ", name='" + name + '\'' +
               '}';
    }
}