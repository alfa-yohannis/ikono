package com.pelunasan.decorator;

public class HutangDenganCatatan extends HutangDecorator {
    private final String catatan;

    public HutangDenganCatatan(IHutang hutang, String catatan) {
        super(hutang);
        this.catatan = catatan;
    }

    @Override
    public String getDeskripsi() {
        return hutang.getDeskripsi() + " [Catatan: " + catatan + "]";
    }
}
