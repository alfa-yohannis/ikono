package org.example.data.entity;

import jakarta.persistence.*;

/**
 * Entitas yang merepresentasikan tabel 'retur_pembelian' di database.
 */
@Entity
@Table(name = "retur_pembelian")
public class ReturPembelian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Menggunakan auto-increment dari database
    @Column(name = "id_retur")
    private int idRetur;

    // Relasi Many-to-One ke Pembelian
    @ManyToOne(fetch = FetchType.LAZY) // LAZY loading biasanya lebih baik untuk performa
    @JoinColumn(name = "id_pembelian", nullable = false)
    private Pembelian pembelian;

    // Relasi Many-to-One ke Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "jumlah", nullable = false)
    private int jumlah;

    @Column(name = "alasan_retur", nullable = false, columnDefinition = "TEXT")
    private String alasanRetur;

    // Konstruktor default (dibutuhkan oleh Hibernate)
    public ReturPembelian() {
    }

    // Getter dan Setter
    public int getIdRetur() {
        return idRetur;
    }

    public void setIdRetur(int idRetur) {
        this.idRetur = idRetur;
    }

    public Pembelian getPembelian() {
        return pembelian;
    }

    public void setPembelian(Pembelian pembelian) {
        this.pembelian = pembelian;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getAlasanRetur() {
        return alasanRetur;
    }

    public void setAlasanRetur(String alasanRetur) {
        this.alasanRetur = alasanRetur;
    }

    @Override
    public String toString() {
        return "ReturPembelian{" +
               "idRetur=" + idRetur +
               ", pembelianId=" + (pembelian != null ? pembelian.getIdPembelian() : "null") +
               ", productId=" + (product != null ? product.getProductId() : "null") +
               ", jumlah=" + jumlah +
               ", alasanRetur='" + alasanRetur + '\'' +
               '}';
    }
}