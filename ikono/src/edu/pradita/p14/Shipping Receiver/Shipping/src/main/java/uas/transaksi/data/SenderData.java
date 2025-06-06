package uas.transaksi.data;

import jakarta.persistence.*;

@Entity
@Table(name = "senders")
public class SenderData {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    // Konstruktor default WAJIB ada untuk Hibernate
    public SenderData() {}

    // Konstruktor untuk inisialisasi objek SenderData
    public SenderData(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    // --- Getters dan Setters (WAJIB ada untuk Hibernate) ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}