package uas.transaksi.service; // PERHATIKAN: Paketnya sekarang 'service'

import uas.transaksi.dao.ISenderDao; // Import interface DAO
import uas.transaksi.dao.IReceiverDao; // Import interface DAO
import uas.transaksi.dao.IShipmentDao; // Import interface DAO
import uas.transaksi.data.SenderData;
import uas.transaksi.data.ReceiverData;
import uas.transaksi.data.ShipmentData;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;

public class PengirimanService {
    private ISenderDao senderDao;
    private IReceiverDao receiverDao;
    private IShipmentDao shipmentDao;

    // Injeksi DAO melalui konstruktor (DIP)
    public PengirimanService(ISenderDao senderDao, IReceiverDao receiverDao, IShipmentDao shipmentDao) {
        this.senderDao = senderDao;
        this.receiverDao = receiverDao;
        this.shipmentDao = shipmentDao;
    }

    public void createShipment(String shipmentCodeParam, int senderId, int receiverId, LocalDate shippingDate, String status) throws Exception {
        final String finalShipmentCode;

        if (shipmentCodeParam == null || shipmentCodeParam.trim().isEmpty()) {
            finalShipmentCode = "SHP-" + System.currentTimeMillis();
        } else {
            finalShipmentCode = shipmentCodeParam;
        }

        SenderData sender = senderDao.getSenderById(senderId);
        if (sender == null) {
            throw new IllegalArgumentException("Pengirim dengan ID " + senderId + " tidak ditemukan.");
        }

        ReceiverData receiver = receiverDao.getReceiverById(receiverId);
        if (receiver == null) {
            throw new IllegalArgumentException("Penerima dengan ID " + receiverId + " tidak ditemukan.");
        }

        List<ShipmentData> existingShipments = shipmentDao.getAllShipments();
        boolean shipmentExistsByCode = existingShipments.stream().anyMatch(s -> finalShipmentCode.equals(s.getShipmentCode()));
        if (shipmentExistsByCode) {
            throw new IllegalArgumentException("Kode pengiriman " + finalShipmentCode + " sudah ada.");
        }

        ShipmentData newShipment = new ShipmentData(finalShipmentCode, sender, receiver, Date.valueOf(shippingDate), status);
        shipmentDao.saveShipment(newShipment);
    }

    public List<ShipmentData> getAllShipments() {
        return shipmentDao.getAllShipments();
    }

    public ShipmentData getShipmentById(int id) {
        return shipmentDao.getShipmentById(id);
    }

    public void updateShipment(ShipmentData shipment) {
        shipmentDao.updateShipment(shipment);
    }

    public void deleteShipment(int id) {
        shipmentDao.deleteShipment(id);
    }
}