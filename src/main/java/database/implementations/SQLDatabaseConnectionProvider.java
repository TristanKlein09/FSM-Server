package database.implementations;

import database.interfaces.DatabaseConnectionProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLDatabaseConnectionProvider implements DatabaseConnectionProvider {
    public static String usersDbUrl = "jdbc:sqlite:users.sqlite";

    public void setUsersDbUrl(String url) { usersDbUrl = url; }

    @Override
    public Connection connectToDb(String url) throws SQLException {
        try {
            return DriverManager.getConnection(url);
        }
        catch (SQLException e) {
            throw new SQLException("Error connecting to the users db: " + e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
