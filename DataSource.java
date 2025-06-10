package org.example.util.decorator;

public interface DataSource {
    String readData();
    void writeData(String data);
}