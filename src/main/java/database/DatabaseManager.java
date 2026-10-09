package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    //TODO: Add some functions to create the tables, so that when a new person sets up the server for the first time they can just call the functions
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
