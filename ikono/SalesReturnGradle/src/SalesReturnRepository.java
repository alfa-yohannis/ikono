package org.example.repository;

import org.example.model.SalesReturn;
import java.util.List;
import java.util.Optional;

public interface SalesReturnRepository {
    SalesReturn save(SalesReturn salesReturn);
    Optional<SalesReturn> findById(int id);
    List<SalesReturn> findAll();
    void update(SalesReturn salesReturn);
    void delete(SalesReturn salesReturn); // Bisa juga void deleteById(int id);
    void deleteById(int id);
}