package uas.transaksi.dao.decorator; // PERHATIKAN: Paketnya sekarang 'decorator'

import uas.transaksi.dao.ISenderDao; // Import interface
import uas.transaksi.data.SenderData;
import java.util.List;
// Anda bisa menggunakan SLF4J Logger untuk logging yang lebih baik
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

public class LoggingSenderDaoDecorator implements ISenderDao {
    // private static final Logger log = LoggerFactory.getLogger(LoggingSenderDaoDecorator.class);
    private ISenderDao decoratedDao;

    public LoggingSenderDaoDecorator(ISenderDao decoratedDao) {
        this.decoratedDao = decoratedDao;
    }

    @Override
    public void saveSender(SenderData sender) {
        System.out.println("LOG (Decorator): Menyimpan pengirim: " + sender.getName()); // log.info()
        decoratedDao.saveSender(sender);
    }

    @Override
    public SenderData getSenderById(int id) {
        System.out.println("LOG (Decorator): Mengambil pengirim dengan ID: " + id);
        return decoratedDao.getSenderById(id);
    }

    @Override
    public List<SenderData> getAllSenders() {
        System.out.println("LOG (Decorator): Mengambil semua pengirim.");
        return decoratedDao.getAllSenders();
    }

    @Override
    public void updateSender(SenderData sender) {
        System.out.println("LOG (Decorator): Memperbarui pengirim: " + sender.getName());
        decoratedDao.updateSender(sender);
    }

    @Override
    public void deleteSender(int id) {
        System.out.println("LOG (Decorator): Menghapus pengirim dengan ID: " + id);
        decoratedDao.deleteSender(id);
    }
}