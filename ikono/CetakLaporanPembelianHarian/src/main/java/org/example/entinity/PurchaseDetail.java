// Lokasi: src/main/java/org/example/entity/PurchaseDetail.java
package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PurchaseDetails")
public class PurchaseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int PurchaseDetailID;

    @ManyToOne
    @JoinColumn(name = "PurchaseID", nullable = false)
    private PurchaseTransaction purchaseTransaction;
    
    @ManyToOne
    @JoinColumn(name = "ProductID", nullable = false)
    private Product product;
    
    private int Quantity;
    private Double UnitPrice;
    private Double Subtotal;

    // Helper untuk menampilkan nama produk di tabel
    public String getProductName() {
        return (product != null) ? product.getProductName() : "N/A";
    }

    // Getters and Setters
    public int getPurchaseDetailID() { return PurchaseDetailID; }
    public void setPurchaseDetailID(int purchaseDetailID) { PurchaseDetailID = purchaseDetailID; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return Quantity; }
    public void setQuantity(int quantity) { Quantity = quantity; }
    public Double getUnitPrice() { return UnitPrice; }
    public void setUnitPrice(Double unitPrice) { UnitPrice = unitPrice; }
    public Double getSubtotal() { return Subtotal; }
    public void setSubtotal(Double subtotal) { Subtotal = subtotal; }
    // ... getter setter lainnya
}
