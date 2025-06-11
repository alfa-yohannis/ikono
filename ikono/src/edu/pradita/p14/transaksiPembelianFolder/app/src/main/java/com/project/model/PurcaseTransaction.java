package com.project.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "PurchaseTransactions")
public class PurchaseTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int purchaseID;

    private String purchaseDate;
    private double totalAmount;
    private String paymentStatus;
    private String paymentMethod;
    private int vendorID;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseDetail> details = new ArrayList<>();
}
