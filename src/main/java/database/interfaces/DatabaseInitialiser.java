package database.interfaces;

import java.sql.SQLException;

public interface DatabaseInitialiser {
    //Initialising the tables related to the users and their logins.
    void initDatabase() throws SQLException;
}
