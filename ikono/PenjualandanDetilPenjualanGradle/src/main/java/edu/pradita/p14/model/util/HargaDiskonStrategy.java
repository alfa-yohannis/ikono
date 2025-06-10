package edu.pradita.p14.model.util;

import edu.pradita.p14.model.entitas.Produk;

/**
 * Strategi konkret untuk harga diskon (misal: diskon 10% untuk pembelian > 5).
 */
public class HargaDiskonStrategy implements HargaStrategy {
    private static final int BATAS_JUMLAH_DISKON = 5;
    private static final double PERSENTASE_DISKON = 0.10; // 10%

    @Override
    public double hitungHarga(Produk produk, int jumlah) {
        double hargaTotal = produk.getHarga() * jumlah;
        if (jumlah > BATAS_JUMLAH_DISKON) {
            hargaTotal -= hargaTotal * PERSENTASE_DISKON;
        }
        return hargaTotal;
    }
}
