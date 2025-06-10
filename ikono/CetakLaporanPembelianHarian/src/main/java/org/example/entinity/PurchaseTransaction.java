// Lokasi: src/main/java/org/example/entity/PurchaseTransaction.java
package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "PurchaseTransactions")
public class PurchaseTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int PurchaseID;
    private LocalDate PurchaseDate;
    private Double TotalAmount;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus PaymentStatus; // Ini sekarang merujuk ke file PaymentStatus.java

    @Enumerated(EnumType.STRING)
    private PaymentMethod PaymentMethod; // Ini sekarang merujuk ke file PaymentMethod.java

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @OneToMany(mappedBy = "purchaseTransaction", fetch = FetchType.EAGER)
    private Set<PurchaseDetail> details;

    // >> DEFINISI ENUM DIHAPUS DARI SINI <<

    // Getters and Setters
    public int getPurchaseID() { return PurchaseID; }
    public void setPurchaseID(int purchaseID) { PurchaseID = purchaseID; }
    public LocalDate getPurchaseDate() { return PurchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { PurchaseDate = purchaseDate; }
    public Double getTotalAmount() { return TotalAmount; }
    public void setTotalAmount(Double totalAmount) { TotalAmount = totalAmount; }
    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }
    public String getVendorName() { return vendor != null ? vendor.getName() : "N/A"; }
    public Set<PurchaseDetail> getDetails() { return details; }
    public void setDetails(Set<PurchaseDetail> details) { this.details = details; }
    public PaymentStatus getPaymentStatus() { return PaymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.PaymentStatus = paymentStatus; }
    public PaymentMethod getPaymentMethod() { return PaymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.PaymentMethod = paymentMethod; }
}
