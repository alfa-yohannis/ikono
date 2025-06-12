package org.example;

import java.util.List;
import java.util.Optional;

public interface MasterModelDao {
    void save(MasterModel model);
    void update(MasterModel model);
    void delete(MasterModel model);
    Optional<MasterModel> findById(String id);
    List<MasterModel> findAll();
}