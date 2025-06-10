package org.example.service;

import org.example.data.entity.ReturPembelian;
import org.example.data.repository.ReturRepository;

/**
 * Implementasi konkret dari ReturServiceInterface.
 * Bertanggung jawab untuk logika bisnis terkait penyimpanan retur pembelian.
 */
public class ReturService implements ReturServiceInterface {

    private final ReturRepository repository;

    /**
     * Konstruktor yang menginjeksikan dependensi ReturRepository.
     * @param repository instance dari ReturRepository.
     */
    public ReturService(ReturRepository repository) {
        this.repository = repository;
    }

    /**
     * Menyimpan data retur pembelian dengan mendelegasikannya ke repository.
     * @param retur Objek ReturPembelian yang akan disimpan.
     * @throws Exception jika terjadi kesalahan selama proses penyimpanan di repository.
     */
    @Override
    public void save(ReturPembelian retur) throws Exception {
        // Logika bisnis tambahan bisa ditambahkan di sini sebelum atau sesudah menyimpan
        // Contoh: validasi kompleks, logging, dll.
        repository.save(retur);
        // Logika bisnis tambahan setelah menyimpan
    }
}