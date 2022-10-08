package me.sk8ingduck.battleships.mysql;


import java.sql.Connection;
import java.sql.SQLException;

public class MySQL {

    private Connection con;

    public MySQL(String host, int port, String username, String password, String database) {
        /*
        try {
            con = ChillsuchtAPI.getDatabaseAPI().openDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setup(database);

        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/",
                    username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
    }

    private void setup(String database) {
        //TODO: Implement
    }
}
