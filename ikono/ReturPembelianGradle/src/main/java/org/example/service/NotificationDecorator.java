package org.example.service;

import org.example.data.entity.ReturPembelian;

/**
 * Decorator untuk ReturServiceInterface yang menambahkan fungsionalitas notifikasi.
 * Ini mengimplementasikan Pola Desain Struktural Decorator.
 */
public class NotificationDecorator implements ReturServiceInterface {

    private final ReturServiceInterface wrappedService; // Layanan yang di-wrap (dihias)

    /**
     * Konstruktor yang menerima instance dari ReturServiceInterface yang akan dihias.
     * @param wrappedService Layanan yang akan ditambahkan fungsionalitas notifikasi.
     */
    public NotificationDecorator(ReturServiceInterface wrappedService) {
        this.wrappedService = wrappedService;
    }

    /**
     * Menyimpan data retur pembelian melalui layanan yang di-wrap,
     * kemudian mengirimkan notifikasi setelah penyimpanan berhasil.
     * @param retur Objek ReturPembelian yang akan disimpan.
     * @throws Exception jika terjadi kesalahan selama proses penyimpanan di layanan yang di-wrap.
     */
    @Override
    public void save(ReturPembelian retur) throws Exception {
        wrappedService.save(retur); // Mendelegasikan operasi save ke layanan asli
        sendNotification(retur);    // Menambahkan fungsionalitas baru (notifikasi)
    }

    /**
     * Metode privat untuk mengirim notifikasi.
     * Dalam implementasi nyata, ini bisa mengirim email, SMS, atau notifikasi dalam aplikasi.
     * @param retur Objek ReturPembelian yang berhasil disimpan.
     */
    private void sendNotification(ReturPembelian retur) {
        // Implementasi notifikasi sederhana ke konsol
        System.out.println("NOTIFIKASI (Decorator): Retur untuk produk '" +
                           (retur.getProduct() != null ? retur.getProduct().getName() : "N/A") +
                           "' dari pembelian '" +
                           (retur.getPembelian() != null ? retur.getPembelian().getIdPembelian() : "N/A") +
                           "' sejumlah " + retur.getJumlah() + " unit telah berhasil diproses.");
        // Di sini Anda bisa menambahkan logika untuk mengirim email, log ke sistem audit, dll.
    }
}