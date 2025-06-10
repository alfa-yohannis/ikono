package edu.pradita.uas.category.model;

/**
 * Interface untuk pola Observer.
 * Setiap kelas yang ingin "mendengarkan" perubahan data kategori
 * harus mengimplementasikan interface ini.
 */
@FunctionalInterface // Interface ini hanya punya satu metode abstrak
public interface CategoryObserver {
    /**
     * Metode ini akan dipanggil oleh Subject (CategoryService) ketika
     * ada perubahan data.
     */
    void onDataChanged();
}
