package edu.pradita.uas.category.service;

import edu.pradita.uas.category.model.Category;
import edu.pradita.uas.category.model.CategoryObserver;
import edu.pradita.uas.category.repository.CategoryRepository;
import edu.pradita.uas.category.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Lapisan Service untuk logika bisnis Category.
 * Bertindak sebagai Facade, menyembunyikan kompleksitas repository dari controller.
 * Bertindak sebagai Subject dalam pola Observer.
 */
public class CategoryService {

    private final CategoryRepository repository;
    private final List<CategoryObserver> observers = new ArrayList<>();

    public CategoryService() {
        // Mendapatkan SessionFactory dari Singleton HibernateUtil
        this.repository = new CategoryRepository(HibernateUtil.getSessionFactory());
    }

    // --- Metode untuk pola Observer ---
    public void addObserver(CategoryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CategoryObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (CategoryObserver observer : observers) {
            observer.onDataChanged();
        }
    }
    // ---------------------------------

    // --- Metode Facade untuk Controller ---
    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    public void saveCategory(Category category) {
        // Di sini bisa ditambahkan logika bisnis, misal:
        // if (category.getName().length() < 3) { throw new IllegalArgumentException("Name too short"); }
        repository.save(category);
        notifyObservers(); // Beri tahu observers bahwa data telah berubah
    }

    public void updateCategory(Category category) {
        repository.update(category);
        notifyObservers(); // Beri tahu observers bahwa data telah berubah
    }

    public void deleteCategory(int id) {
        repository.delete(id);
        notifyObservers(); // Beri tahu observers bahwa data telah berubah
    }

    public boolean isCategoryParent(int id) {
        return repository.isParent(id);
    }
    // -----------------------------------
}
