package dokter.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpecializationTest {

    @Test
    void testSpecializationCreationAndGetters() {
        Specialization spec = new Specialization("Cardiology");
        spec.setId(1);

        assertEquals(1, spec.getId());
        assertEquals("Cardiology", spec.getName());
    }

    @Test
    void testToString() {
        Specialization spec = new Specialization("Neurology");
        assertEquals("Neurology", spec.toString());
    }
} 