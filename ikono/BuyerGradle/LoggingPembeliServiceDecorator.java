package org.example.pattern.decorator;

import org.example.model.Pembeli;
import org.example.service.PembeliService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class LoggingPembeliServiceDecorator extends PembeliServiceDecorator {
    private static final Logger logger = LoggerFactory.getLogger(LoggingPembeliServiceDecorator.class);

    public LoggingPembeliServiceDecorator(PembeliService decoratedService) {
        super(decoratedService);
    }

    @Override
    public void tambahPembeli(Pembeli pembeli) {
        logger.info("[DECORATOR LOG] Attempting to add pembeli: {}", pembeli.getNamaLengkap());
        super.tambahPembeli(pembeli);
        logger.info("[DECORATOR LOG] Pembeli added successfully: {}", pembeli.getNamaLengkap());
    }

    @Override
    public Optional<Pembeli> getPembeliById(int id) {
        logger.info("[DECORATOR LOG] Attempting to get pembeli by ID: {}", id);
        Optional<Pembeli> result = super.getPembeliById(id);
        logger.info("[DECORATOR LOG] Pembeli by ID {} found: {}", id, result.isPresent());
        return result;
    }

    @Override
    public List<Pembeli> getAllPembeli() {
        logger.info("[DECORATOR LOG] Attempting to get all pembeli.");
        List<Pembeli> result = super.getAllPembeli();
        logger.info("[DECORATOR LOG] Found {} pembeli.", result.size());
        return result;
    }

    @Override
    public void updatePembeli(Pembeli pembeli) {
        logger.info("[DECORATOR LOG] Attempting to update pembeli: ID {}", pembeli.getIdPembeli());
        super.updatePembeli(pembeli);
        logger.info("[DECORATOR LOG] Pembeli updated successfully: ID {}", pembeli.getIdPembeli());
    }

    @Override
    public void hapusPembeliById(int id) {
        logger.info("[DECORATOR LOG] Attempting to delete pembeli by ID: {}", id);
        super.hapusPembeliById(id);
        logger.info("[DECORATOR LOG] Pembeli deleted successfully: ID {}", id);
    }
}
