package com.pelunasan.decorator;

import com.pelunasan.model.Hutang;

public class HutangDasar implements IHutang {

    private final Hutang hutang;

    public HutangDasar(Hutang hutang) {
        this.hutang = hutang;
    }

    @Override
    public String getDeskripsi() {
        return hutang.getNama();
    }

    @Override
    public double getJumlah() {
        return hutang.getJumlah();
    }
}
