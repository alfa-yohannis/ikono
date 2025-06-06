package uas.transaksi.data;

import jakarta.persistence.*;

@Entity
@Table(name = "shippingmerek")
public class ShippingMerekData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID di-generate otomatis oleh database
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "harga", nullable = false)
    private double harga;

    @Column(name = "jenis", nullable = false, length = 50)
    private String jenis;

    public ShippingMerekData() {}

    // Konstruktor dengan ID (untuk objek yang diambil dari DB)
    public ShippingMerekData(int id, String name, double harga, String jenis) {
        this.id = id;
        this.name = name;
        this.harga = harga;
        this.jenis = jenis;
    }

    // Konstruktor tanpa ID (untuk objek baru yang akan disimpan, ID akan di-generate DB)
    public ShippingMerekData(String name, double harga, String jenis) {
        this.name = name;
        this.harga = harga;
        this.jenis = jenis;
    }

    // --- Getters dan Setters (WAJIB ada untuk Hibernate) ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}