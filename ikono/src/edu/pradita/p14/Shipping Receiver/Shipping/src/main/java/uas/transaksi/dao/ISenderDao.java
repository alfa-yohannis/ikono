package uas.transaksi.dao;

import uas.transaksi.data.SenderData;
import java.util.List;

public interface ISenderDao {
    void saveSender(SenderData sender);
    SenderData getSenderById(int id);
    List<SenderData> getAllSenders();
    void updateSender(SenderData sender);
    void deleteSender(int id);
}