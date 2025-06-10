// Contoh ProductDao.java (UBAH FILE INI)
// Contoh ProductDao.java (UBAH FILE INI)
package org.example.database.dao;

import org.example.database.entity.Product;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.example.config.HibernateUtil;

import java.util.List;

public class ProductDao extends BaseDao<Product, Long> {

    public ProductDao() {
        super(Product.class);
    }

    public List<Product> findByCategory(String category) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> query = session.createQuery("FROM Product WHERE category = :category", Product.class);
            query.setParameter("category", category);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding products by category: " + e.getMessage());
        }
    }
}