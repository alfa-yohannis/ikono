package dokter.dao;

import java.util.List;

import dokter.model.Patient;

public interface PatientDAO {
    void save(Patient patient);
    Patient getById(int id);
    List<Patient> getAll();
    // Add other methods as needed
} 