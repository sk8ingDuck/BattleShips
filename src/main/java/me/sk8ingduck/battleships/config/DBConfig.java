package me.sk8ingduck.battleships.config;

import java.io.File;

public class DBConfig extends Config {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String database;

    public DBConfig(String name, File path) {
        super(name, path);

        this.host = (String) checkPathOrSet("mysql.host", "localhost");
        this.port = (int) checkPathOrSet("mysql.port", 3306);
        this.username = (String) checkPathOrSet("mysql.username", "root");
        this.password = (String) checkPathOrSet("mysql.password", "pw");
        this.database = (String) checkPathOrSet("mysql.database", "db");
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }
}
