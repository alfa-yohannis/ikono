package com.pelunasan.decorator;

public abstract class HutangDecorator implements IHutang {
    protected IHutang hutang;

    public HutangDecorator(IHutang hutang) {
        this.hutang = hutang;
    }

    @Override
    public String getDeskripsi() {
        return hutang.getDeskripsi();
    }

    @Override
    public double getJumlah() {
        return hutang.getJumlah();
    }
}
