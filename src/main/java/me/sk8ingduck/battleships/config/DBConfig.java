package me.sk8ingduck.battleships.config;

import java.io.File;

public class DBConfig extends Config {

    private String host;
    private int port;
    private String username;
    private String password;
    private String database;

    public DBConfig(String name, File path) {
        super(name, path);

        if (getFileConfiguration().get("mysql.host") == null) {
            getFileConfiguration().set("mysql.host", "localhost");
            getFileConfiguration().set("mysql.port", 3306);
            getFileConfiguration().set("mysql.username", "username");
            getFileConfiguration().set("mysql.password", "password");
            getFileConfiguration().set("mysql.database", "database");
            save();
        }

        this.host = getFileConfiguration().getString("mysql.host");
        this.port = getFileConfiguration().getInt("mysql.port");
        this.username = getFileConfiguration().getString("mysql.username");
        this.password = getFileConfiguration().getString("mysql.password");
        this.database = getFileConfiguration().getString("mysql.database");
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
