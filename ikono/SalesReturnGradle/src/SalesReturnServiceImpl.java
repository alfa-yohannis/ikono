package org.example.service;

import org.example.model.SalesReturn;
import org.example.patterns.observer.Observer;
import org.example.patterns.observer.Subject; // Subject adalah bagian dari implementasi internal service
import org.example.repository.SalesReturnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalesReturnServiceImpl implements SalesReturnService, Subject {
    private static final Logger logger = LoggerFactory.getLogger(SalesReturnServiceImpl.class);

    private final SalesReturnRepository salesReturnRepository;
    private final List<Observer> observers;

    public SalesReturnServiceImpl(SalesReturnRepository salesReturnRepository) {
        this.salesReturnRepository = salesReturnRepository;
        this.observers = new ArrayList<>();
    }

    @Override
    public SalesReturn createReturn(SalesReturn salesReturn) {
        if (salesReturn == null) {
            throw new IllegalArgumentException("Objek SalesReturn tidak boleh null.");
        }
        if (salesReturn.getQuantity() <= 0) {
            logger.warn("Upaya membuat retur dengan kuantitas tidak valid: {}", salesReturn.getQuantity());
            throw new IllegalArgumentException("Kuantitas harus lebih besar dari 0.");
        }
        // Logika bisnis tambahan bisa di sini
        SalesReturn savedReturn = salesReturnRepository.save(salesReturn);
        logger.info("Retur baru dibuat dengan ID: {}", savedReturn.getId());
        notifyObservers(savedReturn, "CREATE");
        return savedReturn;
    }

    @Override
    public Optional<SalesReturn> getReturnById(int id) {
        logger.debug("Mencari retur dengan ID: {}", id);
        return salesReturnRepository.findById(id);
    }

    @Override
    public List<SalesReturn> getAllReturns() {
        logger.debug("Mengambil semua data retur.");
        return salesReturnRepository.findAll();
    }

    @Override
    public void updateReturn(SalesReturn salesReturn) {
        if (salesReturn == null) {
            throw new IllegalArgumentException("Objek SalesReturn untuk update tidak boleh null.");
        }
        if (salesReturn.getId() == 0){ // Atau cara lain untuk mengecek ID yang valid
             throw new IllegalArgumentException("ID SalesReturn untuk update tidak valid.");
        }
        // Pastikan data ada sebelum update
        Optional<SalesReturn> existingReturn = salesReturnRepository.findById(salesReturn.getId());
        if (!existingReturn.isPresent()) {
            logger.warn("Upaya memperbarui retur yang tidak ada dengan ID: {}", salesReturn.getId());
            throw new RuntimeException("Retur dengan ID " + salesReturn.getId() + " tidak ditemukan, tidak dapat diperbarui.");
        }
        salesReturnRepository.update(salesReturn);
        logger.info("Retur dengan ID: {} berhasil diperbarui.", salesReturn.getId());
        notifyObservers(salesReturn, "UPDATE");
    }

    @Override
    public void deleteReturnById(int id) {
        Optional<SalesReturn> salesReturnOpt = salesReturnRepository.findById(id);
        if (salesReturnOpt.isPresent()) {
            salesReturnRepository.deleteById(id);
            logger.info("Retur dengan ID: {} berhasil dihapus.", id);
            notifyObservers(salesReturnOpt.get(), "DELETE");
        } else {
            logger.warn("Gagal menghapus: Retur dengan ID {} tidak ditemukan.", id);
            // throw new RuntimeException("Retur dengan ID " + id + " tidak ditemukan, tidak dapat dihapus.");
        }
    }

    @Override
    public void registerObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            logger.debug("Observer {} berhasil didaftarkan.", observer.getClass().getSimpleName());
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        if (observers.remove(observer)) {
            logger.debug("Observer {} berhasil dihapus.", observer.getClass().getSimpleName());
        }
    }

    @Override
    public void notifyObservers(SalesReturn salesReturn, String action) {
        logger.debug("Memberitahu {} observer tentang aksi '{}' pada retur ID: {}", observers.size(), action, salesReturn != null ? salesReturn.getId() : "N/A");
        for (Observer observer : observers) {
            try {
                observer.update(salesReturn, action);
            } catch (Exception e) {
                logger.error("Error saat observer {} memproses notifikasi: {}", observer.getClass().getSimpleName(), e.getMessage(), e);
                // Lanjutkan notifikasi ke observer lain meskipun satu gagal
            }
        }
    }
}