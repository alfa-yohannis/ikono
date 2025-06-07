package org.example.service;

import org.example.dao.PembeliDao;
import org.example.model.Pembeli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class PembeliServiceImpl implements PembeliService {

    private static final Logger logger = LoggerFactory.getLogger(PembeliServiceImpl.class);
    private final PembeliDao pembeliDao;
    private final NotifikasiService notifikasiService; // Untuk Observer pattern

    public PembeliServiceImpl(PembeliDao pembeliDao, NotifikasiService notifikasiService) {
        this.pembeliDao = pembeliDao;
        this.notifikasiService = notifikasiService;
    }

    @Override
    public void tambahPembeli(Pembeli pembeli) {
        // Validasi atau logika bisnis bisa ditambahkan di sini
        if (pembeli.getNamaLengkap() == null || pembeli.getNamaLengkap().trim().isEmpty()) {
            logger.warn("Nama lengkap tidak boleh kosong.");
            throw new IllegalArgumentException("Nama lengkap tidak boleh kosong.");
        }
        pembeliDao.save(pembeli);
        logger.info("Pembeli berhasil ditambahkan: {}", pembeli.getNamaLengkap());
        notifikasiService.kirimNotifikasi("Pembeli baru ditambahkan: " + pembeli.getNamaLengkap());
    }

    @Override
    public Optional<Pembeli> getPembeliById(int id) {
        logger.debug("Mencari pembeli dengan ID: {}", id);
        return pembeliDao.findById(id);
    }

    @Override
    public List<Pembeli> getAllPembeli() {
        logger.debug("Mengambil semua data pembeli.");
        return pembeliDao.findAll();
    }

    @Override
    public void updatePembeli(Pembeli pembeli) {
        if (pembeli.getIdPembeli() <= 0) {
             logger.warn("ID Pembeli tidak valid untuk update.");
            throw new IllegalArgumentException("ID Pembeli tidak valid untuk update.");
        }
        pembeliDao.update(pembeli);
        logger.info("Pembeli berhasil diupdate: ID {}", pembeli.getIdPembeli());
        notifikasiService.kirimNotifikasi("Data pembeli diupdate: " + pembeli.getNamaLengkap());
    }

    @Override
    public void hapusPembeliById(int id) {
        Optional<Pembeli> pembeliOpt = pembeliDao.findById(id);
        if (pembeliOpt.isPresent()){
            pembeliDao.deleteById(id);
            logger.info("Pembeli berhasil dihapus: ID {}", id);
            notifikasiService.kirimNotifikasi("Pembeli dihapus: " + pembeliOpt.get().getNamaLengkap());
        } else {
            logger.warn("Gagal menghapus. Pembeli dengan ID {} tidak ditemukan.", id);
            // throw new EntityNotFoundException("Pembeli dengan ID " + id + " tidak ditemukan.");
        }
    }
}
