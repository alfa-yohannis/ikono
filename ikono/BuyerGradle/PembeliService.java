package org.example.service;

import org.example.model.Pembeli;
import java.util.List;
import java.util.Optional;

public interface PembeliService {
    void tambahPembeli(Pembeli pembeli);
    Optional<Pembeli> getPembeliById(int id);
    List<Pembeli> getAllPembeli();
    void updatePembeli(Pembeli pembeli);
    void hapusPembeliById(int id);
}
