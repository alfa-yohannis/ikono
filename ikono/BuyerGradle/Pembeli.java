package org.example.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "pembeli")
public class Pembeli {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pembeli")
    private int idPembeli;

    @Column(name = "nama_lengkap", nullable = false)
    private String namaLengkap;

    @Column(name = "alamat", nullable = false) // Menghapus @Lob, akan default ke VARCHAR(255) untuk H2
    private String alamat;

    @Column(name = "kota")
    private String kota;

    @Column(name = "kode_pos")
    private String kodePos;

    @Column(name = "no_telepon")
    private String noTelepon;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin")
    private JenisKelamin jenisKelamin;

    @Column(name = "tanggal_daftar", insertable = false, updatable = false)
    private Timestamp tanggalDaftar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPembeli status;

    public enum JenisKelamin {
        L, P
    }

    public enum StatusPembeli {
        Aktif, Non_Aktif
    }

    // Constructors
    public Pembeli() {
    }

    public Pembeli(String namaLengkap, String alamat, String kota, String kodePos, String noTelepon, String email, JenisKelamin jenisKelamin, StatusPembeli status) {
        this.namaLengkap = namaLengkap;
        this.alamat = alamat;
        this.kota = kota;
        this.kodePos = kodePos;
        this.noTelepon = noTelepon;
        this.email = email;
        this.jenisKelamin = jenisKelamin;
        this.status = status;
    }

    // Getters and Setters
    public int getIdPembeli() {
        return idPembeli;
    }

    public void setIdPembeli(int idPembeli) {
        this.idPembeli = idPembeli;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getKodePos() {
        return kodePos;
    }

    public void setKodePos(String kodePos) {
        this.kodePos = kodePos;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public JenisKelamin getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(JenisKelamin jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public Timestamp getTanggalDaftar() {
        return tanggalDaftar;
    }

    public void setTanggalDaftar(Timestamp tanggalDaftar) {
        this.tanggalDaftar = tanggalDaftar;
    }

    public StatusPembeli getStatus() {
        return status;
    }

    public void setStatus(StatusPembeli status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pembeli pembeli = (Pembeli) o;
        return idPembeli == pembeli.idPembeli;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPembeli);
    }

    @Override
    public String toString() {
        return "ID: " + idPembeli + " - Nama: " + namaLengkap + " - Kota: " + kota + " - Status: " + status;
    }
}
