package uas.transaksi.data;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "shipments")
public class ShipmentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "shipment_code", length = 50)
    private String shipmentCode;

    // Relasi Many-to-One ke SenderData
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading untuk performa
    @JoinColumn(name = "sender_id", nullable = false) // Nama kolom foreign key
    private SenderData sender;

    // Relasi Many-to-One ke ReceiverData
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private ReceiverData receiver;

    @Column(name = "shipping_date")
    private Date shippingDate;

    @Column(name = "status", length = 20)
    private String status;

    public ShipmentData() {}

    // Konstruktor dengan ID (untuk objek yang diambil dari DB)
    public ShipmentData(int id, String shipmentCode, SenderData sender, ReceiverData receiver, Date shippingDate, String status) {
        this.id = id;
        this.shipmentCode = shipmentCode;
        this.sender = sender;
        this.receiver = receiver;
        this.shippingDate = shippingDate;
        this.status = status;
    }

    // Konstruktor tanpa ID (untuk objek baru yang akan disimpan, ID akan di-generate DB)
    public ShipmentData(String shipmentCode, SenderData sender, ReceiverData receiver, Date shippingDate, String status) {
        this.shipmentCode = shipmentCode;
        this.sender = sender;
        this.receiver = receiver;
        this.shippingDate = shippingDate;
        this.status = status;
    }

    // --- Getters dan Setters (WAJIB ada untuk Hibernate) ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShipmentCode() {
        return shipmentCode;
    }

    public void setShipmentCode(String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    public SenderData getSender() {
        return sender;
    }

    public void setSender(SenderData sender) {
        this.sender = sender;
    }

    public ReceiverData getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverData receiver) {
        this.receiver = receiver;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}