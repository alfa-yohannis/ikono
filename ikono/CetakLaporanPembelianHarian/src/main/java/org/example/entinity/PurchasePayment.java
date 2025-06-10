// Lokasi: src/main/java/org/example/entity/PurchasePayment.java
package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "PurchasePayments")
public class PurchasePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int PaymentID;

    @ManyToOne
    @JoinColumn(name = "PurchaseID", nullable = false)
    private PurchaseTransaction purchaseTransaction;

    private LocalDate PaymentDate;
    private Double AmountPaid;

    @Enumerated(EnumType.STRING)
    private PaymentMethod PaymentMethod; // Gunakan enum yang sudah dibuat

    // Getters and Setters
    public int getPaymentID() { return PaymentID; }
    public void setPaymentID(int paymentID) { PaymentID = paymentID; }
    public PurchaseTransaction getPurchaseTransaction() { return purchaseTransaction; }
    public void setPurchaseTransaction(PurchaseTransaction purchaseTransaction) { this.purchaseTransaction = purchaseTransaction; }
    public LocalDate getPaymentDate() { return PaymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { PaymentDate = paymentDate; }
    public Double getAmountPaid() { return AmountPaid; }
    public void setAmountPaid(Double amountPaid) { AmountPaid = amountPaid; }
    public PaymentMethod getPaymentMethod() { return PaymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.PaymentMethod = paymentMethod; }
}
