package org.example.patterns.observer;

import org.example.model.SalesReturn;

// Interface Subject ini sudah diimplementasikan oleh SalesReturnServiceImpl
// Jadi tidak perlu file terpisah jika hanya digunakan oleh service tersebut.
// Namun, jika ingin lebih generik, bisa dibuat terpisah.
// Untuk kasus ini, kita anggap SalesReturnServiceImpl adalah Subject utama.

public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(SalesReturn salesReturn, String action); // action bisa "CREATE", "UPDATE", "DELETE"
}