package org.example.util.decorator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EncryptedDataSourceDecoratorTest {

    @Test
    @DisplayName("Should encrypt and decrypt data correctly")
    void shouldEncryptAndDecryptData() {
        DataSource baseDataSource = new BaseDataSource();
        DataSource encryptedDataSource = new EncryptedDataSourceDecorator(baseDataSource);

        String originalData = "Hello World!";
        encryptedDataSource.writeData(originalData);

        String decryptedData = encryptedDataSource.readData();

        assertEquals(new StringBuilder(originalData).reverse().toString(), baseDataSource.readData());
        assertEquals(originalData, decryptedData);
    }
}