package com.pelunasan.service;

import com.pelunasan.factory.HutangFactory;
import com.pelunasan.factory.DefaultHutangFactory;
import com.pelunasan.dao.HutangDAO;
import com.pelunasan.model.Hutang;
import com.pelunasan.observer.HutangObserver;

import java.util.ArrayList;
import java.util.List;

public class HutangService {

    private final HutangDAO hutangDAO;
    private final HutangFactory factory;
    private final List<HutangObserver> observers = new ArrayList<>();

    public HutangService() {
        this.hutangDAO = new HutangDAO();
        this.factory = new DefaultHutangFactory();
    }

    // Tambah observer
    public void tambahObserver(HutangObserver observer) {
        observers.add(observer);
    }

    // Kirim notifikasi ke semua observer
    private void notifikasi(Hutang hutang, String aksi) {
        for (HutangObserver o : observers) {
            o.onHutangDiubah(hutang, aksi);
        }
    }

    public void tambahHutang(String nama, double jumlah) {
        if (nama == null || nama.isEmpty() || jumlah <= 0) {
            throw new IllegalArgumentException("Nama dan jumlah hutang harus valid.");
        }
        Hutang hutang = factory.buatHutang(nama, jumlah);
        hutangDAO.simpan(hutang);
        notifikasi(hutang, "DITAMBAH");
    }

    public List<Hutang> ambilSemuaHutang() {
        return hutangDAO.getSemuaHutang();
    }

    public void tandaiHutangLunas(Long id) {
        hutangDAO.tandaiLunas(id);
        Hutang hutang = hutangDAO.cariById(id);
        if (hutang != null) {
            notifikasi(hutang, "LUNAS");
        }
    }

    public void hapusHutang(Long id) {
        Hutang hutang = hutangDAO.cariById(id);
        hutangDAO.hapus(id);
        if (hutang != null) {
            notifikasi(hutang, "DIHAPUS");
        }
    }

    public void tutup() {
        hutangDAO.tutup();
    }
}
