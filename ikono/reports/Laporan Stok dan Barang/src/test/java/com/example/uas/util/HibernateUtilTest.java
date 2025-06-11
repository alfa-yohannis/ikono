package com.example.uas.util;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HibernateUtilTest {

    @BeforeEach
    void setUp() {
        try {
            java.lang.reflect.Field field = HibernateUtil.class.getDeclaredField("sessionFactory");
            field.setAccessible(true);
            field.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            java.lang.reflect.Field registryField = HibernateUtil.class.getDeclaredField("registry");
            registryField.setAccessible(true);
            registryField.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetSessionFactoryNotNull() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        assertNotNull(sessionFactory, "SessionFactory should not be null.");
    }

    @Test
    void testGetSessionFactoryIsSingleton() {
        SessionFactory sessionFactory1 = HibernateUtil.getSessionFactory();
        SessionFactory sessionFactory2 = HibernateUtil.getSessionFactory();
        assertSame(sessionFactory1, sessionFactory2, "SessionFactory instances should be the same.");
    }

    @AfterAll
    static void tearDown() {
        HibernateUtil.shutdown();
    }
} 