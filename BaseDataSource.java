package org.example.util.decorator;

public class BaseDataSource implements DataSource {
    private String data = "";

    @Override
    public String readData() {
        return data;
    }

    @Override
    public void writeData(String data) {
        this.data = data;
        System.out.println("Data written to BaseDataSource: " + data);
    }
}