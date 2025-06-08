package org.example.service;

import org.example.model.SalesReturn;
import org.example.patterns.observer.Observer;

import java.util.List;
import java.util.Optional;

public interface SalesReturnService {
    SalesReturn createReturn(SalesReturn salesReturn);
    Optional<SalesReturn> getReturnById(int id);
    List<SalesReturn> getAllReturns();
    void updateReturn(SalesReturn salesReturn);
    void deleteReturnById(int id);

    // Metode untuk Observer Pattern
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(SalesReturn salesReturn, String action);
}