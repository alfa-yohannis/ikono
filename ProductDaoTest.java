package org.example.database;

import org.example.config.HibernateUtil;
import org.example.database.dao.ProductDao;
import org.example.database.entity.Product;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoTest {

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM Product").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    @Test
    @DisplayName("Should save a new product")
    void shouldSaveProduct() {
        Product product = new Product("Brand A", "Product X", new BigDecimal("100.00"), "Electronics", 5);
        Product savedProduct = productDao.save(product);

        assertNotNull(savedProduct.getId());
        assertEquals("Product X", savedProduct.getProductname());
        assertEquals(new BigDecimal("100.00"), savedProduct.getPrice());
    }

    @Test
    @DisplayName("Should find product by ID")
    void shouldFindProductById() {
        Product product = new Product("Brand B", "Product Y", new BigDecimal("250.50"), "Home", 2);
        productDao.save(product);

        Product foundProduct = productDao.findById(product.getId());
        assertNotNull(foundProduct);
        assertEquals("Product Y", foundProduct.getProductname());
    }

    @Test
    @DisplayName("Should find products by category")
    void shouldFindProductsByCategory() {
        productDao.save(new Product("Brand C", "Item 1", new BigDecimal("10.00"), "Books", 1));
        productDao.save(new Product("Brand D", "Item 2", new BigDecimal("20.00"), "Books", 3));
        productDao.save(new Product("Brand E", "Item 3", new BigDecimal("30.00"), "Clothes", 0));

        List<Product> books = productDao.findByCategory("Books");
        assertNotNull(books);
        assertEquals(2, books.size());
        assertTrue(books.stream().allMatch(p -> p.getCategory().equals("Books")));
    }

    @Test
    @DisplayName("Should get all products")
    void shouldGetAllProducts() {
        productDao.save(new Product("Brand F", "Gadget 1", new BigDecimal("500.00"), "Electronics", 10));
        productDao.save(new Product("Brand G", "Gadget 2", new BigDecimal("750.00"), "Electronics", 12));

        List<Product> products = productDao.findAll();
        assertNotNull(products);
        assertEquals(2, products.size());
    }

    @Test
    @DisplayName("Should update an existing product")
    void shouldUpdateProduct() {
        Product product = new Product("Old Brand", "Old Product", new BigDecimal("100.00"), "Old Category", 1);
        productDao.save(product);

        product.setBrandname("New Brand");
        product.setProductname("New Product");
        product.setPrice(new BigDecimal("150.00"));
        product.setCategory("New Category");
        product.setFeatures(2);

        Product updatedProduct = productDao.update(product);

        assertNotNull(updatedProduct);
        assertEquals("New Product", updatedProduct.getProductname());
        assertEquals(new BigDecimal("150.00"), updatedProduct.getPrice());

        Product foundProduct = productDao.findById(product.getId());
        assertEquals("New Product", foundProduct.getProductname());
    }

    @Test
    @DisplayName("Should delete a product")
    void shouldDeleteProduct() {
        Product product = new Product("Brand H", "Product to Delete", new BigDecimal("50.00"), "Tools", 0);
        productDao.save(product);

        productDao.deleteById(product.getId());

        Product deletedProduct = productDao.findById(product.getId());
        assertNull(deletedProduct);
    }
}