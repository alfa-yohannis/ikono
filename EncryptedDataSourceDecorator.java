package org.example.util.decorator;

public class EncryptedDataSourceDecorator extends DataSourceDecorator {

    public EncryptedDataSourceDecorator(DataSource source) {
        super(source);
    }

    @Override
    public String readData() {
        String data = super.readData();
        return decrypt(data);
    }

    @Override
    public void writeData(String data) {
        super.writeData(encrypt(data));
    }

    private String encrypt(String data) {
        System.out.println("Encrypting data: " + data);
        return new StringBuilder(data).reverse().toString();
    }

    private String decrypt(String data) {
        System.out.println("Decrypting data: " + data);
        return new StringBuilder(data).reverse().toString();
    }
}