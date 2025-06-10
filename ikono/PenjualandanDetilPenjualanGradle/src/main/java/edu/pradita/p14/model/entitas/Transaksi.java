package edu.pradita.p14.model.entitas;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transaksi")
public class Transaksi {

    @Id
    // --- PERBAIKAN: Ubah panjang kolom agar sesuai dengan skema database ---
    @Column(name = "id", length = 25)
    private String id;

    @Column(name = "tanggal_waktu", nullable = false)
    private LocalDateTime tanggalWaktu;

    @ManyToOne
    @JoinColumn(name = "pembeli_id")
    private Pembeli pembeli;

    @OneToMany(mappedBy = "transaksi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetailTransaksi> detailTransaksis;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public LocalDateTime getTanggalWaktu() { return tanggalWaktu; }
    public void setTanggalWaktu(LocalDateTime tanggalWaktu) { this.tanggalWaktu = tanggalWaktu; }
    public Pembeli getPembeli() { return pembeli; }
    public void setPembeli(Pembeli pembeli) { this.pembeli = pembeli; }
    public List<DetailTransaksi> getDetailTransaksis() { return detailTransaksis; }
    public void setDetailTransaksis(List<DetailTransaksi> detailTransaksis) { this.detailTransaksis = detailTransaksis; }
}
