package dokter.model;

import javafx.beans.property.*; // Import JavaFX properties if you want to use them directly in TableView later
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "data_pasien") // Your existing table name
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming id_pasien is auto-increment
    @Column(name = "id_pasien")
    private int idPasien;

    @Column(name = "nama_lengkap", nullable = false)
    private String namaLengkap;

    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    @Column(name = "jenis_kelamin")
    private String jenisKelamin;

    @Column(name = "alamat", columnDefinition = "TEXT")
    private String alamat;

    @Column(name = "nomor_telepon")
    private String nomorTelepon;

    @Column(name = "email")
    private String email;

    @Column(name = "golongan_darah")
    private String golonganDarah;

    @Column(name = "status_pernikahan")
    private String statusPernikahan;

    @Column(name = "riwayat_penyakit", columnDefinition = "TEXT")
    private String riwayatPenyakit;

    @Column(name = "alergi", columnDefinition = "TEXT")
    private String alergi;

    @Column(name = "tanggal_registrasi")
    private LocalDate tanggalRegistrasi;

    // JavaFX properties (transient for Hibernate, can be populated manually if needed for UI binding)
    @Transient
    private transient IntegerProperty idPasienProperty = new SimpleIntegerProperty();
    @Transient
    private transient StringProperty namaLengkapProperty = new SimpleStringProperty();
    @Transient
    private transient ObjectProperty<LocalDate> tanggalLahirProperty = new SimpleObjectProperty<>();
    // ... add other properties if direct binding is desired for Patient objects.

    public Patient() {}

    public Patient(String namaLengkap, LocalDate tanggalLahir, String jenisKelamin, String alamat,
                   String nomorTelepon, String email, String golonganDarah, String statusPernikahan,
                   String riwayatPenyakit, String alergi, LocalDate tanggalRegistrasi) {
        this.namaLengkap = namaLengkap;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.nomorTelepon = nomorTelepon;
        this.email = email;
        this.golonganDarah = golonganDarah;
        this.statusPernikahan = statusPernikahan;
        this.riwayatPenyakit = riwayatPenyakit;
        this.alergi = alergi;
        this.tanggalRegistrasi = tanggalRegistrasi;
        updateProperties();
    }
    
    public void updateProperties() {
        this.idPasienProperty.set(this.idPasien);
        this.namaLengkapProperty.set(this.namaLengkap);
        this.tanggalLahirProperty.set(this.tanggalLahir);
        // ... update other properties
    }


    // Getters and Setters for Hibernate
    public int getIdPasien() { return idPasien; }
    public void setIdPasien(int idPasien) { this.idPasien = idPasien; this.idPasienProperty.set(idPasien); }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; this.namaLengkapProperty.set(namaLengkap); }

    public LocalDate getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(LocalDate tanggalLahir) { this.tanggalLahir = tanggalLahir; this.tanggalLahirProperty.set(tanggalLahir); }

    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getNomorTelepon() { return nomorTelepon; }
    public void setNomorTelepon(String nomorTelepon) { this.nomorTelepon = nomorTelepon; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGolonganDarah() { return golonganDarah; }
    public void setGolonganDarah(String golonganDarah) { this.golonganDarah = golonganDarah; }

    public String getStatusPernikahan() { return statusPernikahan; }
    public void setStatusPernikahan(String statusPernikahan) { this.statusPernikahan = statusPernikahan; }

    public String getRiwayatPenyakit() { return riwayatPenyakit; }
    public void setRiwayatPenyakit(String riwayatPenyakit) { this.riwayatPenyakit = riwayatPenyakit; }

    public String getAlergi() { return alergi; }
    public void setAlergi(String alergi) { this.alergi = alergi; }

    public LocalDate getTanggalRegistrasi() { return tanggalRegistrasi; }
    public void setTanggalRegistrasi(LocalDate tanggalRegistrasi) { this.tanggalRegistrasi = tanggalRegistrasi; }

    // JavaFX Property getters (example)
    public StringProperty namaLengkapProperty() { return namaLengkapProperty; }
    public ObjectProperty<LocalDate> tanggalLahirProperty() { return tanggalLahirProperty; }
    // ... add other property getters if needed

    // Getters for TableView using JavaFX properties (if you choose to populate them)
    public String getFxNamaLengkap() { return namaLengkapProperty.get(); }
    public LocalDate getFxTanggalLahir() { return tanggalLahirProperty.get(); }
    // ...
} 