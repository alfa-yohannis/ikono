package edu.pradita.p14.util.strategy;

import java.util.Properties;

public class PostgreSqlStrategy implements DatabaseStrategy {
    @Override
    public Properties getProperties(String host, String port, String dbName, String username, String password) {
        Properties settings = new Properties();
        settings.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        String mysqlPort = (port == null || port.trim().isEmpty()) ? "3306" : port;
        settings.put("hibernate.connection.url", "jdbc:mysql://" + host + ":" + mysqlPort + "/" + dbName + "?useSSL=false&serverTimezone=UTC");
        settings.put("hibernate.connection.username", username);
        settings.put("hibernate.connection.password", password);
        settings.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        settings.put("hibernate.show_sql", "true");
        settings.put("hibernate.current_session_context_class", "thread");
        System.out.println("postgre");
        // settings.put("hibernate.hbm2ddl.auto", "update");
        return settings;
    }

    @Override
    public String toString() {
        return "PostgreSQL";
    }
}