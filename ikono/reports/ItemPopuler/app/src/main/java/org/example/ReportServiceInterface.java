package org.example;

import javafx.collections.ObservableList;
import java.time.LocalDate;

public interface ReportServiceInterface {
    ObservableList<ItemReport> getPopularItems(LocalDate startDate, LocalDate endDate);
}