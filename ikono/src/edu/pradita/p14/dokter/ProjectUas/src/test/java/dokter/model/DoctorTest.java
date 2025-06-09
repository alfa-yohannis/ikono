package dokter.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class DoctorTest {

    @Test
    void testDoctorCreationAndGetters() {
        Specialization cardio = new Specialization("Cardiology");
        cardio.setId(1); // Simulate ID for test

        Doctor doctor = new Doctor(
                "John",
                "Doe",
                cardio,
                "john.doe@example.com",
                "1234567890",
                "123 Main St",
                LocalDate.now()
        );
        doctor.setDoctorId(101); // Simulate ID after persistence
        doctor.updateProperties(); // Update JavaFX properties after setting fields

        assertEquals("John", doctor.getFirstName());
        assertEquals("Doe", doctor.getLastName());
        assertEquals("john.doe@example.com", doctor.getEmail());
        assertEquals(cardio, doctor.getSpecialization());
        assertEquals("Cardiology", doctor.getFxSpecializationName()); // Test FX property
        assertEquals(101, doctor.getDoctorId());
        assertNotNull(doctor.getHireDate());
    }

    @Test
    void testDoctorFxProperties() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Jane");
        doctor.setLastName("Smith");
        doctor.setEmail("jane.smith@email.com");
        // updateProperties() would typically be called by DAO or after setting all fields.
        // Here we are testing individual property updates via setters.

        assertEquals("Jane", doctor.getFxFirstName()); // Test property getter
        assertEquals("jane.smith@email.com", doctor.getFxEmail());

        doctor.firstNameProperty().set("Janet");
        assertEquals("Janet", doctor.getFirstName()); // Test backing field update
        assertEquals("Janet", doctor.getFxFirstName()); // Test property getter again
    }
} 