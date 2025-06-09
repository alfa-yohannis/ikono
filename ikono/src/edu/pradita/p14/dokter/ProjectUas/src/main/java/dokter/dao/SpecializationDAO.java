package dokter.dao;

import dokter.model.Specialization;
import java.util.List;

public interface SpecializationDAO {
    void save(Specialization specialization);
    Specialization getById(int id);
    Specialization getByName(String name);
    List<Specialization> getAll();
} 