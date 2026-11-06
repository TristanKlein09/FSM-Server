import models.server.HttpResponse;
import models.server.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RegistrationTest {

    //Runs before every test and resets the database so that the tables are empty
    @BeforeEach
    void setUp() throws Exception {
        TestHelper.resetDatabase();
    }

    //Valid: a new user is saved correctly and the password is stored as a hash
    @Test
    @DisplayName("Re1: Valid registration returns 201 and saves user with hashed password (valid)")
    void validRegistration() throws Exception {
        String body = "{\"username\":\"Robin\",\"password\":\"password123\","
                + "\"firstName\":\"Richard\",\"lastName\":\"Grayson\",\"permissions\":\"TECHNICIAN\"}";

        HttpResponse response = TestHelper.sendRequest("POST /models/registerUser HTTP/1.1", body);

        assertEquals(HttpStatus.CREATED, response.status);
        //Checking that every entry in the tables are correct
        assertEquals("Robin", TestHelper.getValue("SELECT username FROM userLogins"));
        assertEquals("Richard", TestHelper.getValue("SELECT first_name FROM users"));
        assertEquals("Grayson", TestHelper.getValue("SELECT last_name FROM users"));
        assertEquals("Technician", TestHelper.getValue("SELECT permission FROM users"));
        //Checking that the password has been hashed
        assertNotEquals("password123", TestHelper.getValue("SELECT password_hash FROM userLogins"));
    }

    //Invalid: a missing username is refused and nothing is saved
    @Test
    @DisplayName("Re2.1: Missing username returns 400 and saves nothing (invalid)")
    void noUsername() throws Exception {
        String body = "{\"password\":\"password123\","
                + "\"firstName\":\"Richard\",\"lastName\":\"Grayson\",\"permissions\":\"TECHNICIAN\"}";

        HttpResponse response = TestHelper.sendRequest("POST /models/registerUser HTTP/1.1", body);

        assertEquals(HttpStatus.BAD_REQUEST, response.status);
        assertEquals(0, TestHelper.countRows("userLogins"));
        assertEquals(0, TestHelper.countRows("users"));
    }

    //Invalid: a missing password is refused and nothing is saved
    @Test
    @DisplayName("Re2.2: Missing password returns 400 and saves nothing (invalid)")
    void noPassword() throws Exception {
        String body = "{\"username\":\"Robin\"," +
                "\"firstName\":\"Richard\",\"lastName\":\"Grayson\",\"permissions\":\"TECHNICIAN\"}";

        HttpResponse response = TestHelper.sendRequest("POST /models/registerUser HTTP/1.1", body);

        assertEquals(HttpStatus.BAD_REQUEST, response.status);
        assertEquals(0, TestHelper.countRows("userLogins"));
        assertEquals(0, TestHelper.countRows("users"));
    }
}

