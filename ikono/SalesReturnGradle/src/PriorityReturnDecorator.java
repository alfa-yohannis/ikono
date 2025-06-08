package org.example.patterns.decorator;

import org.example.model.SalesReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PriorityReturnDecorator extends ReturnProcessDecorator {
    private static final Logger logger = LoggerFactory.getLogger(PriorityReturnDecorator.class);

    public PriorityReturnDecorator(ReturnProcess decoratedProcess) {
        super(decoratedProcess);
    }

    @Override
    public void handleReturn(SalesReturn salesReturn) {
        super.handleReturn(salesReturn); // Panggil proses dasar/sebelumnya
        addPriorityHandling(salesReturn); // Tambahkan fungsionalitas prioritas
    }

    private void addPriorityHandling(SalesReturn salesReturn) {
        logger.info("Menambahkan penanganan prioritas untuk RMA: {}. Item akan diproses lebih cepat.", salesReturn.getRmaNumber());
        // Logika spesifik untuk penanganan prioritas, misal: notifikasi ke tim khusus, flag di sistem
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", dengan Penanganan Prioritas";
    }
}