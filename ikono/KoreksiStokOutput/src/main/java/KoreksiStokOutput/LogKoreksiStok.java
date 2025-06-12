package KoreksiStokOutput;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_koreksi_stok")
public class LogKoreksiStok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_barang", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Barang barang;

    // Perbaikan: Anotasi @Column untuk memastikan nama cocok dengan DB
    @Column(name = "stok_lama")
    private int stokLama;

    @Column(name = "stok_baru")
    private int stokBaru;

    @Column(name = "waktu_koreksi")
    private LocalDateTime waktuKoreksi;

    // ... sisa Getter dan Setter tidak perlu diubah ...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Barang getBarang() { return barang; }
    public void setBarang(Barang barang) { this.barang = barang; }
    public int getStokLama() { return stokLama; }
    public void setStokLama(int stokLama) { this.stokLama = stokLama; }
    public int getStokBaru() { return stokBaru; }
    public void setStokBaru(int stokBaru) { this.stokBaru = stokBaru; }
    public LocalDateTime getWaktuKoreksi() { return waktuKoreksi; }
    public void setWaktuKoreksi(LocalDateTime waktuKoreksi) { this.waktuKoreksi = waktuKoreksi; }
}