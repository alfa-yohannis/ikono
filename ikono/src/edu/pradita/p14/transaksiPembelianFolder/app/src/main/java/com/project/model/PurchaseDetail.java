package com.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PurchaseDetails")
public class PurchaseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int purchaseDetailID;

    private int productID;
    private int quantity;
    private double unitPrice;
    private double subtotal;

    @ManyToOne
    @JoinColumn(name = "PurchaseID")
    private PurchaseTransaction transaction;

    // === GETTERS & SETTERS ===

    public int getPurchaseDetailID() {
        return purchaseDetailID;
    }

    public void setPurchaseDetailID(int purchaseDetailID) {
        this.purchaseDetailID = purchaseDetailID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public PurchaseTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(PurchaseTransaction transaction) {
        this.transaction = transaction;
    }
}
