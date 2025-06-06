package uas.transaksi.dao;

import uas.transaksi.data.ReceiverData;
import java.util.List;

public interface IReceiverDao {
    void saveReceiver(ReceiverData receiver);
    ReceiverData getReceiverById(int id);
    boolean isReceiverExist(int id);
    List<ReceiverData> getAllReceivers();
    void updateReceiver(ReceiverData receiver);
    void deleteReceiver(int id);
}