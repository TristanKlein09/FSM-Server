package database.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnectionProvider {
    //Provide a connection to the users database
    public Connection connectUsersDb() throws SQLException;
}
