package org.example.patterns.observer;

import org.example.model.SalesReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryUpdaterObserver implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(InventoryUpdaterObserver.class);

    @Override
    public void update(SalesReturn salesReturn, String action) {
        logger.info("[Inventory Observer] Menerima notifikasi: Aksi '{}' pada Retur RMA '{}' untuk Item ID '{}', Kuantitas '{}'",
                action, salesReturn.getRmaNumber(), salesReturn.getItemId(), salesReturn.getQuantity());

        switch (action.toUpperCase()) {
            case "CREATE":
                // Logika untuk menambah stok kembali atau menandai item sebagai 'dikembalikan'
                logger.info("[Inventory Observer] Stok untuk item {} telah disesuaikan (bertambah) sebanyak {}.",
                        salesReturn.getItemId(), salesReturn.getQuantity());
                // Contoh: panggil service inventaris: inventoryService.increaseStock(salesReturn.getItemId(), salesReturn.getQuantity());
                break;
            case "DELETE":
                // Logika untuk mengembalikan penyesuaian stok jika retur dibatalkan
                logger.info("[Inventory Observer] Retur RMA: {} dibatalkan. Stok untuk item {} dikembalikan (berkurang) sebanyak {}.",
                        salesReturn.getRmaNumber(), salesReturn.getItemId(), salesReturn.getQuantity());
                // Contoh: inventoryService.decreaseStock(salesReturn.getItemId(), salesReturn.getQuantity());
                break;
            case "UPDATE":
                 // Logika jika ada perubahan kuantitas pada update
                 // Ini mungkin memerlukan perbandingan dengan data sebelum update
                logger.info("[Inventory Observer] Retur RMA: {} diperbarui. Periksa apakah ada perubahan kuantitas yang mempengaruhi stok.",
                        salesReturn.getRmaNumber());
                break;
            default:
                logger.warn("[Inventory Observer] Aksi tidak dikenal: {}", action);
        }
    }
}
