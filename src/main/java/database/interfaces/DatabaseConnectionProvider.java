package database.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnectionProvider {
    //Provide a connection to the specified database
    Connection connectToDb(String url) throws SQLException;
}
