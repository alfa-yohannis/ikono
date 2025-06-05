package com.pelunasan.service;

import com.pelunasan.model.Hutang;
import com.pelunasan.observer.HutangObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HutangServiceTest {

    private HutangService service;

    @BeforeEach
    public void setUp() {
        service = new HutangService();
    }

    @Test
    public void testTambahHutang_ValidInput_BerhasilDitambahkan() {
        service.tambahHutang("Budi", 100000);
        assertEquals(1, service.ambilSemuaHutang().size());
        assertEquals("Budi", service.ambilSemuaHutang().get(0).getNama());
    }

    @Test
    public void testTambahHutang_NamaKosong_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.tambahHutang("", 50000);
        });
    }

    @Test
    public void testTambahHutang_JumlahNegatif_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.tambahHutang("Andi", -1000);
        });
    }

    @Test
    public void testObserver_DiberitahuSaatHutangDitambahkan() {
        HutangObserver observer = mock(HutangObserver.class);
        service.tambahObserver(observer);

        service.tambahHutang("Sinta", 200000);

        ArgumentCaptor<Hutang> captor = ArgumentCaptor.forClass(Hutang.class);
        verify(observer, times(1)).onHutangDiubah(captor.capture(), eq("DITAMBAH"));
        assertEquals("Sinta", captor.getValue().getNama());
    }
}
