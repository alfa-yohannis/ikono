package com.pelunasan.factory;

import com.pelunasan.model.Hutang;

public class DefaultHutangFactory implements HutangFactory {
    @Override
    public Hutang buatHutang(String nama, double jumlah) {
        return new Hutang(nama, jumlah);
    }
}
