package edu.pradita.p14.util.strategy;

public class DefaultUiSettings {
    private String host;
    private String port;
    private String username;
    private String defaultDatabaseName;

    public DefaultUiSettings(String host, String port, String username, String defaultDatabaseName) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.defaultDatabaseName = defaultDatabaseName;
    }

    public String getHost() { return host; }
    public String getPort() { return port; }
    public String getUsername() { return username; }
    public String getDefaultDatabaseName() { return defaultDatabaseName; }
}