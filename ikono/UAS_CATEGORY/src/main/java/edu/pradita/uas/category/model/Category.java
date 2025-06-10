package edu.pradita.uas.category.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Kelas Entity yang merepresentasikan tabel 'categories' di database.
 * Tanggung jawab: Hanya untuk menampung data kategori. (SRP)
 */
@Entity
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Relasi Many-to-One ke dirinya sendiri untuk struktur parent-child.
    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetch agar parent tidak langsung di-load
    @JoinColumn(name = "parent_id") // Nama kolom foreign key
    private Category parent;

    // Hibernate membutuhkan constructor tanpa argumen
    public Category() {}

    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }

    // Getters dan Setters
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

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    // Override untuk tampilan di ComboBox
    @Override
    public String toString() {
        return name;
    }

    // Override equals dan hashCode penting untuk perbandingan objek, terutama di Collections
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
