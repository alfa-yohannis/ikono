package org.example.service;

import org.example.data.entity.ReturPembelian;

/**
 * Interface untuk layanan ReturPembelian.
 * Mendefinisikan kontrak untuk operasi yang terkait dengan retur pembelian.
 * Ini mendukung Prinsip Ketergantungan Terbalik (Dependency Inversion Principle) dari SOLID.
 */
public interface ReturServiceInterface {

    /**
     * Menyimpan data retur pembelian.
     * @param retur Objek ReturPembelian yang akan disimpan.
     * @throws Exception jika terjadi kesalahan selama proses penyimpanan.
     */
    void save(ReturPembelian retur) throws Exception;
}