package org.example.patterns.observer;

import org.example.model.SalesReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailNotificationObserver implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationObserver.class);

    @Override
    public void update(SalesReturn salesReturn, String action) {
        logger.info("[Email Observer] Menerima notifikasi: Aksi '{}' pada Retur RMA '{}'", action, salesReturn.getRmaNumber());

        String subject = "";
        String body = "";

        switch (action.toUpperCase()) {
            case "CREATE":
                subject = "Retur Penjualan Diterima: RMA " + salesReturn.getRmaNumber();
                body = String.format(
                    "Pelanggan Yth. %s,\n\n" +
                    "Retur Anda untuk nomor RMA %s terkait nomor nota %s telah kami terima dan sedang diproses.\n" +
                    "Detail item: %s (ID: %s), Kuantitas: %d.\n" +
                    "Alasan Retur: %s.\n" +
                    "Metode Refund: %s.\n" +
                    "Komentar: %s\n\n" +
                    "Terima kasih.",
                    salesReturn.getCustomerName(), salesReturn.getRmaNumber(), salesReturn.getReceiptNumber(),
                    salesReturn.getDescription(), salesReturn.getItemId(), salesReturn.getQuantity(),
                    salesReturn.getReturnReason(), salesReturn.getRefundMethod(), salesReturn.getComments() != null ? salesReturn.getComments() : "-"
                );
                break;
            case "UPDATE":
                subject = "Pembaruan Status Retur Penjualan: RMA " + salesReturn.getRmaNumber();
                body = String.format(
                    "Pelanggan Yth. %s,\n\n" +
                    "Ada pembaruan status untuk retur Anda dengan nomor RMA %s.\n" +
                    "Detail terbaru:\n" +
                    "Deskripsi: %s, Kuantitas: %d, Alasan: %s, Metode Refund: %s, Komentar: %s\n" +
                    "Silakan cek status terbaru di sistem kami atau hubungi layanan pelanggan.\n\n" +
                    "Terima kasih.",
                    salesReturn.getCustomerName(), salesReturn.getRmaNumber(),
                    salesReturn.getDescription(), salesReturn.getQuantity(), salesReturn.getReturnReason(),
                    salesReturn.getRefundMethod(), salesReturn.getComments() != null ? salesReturn.getComments() : "-"
                );
                break;
            case "DELETE":
                 subject = "Pembatalan Retur Penjualan: RMA " + salesReturn.getRmaNumber();
                body = String.format(
                    "Pelanggan Yth. %s,\n\n" +
                    "Retur Anda dengan nomor RMA %s telah dibatalkan/dihapus dari sistem kami.\n" +
                    "Jika Anda memiliki pertanyaan, silakan hubungi layanan pelanggan kami.\n\n" +
                    "Terima kasih.",
                    salesReturn.getCustomerName(), salesReturn.getRmaNumber()
                );
                break;
            default:
                logger.warn("[Email Observer] Aksi tidak dikenal: {}. Tidak ada email yang dikirim.", action);
                return;
        }

        // Simulasi pengiriman email
        logger.info("[Email Observer] Mengirim email ke {} dengan subjek: '{}'", salesReturn.getCustomerName(), subject);
        // logger.debug("[Email Observer] Isi Email:\n{}", body); // Uncomment untuk melihat isi email di log debug
        // Di aplikasi nyata, Anda akan menggunakan JavaMail API atau library email lainnya di sini.
    }
}