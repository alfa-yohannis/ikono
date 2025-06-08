package org.example.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity // Menandakan kelas ini adalah entitas JPA
@Table(name = "returns_sales") // Menentukan tabel database yang dipetakan
public class SalesReturn implements Serializable {

    @Id // Menandakan ini adalah primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Menggunakan auto-increment dari database
    private int id;

    @Column(name = "rma_number", length = 50)
    private String rmaNumber;

    @Column(name = "receipt_number", length = 50)
    private String receiptNumber;

    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Column(name = "item_id", length = 50)
    private String itemId;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "return_reason", length = 100)
    private String returnReason;

    @Column(name = "refund_method", length = 50)
    private String refundMethod;

    @Lob // Untuk kolom TEXT yang lebih besar
    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    // Konstruktor default (diperlukan oleh Hibernate)
    public SalesReturn() {
    }

    // Konstruktor dengan parameter (opsional, bisa berguna)
    public SalesReturn(String rmaNumber, String receiptNumber, String customerName, String itemId, String description, int quantity, String returnReason, String refundMethod, String comments) {
        this.rmaNumber = rmaNumber;
        this.receiptNumber = receiptNumber;
        this.customerName = customerName;
        this.itemId = itemId;
        this.description = description;
        this.quantity = quantity;
        this.returnReason = returnReason;
        this.refundMethod = refundMethod;
        this.comments = comments;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRmaNumber() {
        return rmaNumber;
    }

    public void setRmaNumber(String rmaNumber) {
        this.rmaNumber = rmaNumber;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getRefundMethod() {
        return refundMethod;
    }

    public void setRefundMethod(String refundMethod) {
        this.refundMethod = refundMethod;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "SalesReturn{" +
                "id=" + id +
                ", rmaNumber='" + rmaNumber + '\'' +
                ", receiptNumber='" + receiptNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", itemId='" + itemId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
