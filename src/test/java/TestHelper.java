import database.DatabaseManager;
import models.server.HttpRequest;
import models.server.HttpResponse;
import org.mindrot.jbcrypt.BCrypt;
import server.Router;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
    A class that contains methods that are used in the unit tests.
 **/
public class TestHelper {

    //Creates a temporary database that will be used in the unit tests.
    private static final String TEST_DB_URL = "jdbc:sqlite:test_users.sqlite";

    //Between each test the database should be reset, so that the tests don't interfere with each other
    public static void resetDatabase() throws Exception {
        DatabaseManager.setUsersDbUrl(TEST_DB_URL);

        try {
            Connection connection = DriverManager.getConnection(TEST_DB_URL); //Establish connection to the database
            Statement statement = connection.createStatement();

            //Deleting the tables if they exist
            statement.execute("DROP TABLE IF EXISTS userLogins");
            statement.execute("DROP TABLE IF EXISTS users");

            //Creating new empty tables
            statement.execute("CREATE TABLE userLogins (user_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, password_hash TEXT NOT NULL)");
            statement.execute("CREATE TABLE users (id INTEGER PRIMARY KEY, first_name TEXT, last_name TEXT, permission TEXT)");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error resetting test database: " + e.getMessage());
        }
    }

    //Adds a user to the temporary database. Also hashed the password, as this is what the real server does
    public static void addUser(String username, String password) throws Exception {
        String hash = BCrypt.hashpw(password, BCrypt.gensalt()); //Hashing the password

        try {
            Connection connection = DriverManager.getConnection(TEST_DB_URL);
            //SQL statement to insert the user into the logins table
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO userLogins (username, password_hash) VALUES (?, ?)");
            statement.setString(1, username);
            statement.setString(2, hash);

            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error adding user to test database: " + e.getMessage());
        }
    }


    //Creates a request and passes it onto the router and returns the response.
    //This is done to be able to test the server without needing a network.
    public static HttpResponse sendRequest(String requestLine, String body) throws Exception {
        String requestString = requestLine + "\r\n"
                + "Host: localhost\r\n"
                + "Content-Length: " + body.length() + "\r\n"
                + "\r\n"
                + body;

        BufferedReader reader = new BufferedReader(new StringReader(requestString));
        HttpRequest request = new HttpRequest(reader);
        return new Router(reader).routeRequest(request);
    }

    //Check how many rows are in a table, used to check if a user was added or not
    public static int countRows(String table) throws Exception {
        return Integer.parseInt(getValue("SELECT COUNT(*) FROM " + table));
    }


    // Runs a SELECT on the test database and returns the first column of the first row
    public static String getValue(String sql) throws Exception {
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(sql)) {
            results.next();
            return results.getString(1);
        }
    }
}
