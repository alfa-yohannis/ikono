// Lokasi: src/main/java/org/example/entity/Vendor.java
package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendor")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vendor_id;
    private String name;
    private String contact_person;
    private String email;
    private String phone;
    @Column(columnDefinition = "TEXT")
    private String address;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    
    // Getters and Setters
    public int getVendor_id() { return vendor_id; }
    public void setVendor_id(int vendor_id) { this.vendor_id = vendor_id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    // ... getter setter lainnya untuk semua field ...
}
