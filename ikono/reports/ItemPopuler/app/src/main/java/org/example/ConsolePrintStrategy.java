package org.example;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView; // Mungkin tidak digunakan

public class ConsolePrintStrategy<T> implements PrintStrategy<T> {
    @Override
    public void print(TableView<T> table, ObservableList<ItemReport> data) { // TableView mungkin tidak dibutuhkan
        if (data == null || data.isEmpty()) {
            System.out.println("Tidak ada data untuk dicetak ke konsol.");
            return;
        }
        System.out.println("--- Laporan Item Populer (Konsol) ---");
        for (ItemReport item : data) {
            System.out.println("Produk: " + item.productNameProperty().get() + ", Jumlah: " + item.quantityProperty().get());
        }
        System.out.println("--- Akhir Laporan ---");
    }
}