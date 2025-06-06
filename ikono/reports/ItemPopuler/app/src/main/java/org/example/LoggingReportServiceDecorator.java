package org.example;

import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingReportServiceDecorator implements ReportServiceInterface {
    private final ReportServiceInterface wrappedService;
    private static final Logger logger = Logger.getLogger(LoggingReportServiceDecorator.class.getName());

    public LoggingReportServiceDecorator(ReportServiceInterface wrappedService) {
        this.wrappedService = wrappedService;
    }

    @Override
    public ObservableList<ItemReport> getPopularItems(LocalDate startDate, LocalDate endDate) {
        logger.log(Level.INFO, "Mencoba memuat item populer antara " + startDate + " dan " + endDate);

        ObservableList<ItemReport> result = null;
        long startTime = System.currentTimeMillis();
        try {
            result = wrappedService.getPopularItems(startDate, endDate);
            long endTime = System.currentTimeMillis();
            logger.log(Level.INFO, "Berhasil memuat " + (result != null ? result.size() : 0) + " item populer. Durasi: " + (endTime - startTime) + " ms.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Gagal memuat item populer: " + e.getMessage(), e);
            throw e; // Lemparkan kembali exception agar perilaku tidak berubah
        }
        return result;
    }
}