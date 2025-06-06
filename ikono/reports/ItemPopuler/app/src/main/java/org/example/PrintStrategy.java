package org.example;

import javafx.scene.control.TableView;
import javafx.collections.ObservableList;

public interface PrintStrategy<T> {
    void print(TableView<T> table, ObservableList<ItemReport> data); // Bisa juga hanya menerima data jika strategi tidak selalu butuh TableView
}