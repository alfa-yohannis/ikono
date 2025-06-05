package com.pelunasan.observer;

import com.pelunasan.model.Hutang;

public class ConsoleLoggerObserver implements HutangObserver {
    @Override
    public void onHutangDiubah(Hutang hutang, String aksi) {
        System.out.println("Hutang " + hutang.getNama() + " " + aksi + ": Rp " + hutang.getJumlah());
    }
}
