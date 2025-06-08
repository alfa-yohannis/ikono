package org.example.patterns.decorator;

import org.example.model.SalesReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicReturnProcess implements ReturnProcess {
    private static final Logger logger = LoggerFactory.getLogger(BasicReturnProcess.class);

    @Override
    public void handleReturn(SalesReturn salesReturn) {
        logger.info("Memproses retur dasar untuk RMA: {}. Item ID: {}, Kuantitas: {}",
                salesReturn.getRmaNumber(), salesReturn.getItemId(), salesReturn.getQuantity());
        // Logika dasar penanganan retur: validasi awal, pencatatan sederhana
    }

    @Override
    public String getDescription() {
        return "Proses Retur Dasar";
    }
}