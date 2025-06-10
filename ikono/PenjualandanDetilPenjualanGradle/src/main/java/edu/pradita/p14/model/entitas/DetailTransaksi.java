package edu.pradita.p14.model.entitas;

import jakarta.persistence.*;

@Entity
@Table(name = "detail_transaksi")
public class DetailTransaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "transaksi_id", nullable = false)
    private Transaksi transaksi;

    @ManyToOne
    @JoinColumn(name = "produk_id", nullable = false)
    private Produk produk;

    @Column(name = "jumlah", nullable = false)
    private int jumlah;

    @Column(name = "harga_saat_transaksi", nullable = false)
    private double hargaSaatTransaksi;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public Transaksi getTransaksi() { return transaksi; }
    public void setTransaksi(Transaksi transaksi) { this.transaksi = transaksi; }
    public Produk getProduk() { return produk; }
    public void setProduk(Produk produk) { this.produk = produk; }
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public double getHargaSaatTransaksi() { return hargaSaatTransaksi; }
    public void setHargaSaatTransaksi(double hargaSaatTransaksi) { this.hargaSaatTransaksi = hargaSaatTransaksi; }
}
