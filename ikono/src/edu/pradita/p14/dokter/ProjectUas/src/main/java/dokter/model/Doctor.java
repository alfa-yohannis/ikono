package dokter.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Transient
    private IntegerProperty doctorIdProperty = new SimpleIntegerProperty();
    @Transient
    private StringProperty firstNameProperty = new SimpleStringProperty();
    @Transient
    private StringProperty lastNameProperty = new SimpleStringProperty();
    // Specialization will be an object, but for TableView, we might need a StringProperty
    @Transient
    private StringProperty specializationNameProperty = new SimpleStringProperty();
    @Transient
    private StringProperty emailProperty = new SimpleStringProperty();
    @Transient
    private StringProperty phoneNumberProperty = new SimpleStringProperty();
    @Transient
    private StringProperty addressProperty = new SimpleStringProperty();
    @Transient
    private ObjectProperty<LocalDate> hireDateProperty = new SimpleObjectProperty<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private int doctorId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER) // Eager fetch for simplicity here
    @JoinColumn(name = "specialization_id", nullable = false)
    private Specialization specialization;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Schedule> schedules = new HashSet<>();
    
    public Doctor() {
        // Default constructor needed by Hibernate
        // Add listener to update the firstName field when firstNameProperty changes
        this.firstNameProperty.addListener((obs, oldV, newV) -> {
            // Check to prevent potential recursion if field setter also updates property,
            // though in this specific setup, this.firstName = newV; doesn't call setFirstName().
            if (this.firstName == null || !this.firstName.equals(newV)) {
                this.firstName = newV;
            }
        });
    }

    // Constructor for creating new Doctor instances before persisting
    public Doctor(String firstName, String lastName, Specialization specialization, String email, String phoneNumber, String address, LocalDate hireDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.hireDate = hireDate;
        updateProperties(); // Update JavaFX properties
    }
    
    // Call this after loading from DB or setting fields
    public void updateProperties() {
        this.doctorIdProperty.set(this.doctorId);
        this.firstNameProperty.set(this.firstName);
        this.lastNameProperty.set(this.lastName);
        if (this.specialization != null) {
            this.specializationNameProperty.set(this.specialization.getSpecializationName());
        }
        this.emailProperty.set(this.email);
        this.phoneNumberProperty.set(this.phoneNumber);
        this.addressProperty.set(this.address);
        this.hireDateProperty.set(this.hireDate);
    }


    // Getters and Setters for Hibernate fields
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; this.doctorIdProperty.set(doctorId); }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; this.firstNameProperty.set(firstName); }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; this.lastNameProperty.set(lastName); }

    public Specialization getSpecialization() { return specialization; }
    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
        if (specialization != null) {
            this.specializationNameProperty.set(specialization.getSpecializationName());
        } else {
            this.specializationNameProperty.set(null);
        }
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; this.emailProperty.set(email); }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; this.phoneNumberProperty.set(phoneNumber); }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; this.addressProperty.set(address); }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; this.hireDateProperty.set(hireDate); }
    
    public Set<Schedule> getSchedules() { return schedules; }
    public void setSchedules(Set<Schedule> schedules) { this.schedules = schedules; }

    // JavaFX Property getters
    public IntegerProperty doctorIdProperty() { return doctorIdProperty; }
    public StringProperty firstNameProperty() { return firstNameProperty; }
    public StringProperty lastNameProperty() { return lastNameProperty; }
    public StringProperty specializationNameProperty() { return specializationNameProperty; } // For TableView
    public StringProperty emailProperty() { return emailProperty; }
    public StringProperty phoneNumberProperty() { return phoneNumberProperty; }
    public StringProperty addressProperty() { return addressProperty; }
    public ObjectProperty<LocalDate> hireDateProperty() { return hireDateProperty; }

    // Convenience method for table display or other UI needs
    public String getFxSpecializationName() { return specializationNameProperty.get(); }
    public int getFxDoctorId() { return doctorIdProperty.get(); }
    public String getFxFirstName() { return firstNameProperty.get(); }
    public String getFxLastName() { return lastNameProperty.get(); }
    public String getFxEmail() { return emailProperty.get(); }
    public String getFxPhoneNumber() { return phoneNumberProperty.get(); }
    public String getFxAddress() { return addressProperty.get(); }
    public LocalDate getFxHireDate() { return hireDateProperty.get(); }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
} 