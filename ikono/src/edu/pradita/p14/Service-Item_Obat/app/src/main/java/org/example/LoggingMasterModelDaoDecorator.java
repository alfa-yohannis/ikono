package org.example;

import java.util.List;
import java.util.Optional;

public class LoggingMasterModelDaoDecorator implements MasterModelDao {
    private final MasterModelDao wrappedDao; // DAO asli yang akan kita "dekorasi"

    // Constructor untuk menerima DAO yang akan dibungkus
    public LoggingMasterModelDaoDecorator(MasterModelDao wrappedDao) {
        this.wrappedDao = wrappedDao;
    }

    @Override
    public void save(MasterModel model) {
        System.out.println("[LOG-DECORATOR] Mencoba menyimpan model dengan ID: " + model.getId());
        wrappedDao.save(model); // Panggil metode save dari DAO asli
        System.out.println("[LOG-DECORATOR] Berhasil menyimpan model dengan ID: " + model.getId());
    }

    @Override
    public void update(MasterModel model) {
        System.out.println("[LOG-DECORATOR] Mencoba memperbarui model dengan ID: " + model.getId());
        wrappedDao.update(model);
        System.out.println("[LOG-DECORATOR] Berhasil memperbarui model dengan ID: " + model.getId());
    }

    @Override
    public void delete(MasterModel model) {
        System.out.println("[LOG-DECORATOR] Mencoba menghapus model dengan ID: " + model.getId());
        wrappedDao.delete(model);
        System.out.println("[LOG-DECORATOR] Berhasil menghapus model dengan ID: " + model.getId());
    }

    @Override
    public Optional<MasterModel> findById(String id) {
        System.out.println("[LOG-DECORATOR] Mencoba mencari model dengan ID: " + id);
        Optional<MasterModel> result = wrappedDao.findById(id);
        if (result.isPresent()) {
            System.out.println("[LOG-DECORATOR] Model dengan ID: " + id + " ditemukan.");
        } else {
            System.out.println("[LOG-DECORATOR] Model dengan ID: " + id + " tidak ditemukan.");
        }
        return result;
    }

    @Override
    public List<MasterModel> findAll() {
        System.out.println("[LOG-DECORATOR] Mencoba mencari semua model.");
        List<MasterModel> result = wrappedDao.findAll();
        System.out.println("[LOG-DECORATOR] Ditemukan " + result.size() + " model.");
        return result;
    }
}