package com.pelunasan.factory;

import com.pelunasan.model.Hutang;

public interface HutangFactory {
    Hutang buatHutang(String nama, double jumlah);
}
