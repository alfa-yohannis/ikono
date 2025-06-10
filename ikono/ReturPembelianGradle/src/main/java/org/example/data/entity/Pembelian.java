package org.example.data.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Entitas yang merepresentasikan tabel 'pembelian' di database.
 */
@Entity
@Table(name = "pembelian")
public class Pembelian {

    @Id
    @Column(name = "id_pembelian", nullable = false, length = 10)
    private String idPembelian;

    // Relasi Many-to-Many ke Product melalui tabel perantara 'detail_pembelian'
    @ManyToMany(fetch = FetchType.EAGER) // EAGER loading untuk memastikan produk selalu dimuat bersama pembelian
    @JoinTable(
            name = "detail_pembelian", // Nama tabel perantara
            joinColumns = @JoinColumn(name = "id_pembelian"), // Foreign key di tabel perantara yang merujuk ke Pembelian
            inverseJoinColumns = @JoinColumn(name = "product_id") // Foreign key di tabel perantara yang merujuk ke Product
    )
    private List<Product> products = new ArrayList<>(); // Inisialisasi untuk menghindari NullPointerException

    // Konstruktor default (dibutuhkan oleh Hibernate)
    public Pembelian() {
    }

    // Konstruktor dengan parameter
    public Pembelian(String idPembelian) {
        this.idPembelian = idPembelian;
    }

    // Getter dan Setter
    public String getIdPembelian() {
        return idPembelian;
    }

    public void setIdPembelian(String idPembelian) {
        this.idPembelian = idPembelian;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    // Override toString untuk kemudahan debugging dan tampilan di ComboBox (jika tidak menggunakan StringConverter)
    @Override
    public String toString() {
        return "Pembelian{" +
               "idPembelian='" + idPembelian + '\'' +
               '}';
    }
}
