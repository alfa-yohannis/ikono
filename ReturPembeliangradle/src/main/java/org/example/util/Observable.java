package org.example.util;

// Menggunakan java.util.Observable dan Observer yang sudah deprecated
// sebagai contoh sederhana dari pola Observer.
// Untuk aplikasi produksi, pertimbangkan menggunakan alternatif modern
// seperti java.beans.PropertyChangeSupport atau library pihak ketiga (RxJava, dll).

/**
 * Kelas dasar untuk objek yang dapat diamati (Subject) dalam Pola Desain Behavioral Observer.
 * Kelas ini memperluas java.util.Observable yang sudah deprecated,
 * namun menyediakan dasar untuk implementasi pola Observer.
 */
@SuppressWarnings("deprecation") // Menekan peringatan deprecated
public class Observable extends java.util.Observable {

    /**
     * Memberi tahu semua observer yang terdaftar tentang perubahan.
     * Metode ini pertama-tama menandai objek ini sebagai telah berubah (`setChanged()`),
     * kemudian memanggil `notifyObservers(Object arg)` dari superclass.
     * @param arg Argumen yang akan diteruskan ke metode update() dari setiap observer.
     */
    @Override
    public synchronized void notifyObservers(Object arg) {
        super.setChanged(); // Penting untuk menandai bahwa ada perubahan sebelum notifikasi
        super.notifyObservers(arg);
    }

    /**
     * Memberi tahu semua observer yang terdaftar tentang perubahan, tanpa argumen.
     */
    @Override
    public synchronized void notifyObservers() {
        super.setChanged();
        super.notifyObservers();
    }
}