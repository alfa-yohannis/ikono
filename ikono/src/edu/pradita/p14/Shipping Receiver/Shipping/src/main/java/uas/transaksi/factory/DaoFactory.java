package uas.transaksi.factory;

import uas.transaksi.dao.*; // Import semua interface DAO
import uas.transaksi.dao.impl.*; // Import implementasi DAO konkret

// Ini adalah Factory untuk membuat instance DAO
public class DaoFactory {

    // Metode pabrik untuk ISenderDao
    public static ISenderDao getSenderDao() {
        // Di sini Anda bisa menambahkan logika untuk mengembalikan implementasi yang berbeda
        // Misalnya, berdasarkan konfigurasi (database_type=hibernate/jdbc)
        return new SenderDaoImpl(); // Mengembalikan implementasi Hibernate
    }

    // Metode pabrik untuk IReceiverDao
    public static IReceiverDao getReceiverDao() {
        return new ReceiverDaoImpl();
    }

    // Metode pabrik untuk IShipmentDao
    public static IShipmentDao getShipmentDao() {
        return new ShipmentDaoImpl();
    }

    // Metode pabrik untuk IShippingMerekDao
    public static IShippingMerekDao getShippingMerekDao() {
        return new ShippingMerekDaoImpl();
    }
}