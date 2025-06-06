package uas.transaksi.service; // PERHATIKAN: Paketnya sekarang 'service'

import uas.transaksi.dao.ISenderDao; // Import interface DAO
import uas.transaksi.dao.IReceiverDao; // Import interface DAO
import uas.transaksi.dao.IShippingMerekDao; // Import interface DAO
import uas.transaksi.data.SenderData;
import uas.transaksi.data.ReceiverData;
import uas.transaksi.data.ShippingMerekData;
import java.util.List;

public class MasterDataService {
    private ISenderDao senderDao;
    private IReceiverDao receiverDao;
    private IShippingMerekDao shippingMerekDao;

    // Injeksi DAO melalui konstruktor (DIP)
    public MasterDataService(ISenderDao senderDao, IReceiverDao receiverDao, IShippingMerekDao shippingMerekDao) {
        this.senderDao = senderDao;
        this.receiverDao = receiverDao;
        this.shippingMerekDao = shippingMerekDao;
    }

    // Metode untuk Sender
    public void addSender(SenderData sender) { senderDao.saveSender(sender); }
    public SenderData getSenderById(int id) { return senderDao.getSenderById(id); }
    public List<SenderData> getAllSenders() { return senderDao.getAllSenders(); }
    public void updateSender(SenderData sender) { senderDao.updateSender(sender); }
    public void deleteSender(int id) { senderDao.deleteSender(id); }

    // Metode untuk Receiver
    public void addReceiver(ReceiverData receiver) { receiverDao.saveReceiver(receiver); }
    public ReceiverData getReceiverById(int id) { return receiverDao.getReceiverById(id); }
    public boolean isReceiverExist(int id) { return receiverDao.isReceiverExist(id); }
    public List<ReceiverData> getAllReceivers() { return receiverDao.getAllReceivers(); }
    public void updateReceiver(ReceiverData receiver) { receiverDao.updateReceiver(receiver); }
    public void deleteReceiver(int id) { receiverDao.deleteReceiver(id); }

    // Metode untuk ShippingMerek
    public void addShippingMerek(ShippingMerekData merekV2) { shippingMerekDao.saveShippingMerek(merekV2); }
    public ShippingMerekData getShippingMerekById(int id) { return shippingMerekDao.getShippingMerekById(id); }
    public boolean isValidCourierByName(String courierName) { return shippingMerekDao.isValidCourierByName(courierName); }
    public List<ShippingMerekData> getAllShippingMereks() { return shippingMerekDao.getAllShippingMerek(); }
    public void updateShippingMerek(ShippingMerekData merekV2) { shippingMerekDao.updateShippingMerek(merekV2); }
    public void deleteShippingMerek(int id) { shippingMerekDao.deleteShippingMerek(id); }
}