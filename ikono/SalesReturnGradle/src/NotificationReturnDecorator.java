package org.example.patterns.decorator;

import org.example.model.SalesReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationReturnDecorator extends ReturnProcessDecorator {
    private static final Logger logger = LoggerFactory.getLogger(NotificationReturnDecorator.class);

    public NotificationReturnDecorator(ReturnProcess decoratedProcess) {
        super(decoratedProcess);
    }

    @Override
    public void handleReturn(SalesReturn salesReturn) {
        super.handleReturn(salesReturn); // Panggil proses dasar/sebelumnya
        sendNotification(salesReturn); // Tambahkan fungsionalitas notifikasi
    }

    private void sendNotification(SalesReturn salesReturn) {
        logger.info("Mengirim notifikasi (melalui decorator) untuk RMA: {} kepada pelanggan {}",
                salesReturn.getRmaNumber(), salesReturn.getCustomerName());
        // Logika untuk mengirim email, SMS, atau notifikasi internal lainnya
        // Ini bisa didelegasikan ke service notifikasi terpisah
    }

     @Override
    public String getDescription() {
        return super.getDescription() + ", dengan Notifikasi Tambahan";
    }
}