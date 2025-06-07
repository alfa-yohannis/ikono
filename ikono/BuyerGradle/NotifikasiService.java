package org.example.service;

import org.example.pattern.observer.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class NotifikasiService {
    private static final Logger logger = LoggerFactory.getLogger(NotifikasiService.class);
    private final List<Observer> observers = new ArrayList<>();

    public void tambahObserver(Observer observer) {
        observers.add(observer);
        logger.debug("Observer ditambahkan: {}", observer.getClass().getSimpleName());
    }

    public void hapusObserver(Observer observer) {
        observers.remove(observer);
        logger.debug("Observer dihapus: {}", observer.getClass().getSimpleName());
    }

    public void kirimNotifikasi(String message) {
        logger.info("Mengirim notifikasi: \"{}\" ke {} observer.", message, observers.size());
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}