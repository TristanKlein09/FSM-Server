package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String usersDbUrl = "jdbc:sqlite:users.sqlite";

    //Opens a connection to the users.sqlite database
    public static Connection connectUsersDb() throws SQLException {
        try {
            return DriverManager.getConnection(DatabaseManager.usersDbUrl); //Establish connection to the database

        } catch (Exception e) {
            System.err.println("Error initializing user database: " + e.getMessage());
        }

        return null;
    }

    //Getters and setters
    public static String getUsersDbUrl() {
        return usersDbUrl;
    }

}
