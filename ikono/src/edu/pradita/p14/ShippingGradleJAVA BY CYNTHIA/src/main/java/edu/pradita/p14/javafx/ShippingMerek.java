package edu.pradita.p14.javafx;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shippingmerek")
public class ShippingMerek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private double harga;
    private String jenis;

    // --- PERBAIKAN: Tambahkan Constructor dan Getter/Setter ---

    // Diperlukan oleh Hibernate
    public ShippingMerek() {
    }

    // Diperlukan oleh Test Anda
    public ShippingMerek(String name, double harga, String jenis) {
        this.name = name;
        this.harga = harga;
        this.jenis = jenis;
    }

    // Diperlukan oleh Test Anda
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