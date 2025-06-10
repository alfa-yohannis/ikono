package com.inventory.warehouseinventorysystem.service;

import com.inventory.warehouseinventorysystem.dao.DaoFactory;
import com.inventory.warehouseinventorysystem.dao.ProductDao;
import com.inventory.warehouseinventorysystem.model.Product;
import com.inventory.warehouseinventorysystem.model.Warehouse;
import com.inventory.warehouseinventorysystem.observer.ProductEventManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao mockProductDao;

    @Mock
    private DaoFactory mockDaoFactory;

    @Mock
    private ProductEventManager mockProductEventManager;

    private ProductService productService;

    private Warehouse dummyWarehouse;

    @BeforeEach
    void setUp() {
        when(mockDaoFactory.getProductDao()).thenReturn(mockProductDao);
        productService = new ProductService(mockDaoFactory, mockProductEventManager);

        dummyWarehouse = new Warehouse("Test Warehouse", "Test Location");
        dummyWarehouse.setId(1);
    }

    @Test
    void getProductById_whenProductExists_shouldReturnProduct() {
        // Arrange
        int productId = 1;
        Product expectedProduct = new Product("Test Product", "SKU001", 10, BigDecimal.TEN, dummyWarehouse);
        expectedProduct.setId(productId);
        when(mockProductDao.findById(productId)).thenReturn(expectedProduct);

        // Act
        Product actualProduct = productService.getProductById(productId);

        // Assert
        assertNotNull(actualProduct);
        assertEquals(expectedProduct.getName(), actualProduct.getName());
        verify(mockProductDao, times(1)).findById(productId);
    }

    @Test
    void getProductById_whenProductDoesNotExist_shouldReturnNull() {
        // Arrange
        int productId = 99;
        when(mockProductDao.findById(productId)).thenReturn(null);

        // Act
        Product actualProduct = productService.getProductById(productId);

        // Assert
        assertNull(actualProduct);
        verify(mockProductDao, times(1)).findById(productId);
    }

    @Test
    void saveProduct_whenNewValidProduct_shouldCallDaoSaveAndNotifyEventManager() {
        // Arrange
        Product newProduct = new Product("New Gadget", "SKU-NEW-001", 5, new BigDecimal("199.99"), dummyWarehouse);
        when(mockProductDao.findBySku(newProduct.getSku())).thenReturn(null);

        // Act
        productService.saveProduct(newProduct);

        // Assert
        verify(mockProductDao, times(1)).save(newProduct);
        verify(mockProductEventManager, times(1)).productAdded(newProduct);
        verify(mockProductDao, never()).update(any(Product.class));
    }

    @Test
    void saveProduct_whenExistingValidProduct_shouldCallDaoUpdateAndNotifyEventManager() {
        // Arrange
        Product existingProduct = new Product("Old Gadget", "SKU-OLD-001", 10, new BigDecimal("99.99"), dummyWarehouse);
        existingProduct.setId(1);
        when(mockProductDao.findBySku(existingProduct.getSku())).thenReturn(existingProduct);

        // Act
        productService.saveProduct(existingProduct);

        // Assert
        verify(mockProductDao, times(1)).update(existingProduct);
        verify(mockProductEventManager, times(1)).productUpdated(existingProduct);
        verify(mockProductDao, never()).save(any(Product.class));
    }

    @Test
    void saveProduct_whenSkuAlreadyExistsForNewProduct_shouldThrowIllegalArgumentException() {
        // Arrange
        Product newProduct = new Product("Another Gadget", "SKU-DUP-002", 20, new BigDecimal("50.00"), dummyWarehouse);
        Product existingProductWithSameSku = new Product("Different Name", "SKU-DUP-002", 5, BigDecimal.ONE, dummyWarehouse);
        existingProductWithSameSku.setId(2);

        when(mockProductDao.findBySku("SKU-DUP-002")).thenReturn(existingProductWithSameSku);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.saveProduct(newProduct);
        });
        assertEquals("Product with SKU 'SKU-DUP-002' already exists.", exception.getMessage());
        verify(mockProductDao, never()).save(any(Product.class));
        verify(mockProductDao, never()).update(any(Product.class));
        verify(mockProductEventManager, never()).productAdded(any(Product.class));
        verify(mockProductEventManager, never()).productUpdated(any(Product.class));
    }

    @Test
    void saveProduct_whenNameIsEmpty_shouldThrowIllegalArgumentException() {
        Product productWithEmptyName = new Product("", "SKU-EMPTY-NAME", 10, BigDecimal.TEN, dummyWarehouse);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.saveProduct(productWithEmptyName);
        });
        assertEquals("Product name cannot be empty.", exception.getMessage());
    }

    @Test
    void saveProduct_whenSkuIsEmpty_shouldThrowIllegalArgumentException() {
        Product productWithEmptySku = new Product("Valid Name", "", 10, BigDecimal.TEN, dummyWarehouse);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.saveProduct(productWithEmptySku);
        });
        assertEquals("Product SKU cannot be empty.", exception.getMessage());
    }

    @Test
    void saveProduct_whenWarehouseIsNull_shouldThrowIllegalArgumentException() {
        Product productWithNullWarehouse = new Product("Valid Name", "SKU-NULL-WH", 10, BigDecimal.TEN, null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.saveProduct(productWithNullWarehouse);
        });
        assertEquals("Product must be assigned to a warehouse.", exception.getMessage());
    }

    @Test
    void saveProduct_whenPriceIsNull_shouldThrowIllegalArgumentException() {
        Product productWithNullPrice = new Product("Valid Name", "SKU-NULL-PRICE", 10, null, dummyWarehouse);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.saveProduct(productWithNullPrice);
        });
        assertEquals("Product price cannot be null.", exception.getMessage());
    }

    @Test
    void deleteProduct_whenProductExists_shouldCallDaoDeleteAndNotifyEventManager() {
        // Arrange
        int productIdToDelete = 1;
        Product productToDelete = new Product("Test Product", "SKU-DEL", 10, BigDecimal.TEN, dummyWarehouse);
        productToDelete.setId(productIdToDelete);

        when(mockProductDao.findById(productIdToDelete)).thenReturn(productToDelete);
        // doNothing().when(mockProductDao).delete(productToDelete); // Untuk metode void

        // Act
        productService.deleteProduct(productIdToDelete);

        // Assert
        verify(mockProductDao, times(1)).delete(productToDelete);
        verify(mockProductEventManager, times(1)).productDeleted(productToDelete);
    }

    @Test
    void deleteProduct_whenProductDoesNotExist_shouldThrowIllegalArgumentExceptionAndNotNotify() {
        // Arrange
        int productIdToDelete = 99;
        when(mockProductDao.findById(productIdToDelete)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.deleteProduct(productIdToDelete);
        });
        assertEquals("Product with id " + productIdToDelete + " not found.", exception.getMessage());
        verify(mockProductDao, never()).delete(any(Product.class));
        verify(mockProductEventManager, never()).productDeleted(any(Product.class));
    }
    // Contoh untuk getAllProducts
    @Test
    void getAllProducts_whenProductsExist_shouldReturnProductList() {
        // Arrange
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(new Product("P1", "S1", 1, BigDecimal.ONE, dummyWarehouse));
        expectedProducts.add(new Product("P2", "S2", 2, BigDecimal.TEN, dummyWarehouse));
        when(mockProductDao.findAll()).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productService.getAllProducts();

        // Assert
        assertNotNull(actualProducts);
        assertEquals(2, actualProducts.size());
        verify(mockProductDao, times(1)).findAll();
    }
}