package edu.pradita.p14.model.entitas;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pembeli")
public class Pembeli {

    @Id
    @Column(name = "id", length = 10)
    private String id;

    @Column(name = "nama", length = 100, nullable = false)
    private String nama;

    @Column(name = "telepon", length = 15)
    private String telepon;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }
}
