package dokter.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.*;

@Entity
@Table(name = "jadwal_praktek") // Assuming this is your table name
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id") // Assuming you have a primary key
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "hari_praktek")
    private String dayOfWeek;

    @Column(name = "jam_mulai")
    private String startTime;

    @Column(name = "jam_selesai")
    private String endTime;

    @Column(name = "keterangan")
    private String description;

    @Column(name = "lokasi_praktek")
    private String location;
    
    // JavaFX Properties (transient for Hibernate, populated manually)
    @Transient
    private transient StringProperty dayOfWeekProperty = new SimpleStringProperty();
    @Transient
    private transient StringProperty startTimeProperty = new SimpleStringProperty();
    @Transient
    private transient StringProperty endTimeProperty = new SimpleStringProperty();
    @Transient
    private transient StringProperty descriptionProperty = new SimpleStringProperty();
    @Transient
    private transient StringProperty locationProperty = new SimpleStringProperty();

    public Schedule() {}

    public Schedule(Doctor doctor, String dayOfWeek, String startTime, String endTime, String description, String location) {
        this.doctor = doctor;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.location = location;
        updateProperties();
    }
    
    public void updateProperties() {
        this.dayOfWeekProperty.set(this.dayOfWeek);
        this.startTimeProperty.set(this.startTime);
        this.endTimeProperty.set(this.endTime);
        this.descriptionProperty.set(this.description);
        this.locationProperty.set(this.location);
    }

    // Getters and Setters for Hibernate fields
    public int getId() { return id; }
    public void setId(int id) { this.id = id;}

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; this.dayOfWeekProperty.set(dayOfWeek); }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; this.startTimeProperty.set(startTime); }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; this.endTimeProperty.set(endTime); }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; this.descriptionProperty.set(description); }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; this.locationProperty.set(location); }

    // JavaFX Property Getters
    public StringProperty dayOfWeekProperty() { return dayOfWeekProperty; }
    public StringProperty startTimeProperty() { return startTimeProperty; }
    public StringProperty endTimeProperty() { return endTimeProperty; }
    public StringProperty descriptionProperty() { return descriptionProperty; }
    public StringProperty locationProperty() { return locationProperty; }

    // Convenience getters for TableView
    public String getFxDayOfWeek() { return dayOfWeekProperty.get(); }
    public String getFxStartTime() { return startTimeProperty.get(); }
    public String getFxEndTime() { return endTimeProperty.get(); }
    public String getFxDescription() { return descriptionProperty.get(); }
    public String getFxLocation() { return locationProperty.get(); }
} 