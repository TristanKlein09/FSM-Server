import database.implementations.SQLDatabaseInitialiser;
import database.interfaces.DatabaseInitialiser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseInitialiserTest {
    private final String url = "jdbc:sqlite:init_test.sqlite";

    @BeforeEach
    void setUp() throws Exception {
        TestHelper.resetDatabase(this.url);
    }

    //Valid: Check that the admin user has been added to both tables
    @Test
    @DisplayName("Di1: Correct initialisation with correct admin user (valid)")
    void correctInit() throws Exception {
        DatabaseInitialiser init = new SQLDatabaseInitialiser();
        init.initDatabase();

        assertEquals("admin", TestHelper.getValueFromDb(this.url,"SELECT username FROM userLogins"));
        assertEquals("Admin", TestHelper.getValueFromDb(this.url, "SELECT first_name FROM users"));
    }

    //Boundary: initDatabase() is called twice, so should only create one copy of the table
    @Test
    @DisplayName("Di2: Creates only one copy of the tables (boundary)")
    void initTwice() throws Exception {
        DatabaseInitialiser init = new SQLDatabaseInitialiser();
        init.initDatabase();
        init.initDatabase();

        assertEquals("admin", TestHelper.getValueFromDb(this.url,"SELECT username FROM userLogins"));
        assertEquals("Admin", TestHelper.getValueFromDb(this.url, "SELECT first_name FROM users"));
    }
}
