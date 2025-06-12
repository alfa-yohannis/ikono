package KoreksiStokOutput;

import javax.persistence.*;

@Entity
@Table(name = "stok")
public class Barang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_barang")
    private int idBarang;

    @Column(name = "nama_barang")
    private String namaBarang;

    private int stok;
    private String satuan;

    // Constructor kosong (WAJIB ADA untuk Hibernate)
    public Barang() {}

    // =====================================================================
    // TAMBAHKAN CONSTRUCTOR INI KEMBALI
    // Constructor untuk membuat objek baru dengan mudah di kode kita
    // =====================================================================
    public Barang(int idBarang, String namaBarang, int stok, String satuan) {
        this.idBarang = idBarang;
        this.namaBarang = namaBarang;
        this.stok = stok;
        this.satuan = satuan;
    }

    // Getters dan Setters (tidak perlu diubah)
    public int getIdBarang() { return idBarang; }
    public void setIdBarang(int idBarang) { this.idBarang = idBarang; }
    public String getNamaBarang() { return namaBarang; }
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }

    @Override
    public String toString() {
        return this.namaBarang;
    }
}