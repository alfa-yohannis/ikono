package edu.pradita.p14.javafx.controller;

// PERBAIKAN DI SINI: Tambahkan import untuk SalesOrderDetail
import edu.pradita.p14.javafx.entity.SalesOrderDetail;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Sebaiknya kelas ini juga dipindahkan ke package 'entity' agar konsisten
@Entity
@Table(name = "sales_order")
public class SalesOrder {
    @Id
    @Column(name = "code")
    private String code;

    @Temporal(TemporalType.DATE)
    @Column(name = "order_date")
    private Date date;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SalesOrderDetail> orderDetails = new ArrayList<>();

    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public List<SalesOrderDetail> getOrderDetails() { return orderDetails; }
    public void setOrderDetails(List<SalesOrderDetail> orderDetails) { this.orderDetails = orderDetails; }
}