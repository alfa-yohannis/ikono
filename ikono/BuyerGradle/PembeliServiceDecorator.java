package org.example.pattern.decorator;

import org.example.model.Pembeli;
import org.example.service.PembeliService;

import java.util.List;
import java.util.Optional;

public abstract class PembeliServiceDecorator implements PembeliService {
    protected PembeliService decoratedService;

    public PembeliServiceDecorator(PembeliService decoratedService) {
        this.decoratedService = decoratedService;
    }

    @Override
    public void tambahPembeli(Pembeli pembeli) {
        decoratedService.tambahPembeli(pembeli);
    }

    @Override
    public Optional<Pembeli> getPembeliById(int id) {
        return decoratedService.getPembeliById(id);
    }

    @Override
    public List<Pembeli> getAllPembeli() {
        return decoratedService.getAllPembeli();
    }

    @Override
    public void updatePembeli(Pembeli pembeli) {
        decoratedService.updatePembeli(pembeli);
    }

    @Override
    public void hapusPembeliById(int id) {
        decoratedService.hapusPembeliById(id);
    }
}
