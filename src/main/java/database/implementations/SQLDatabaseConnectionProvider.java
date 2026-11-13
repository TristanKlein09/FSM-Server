package database.implementations;

import database.interfaces.DatabaseConnectionProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLDatabaseConnectionProvider implements DatabaseConnectionProvider {
    private static String usersDbUrl = "jdbc:sqlite:users.sqlite";

    public static void setUsersDbUrl(String url) { usersDbUrl = url; }

    @Override
    public Connection connectUsersDb() throws SQLException {
        try {
            return DriverManager.getConnection(usersDbUrl);
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
