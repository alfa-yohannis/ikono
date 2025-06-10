package org.example.util.decorator;

public abstract class DataSourceDecorator implements DataSource {
    protected DataSource wrappee;

    public DataSourceDecorator(DataSource source) {
        this.wrappee = source;
    }

    @Override
    public String readData() {
        return wrappee.readData();
    }

    @Override
    public void writeData(String data) {
        wrappee.writeData(data);
    }
}