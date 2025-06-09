package dokter.dao;

import dokter.model.Doctor;
import dokter.model.Specialization;
import java.util.List;

public interface DoctorDAO {
    void saveOrUpdate(Doctor doctor);
    Doctor getById(int id);
    List<Doctor> getAll();
    void delete(Doctor doctor);
    List<Doctor> searchByName(String name);
} 