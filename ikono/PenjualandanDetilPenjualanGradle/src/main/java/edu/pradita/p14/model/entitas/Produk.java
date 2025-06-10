package edu.pradita.p14.model.entitas;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "produk")
public class Produk {

    @Id
    @Column(name = "id", length = 10)
    private String id;

    @Column(name = "nama", length = 100, nullable = false)
    private String nama;

    @Column(name = "harga", nullable = false)
    private double harga;

    // Constructors
    public Produk() {}

    public Produk(String id, String nama, double harga) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }
}
