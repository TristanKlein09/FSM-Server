package database.implementations;

import database.interfaces.DatabaseConnectionProvider;
import database.interfaces.DatabaseInitialiser;
import login.AuthService;
import models.login.Permissions;
import models.login.User;
import util.Util;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLDatabaseInitialiser implements DatabaseInitialiser {
    @Override
    public void initDatabase() throws SQLException {
        initUsers();
        initAddresses();
        initJobs();
    }

    public void initUsers() {
        DatabaseConnectionProvider connectionProvider = new SQLDatabaseConnectionProvider();
        try (Connection connection = connectionProvider.connectToDb(SQLDatabaseConnectionProvider.dbUrl)){
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

    public void initJobs() throws SQLException {
        //Establish a connection to the database
        DatabaseConnectionProvider connectionProvider = new SQLDatabaseConnectionProvider();
        try (Connection connection = connectionProvider.connectToDb(SQLDatabaseConnectionProvider.dbUrl)) {
            //Create jobs table if it does not exist
            String createJobsTable = "CREATE TABLE IF NOT EXISTS jobs " +
                    "(job_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "description TEXT," +
                    "address TEXT NOT NULL," +
                    "priority TEXT NOT NULL," +
                    "status TEXT NOT NULL," +
                    "scheduled_start_time DATETIME NOT NULL," +
                    "scheduled_end_time DATETIME NOT NULL," +
                    "actual_start_time DATETIME NOT NULL," +
                    "actual_end_time DATETIME NOT NULL," +
                    "assigned_to INTEGER NOT NULL," +
                    "created_by INTEGER NOT NULL," +
                    "form_name TEXT NOT NULL," +
                    "FOREIGN KEY(assigned_to) REFERENCES users(id)," +
                    "FOREIGN KEY(created_by) REFERENCES users(id)," +
                    "FOREIGN KEY(address) REFERENCES addresses(address_id))";

            assert connection != null;
            //Execute statements
            connection.createStatement().execute(createJobsTable);
        }
    }

    private void initAddresses() throws SQLException {
        DatabaseConnectionProvider connectionProvider = new SQLDatabaseConnectionProvider();
        try (Connection connection = connectionProvider.connectToDb(SQLDatabaseConnectionProvider.dbUrl)) {
            //Create address table if it doesn't already exist
            String createAddressTable = "CREATE TABLE IF NOT EXISTS addresses" +
                    "(address_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "address_line_1 TEXT NOT NULL," +
                    "address_line_2 TEXT," +
                    "city TEXT NOT NULL," +
                    "post_code TEXT NOT NULL)";

            assert connection != null;
            connection.createStatement().execute(createAddressTable);
        }
    }
}
