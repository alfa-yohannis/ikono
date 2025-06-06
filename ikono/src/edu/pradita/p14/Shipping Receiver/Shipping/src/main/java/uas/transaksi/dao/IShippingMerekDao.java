package uas.transaksi.dao;

import uas.transaksi.data.ShippingMerekData;
import java.util.List;

public interface IShippingMerekDao {
    void saveShippingMerek(ShippingMerekData merekV2);
    ShippingMerekData getShippingMerekById(int id);
    boolean isValidCourierByName(String courierName);
    List<ShippingMerekData> getAllShippingMerek();
    void updateShippingMerek(ShippingMerekData merekV2);
    void deleteShippingMerek(int id);
}