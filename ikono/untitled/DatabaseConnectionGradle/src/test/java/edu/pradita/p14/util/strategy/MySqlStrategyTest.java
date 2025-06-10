package edu.pradita.p14.util.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;

public class MySqlStrategyTest {

    private DatabaseStrategy mySqlStrategy;

    @BeforeEach
    void setUp() {
        mySqlStrategy = new MySqlStrategy();
    }

    @Test
    void testGetProperties() {
        Properties props = mySqlStrategy.getProperties("localhost", "3306", "testdb", "user", "pass");

        assertNotNull(props);
        assertEquals("com.mysql.cj.jdbc.Driver", props.getProperty("hibernate.connection.driver_class"));
        assertEquals("jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=UTC", props.getProperty("hibernate.connection.url"));
        assertEquals("user", props.getProperty("hibernate.connection.username"));
        assertEquals("pass", props.getProperty("hibernate.connection.password"));
        assertEquals("org.hibernate.dialect.MySQL8Dialect", props.getProperty("hibernate.dialect"));
    }
}