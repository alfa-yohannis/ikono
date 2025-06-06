package edu.pradita.p14.javafx.dao;

import edu.pradita.p14.javafx.ShippingMerek;
import java.util.List;

public interface ShippingMerekDao {
    // PERUBAHAN: Tambahkan "throws Exception" agar metode bisa "melempar" error
    void save(ShippingMerek merek) throws Exception;
    void update(ShippingMerek merek) throws Exception;
    void delete(int id) throws Exception;

    // Metode ini hanya membaca, jadi relatif aman, tapi lebih baik ditambahkan juga
    ShippingMerek findById(int id) throws Exception;
    List<ShippingMerek> findAll() throws Exception;
    List<ShippingMerek> findByName(String name) throws Exception;
}