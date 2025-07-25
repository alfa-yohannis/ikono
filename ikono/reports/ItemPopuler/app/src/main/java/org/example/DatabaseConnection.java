// DatabaseConnection.java
package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private String url;
    private String user;
    private String password;

    public DatabaseConnection(String url, String user, String password) {
        this.url = "jdbc:mysql://localhost:3306/transaction";
        this.user = "root";
        this.password = "12345";
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}