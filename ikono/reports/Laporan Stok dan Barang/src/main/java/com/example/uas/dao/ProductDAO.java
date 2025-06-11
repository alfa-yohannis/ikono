package com.example.uas.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.example.uas.model.Product;
import com.example.uas.util.HibernateUtil;

public class ProductDAO {

    public List<Product> getAllProducts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Use HQL (Hibernate Query Language) to retrieve all products
            Query<Product> query = session.createQuery("FROM Product", Product.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 