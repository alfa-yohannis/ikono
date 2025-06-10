package edu.pradita.p14.util.strategy;

import java.util.Properties;

public interface DatabaseStrategy {
    Properties getProperties(String host, String port, String dbName, String username, String password);
}