package uas.transaksi.dao;

import uas.transaksi.data.ShipmentData;
import java.util.List;

public interface IShipmentDao {
    void saveShipment(ShipmentData shipment);
    ShipmentData getShipmentById(int id);
    List<ShipmentData> getAllShipments();
    void updateShipment(ShipmentData shipment);
    void deleteShipment(int id);
}