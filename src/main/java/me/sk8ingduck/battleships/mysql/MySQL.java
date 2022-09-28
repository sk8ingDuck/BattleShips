package me.sk8ingduck.battleships.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private Connection con;

    public MySQL(String host, String port, String username, String password, String database) {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/",
                    username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setup(database);
    }

    private void setup(String database) {
        //TODO: Implement
    }
}
