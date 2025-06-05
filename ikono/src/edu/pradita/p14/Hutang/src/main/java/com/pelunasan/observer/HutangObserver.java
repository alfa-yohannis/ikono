package com.pelunasan.observer;

import com.pelunasan.model.Hutang;

public interface HutangObserver {
    void onHutangDiubah(Hutang hutang, String aksi);
}
