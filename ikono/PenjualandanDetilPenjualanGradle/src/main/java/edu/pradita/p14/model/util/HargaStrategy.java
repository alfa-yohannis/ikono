package edu.pradita.p14.model.util;

import edu.pradita.p14.model.entitas.Produk;

/**
 * Interface untuk Strategy Pattern.
 * Mendefinisikan metode untuk menghitung harga.
 */
public interface HargaStrategy {
    double hitungHarga(Produk produk, int jumlah);
}

