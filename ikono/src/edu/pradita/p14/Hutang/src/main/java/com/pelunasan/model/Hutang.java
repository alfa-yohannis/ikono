package com.pelunasan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "hutang")
public class Hutang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @Column(nullable = false)
    private double jumlah;

    @Column(nullable = false)
    private boolean lunas;

    public Hutang() {}

    public Hutang(String nama, double jumlah) {
        this.nama = nama;
        this.jumlah = jumlah;
        this.lunas = false;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getJumlah() { return jumlah; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }

    public boolean isLunas() { return lunas; }
    public void setLunas(boolean lunas) { this.lunas = lunas; }
}
