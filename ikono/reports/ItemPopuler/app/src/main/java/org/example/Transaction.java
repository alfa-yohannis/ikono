// Transaction.java
package org.example;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;

    // Many-to-One relationship with Product
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading Product details
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // Ini akan menjadi objek Product, bukan hanya ID

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "transaction_date")
    private LocalDate transactionDate; // Gunakan LocalDate untuk DATE SQL Type

    // Constructors
    public Transaction() {
    }

    public Transaction(Product product, int quantity, LocalDate transactionDate) {
        this.product = product;
        this.quantity = quantity;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}