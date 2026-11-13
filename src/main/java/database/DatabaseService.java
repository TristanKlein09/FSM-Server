package database;

import login.AuthService;
import models.login.Permissions;
import models.login.User;
import util.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

@Deprecated
public class DatabaseService {
    //TODO: Add some functions to create the tables, so that when a new person sets up the server for the first time they can just call the functions

    private static String usersDbUrl = "jdbc:sqlite:users.sqlite";

    public static void setUsersDbUrl(String url) { usersDbUrl = url; }

    //Opens a connection to the users.sqlite database
    public static Connection connectUsersDb() {
        try {
            return DriverManager.getConnection(DatabaseService.usersDbUrl); //Establish connection to the database

        } catch (Exception e) {
            System.err.println("Error initializing user database: " + e.getMessage());
        }

        return null;
    }

    //TODO: Finish
    //TODO: Write tests for this function
    /**
     * Initialises the database with the required tables if they do not exist. Also adds a default admin user
     * if the tables are empty. There is hashing involved so this needs to be run on its own thread.
     */
    public void initTables() {
        try (Connection connection = connectUsersDb();){
            //Create usersLogins table if it does not exist
            String createUserLogins = "CREATE TABLE IF NOT EXISTS userLogins " +
                    "(user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password_hash TEXT NOT NULL)";

            //Create users table if it does not exist
            String createUsers = "CREATE TABLE IF NOT EXISTS users" +
                    "(id INTEGER PRIMARY KEY," +
                    "first_name TEXT NOT NULL," +
                    "last_name TEXT NOT NULL," +
                    "permission TEXT NOT NULL," +
                    "FOREIGN KEY (id) REFERENCES userLogins(user_id))";

            assert connection != null;
            connection.createStatement().execute(createUserLogins);
            connection.createStatement().execute(createUsers);

            //Add one default admin user to the database if it does not exist
            //Check if table is empty by checking number of rows
            boolean isUserEmpty = false;
            boolean isUserLoginsEmpty = false;
            ResultSet rsUserEmpty = connection.createStatement().executeQuery("SELECT * FROM users");
            if (!rsUserEmpty.next()) isUserEmpty = true; //There are no rows
            ResultSet rsUserLoginsEmpty = connection.createStatement().executeQuery("SELECT * FROM userLogins");
            if (!rsUserLoginsEmpty.next()) isUserLoginsEmpty = true;

            //If both tables are empty, add the default admin user
            if (isUserEmpty && isUserLoginsEmpty) {
                AuthService authService = new AuthService();
                User adminUser = new User("admin", Util.hashPlainText("admin"), "Admin", "Admin", Permissions.ADMIN);
                authService.registerUser(adminUser);
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
