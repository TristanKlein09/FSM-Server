package database.implementations;

import database.interfaces.DatabaseConnectionProvider;
import database.interfaces.DatabaseInitialiser;
import login.AuthService;
import models.login.Permissions;
import models.login.User;
import util.Util;
import java.sql.Connection;
import java.sql.ResultSet;

public class SQLDatabaseInitialiser implements DatabaseInitialiser {
    @Override
    public void initDatabase() {
        initUsers();
    }

    public void initUsers() {
        DatabaseConnectionProvider connectionProvider = new SQLDatabaseConnectionProvider();
        try (Connection connection = connectionProvider.connectToDb(SQLDatabaseConnectionProvider.usersDbUrl)){
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
