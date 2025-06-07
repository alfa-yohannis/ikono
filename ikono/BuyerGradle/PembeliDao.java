package org.example.dao;

import org.example.model.Pembeli;
import java.util.List;
import java.util.Optional;

public interface PembeliDao {
    void save(Pembeli pembeli);
    Optional<Pembeli> findById(int id);
    List<Pembeli> findAll();
    void update(Pembeli pembeli);
    void delete(Pembeli pembeli);
    void deleteById(int id);
}