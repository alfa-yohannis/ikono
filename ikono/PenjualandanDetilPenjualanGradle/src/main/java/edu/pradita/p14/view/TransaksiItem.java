package edu.pradita.p14.view;

import edu.pradita.p14.model.entitas.Produk;
import edu.pradita.p14.model.util.HargaStrategy;

/**
 * Kelas ini bertindak sebagai Adapter (atau Data Transfer Object).
 * Ia mengadaptasi objek 'Produk' dari model menjadi format yang
 * siap ditampilkan di TableView pada UI.
 */
public class TransaksiItem {
    private String kode;
    private String namaBarang;
    private int jumlah;
    private double hargaSatuan;
    private double subtotal;

    /**
     * Constructor yang berfungsi sebagai adapter.
     * @param produk Objek Produk asli dari database.
     * @param jumlah Jumlah barang yang dibeli.
     * @param strategy Algoritma (strategi) untuk menghitung harga.
     */
    public TransaksiItem(Produk produk, int jumlah, HargaStrategy strategy) {
        this.kode = produk.getId();
        this.namaBarang = produk.getNama();
        this.jumlah = jumlah;
        this.hargaSatuan = produk.getHarga();
        // Menggunakan strategy yang diberikan untuk menghitung subtotal
        this.subtotal = strategy.hitungHarga(produk, jumlah);
    }

    // --- GETTERS ---
    // Method getter ini sangat penting karena akan dipanggil oleh
    // PropertyValueFactory di JavaFX untuk mengisi data ke setiap kolom tabel.

    public String getKode() {
        return kode;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public int getJumlah() {
        return jumlah;
    }

    public double getHargaSatuan() {
        return hargaSatuan;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
