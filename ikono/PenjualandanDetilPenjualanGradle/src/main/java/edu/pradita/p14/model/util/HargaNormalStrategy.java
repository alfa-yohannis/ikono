package edu.pradita.p14.model.util;

import edu.pradita.p14.model.entitas.Produk;

/**
 * Strategi konkret untuk harga normal.
 */
public class HargaNormalStrategy implements HargaStrategy {
    @Override
    public double hitungHarga(Produk produk, int jumlah) {
        return produk.getHarga() * jumlah;
    }
}
