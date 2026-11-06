import models.server.HttpResponse;
import models.server.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RegistrationTest {

    // Runs before every test, so each test starts with empty tables
    @BeforeEach
    void setUp() throws Exception {
        TestHelper.resetDatabase();
    }

    // Re1 - Valid: a new user is saved, and the password is stored hashed
    @Test
    void Re1_validRegistration_savesUserWithHashedPassword() throws Exception {
        // Arrange
        String body = "{\"username\":\"Robin\",\"password\":\"password123\","
                + "\"firstName\":\"Richard\",\"lastName\":\"Grayson\",\"permissions\":\"TECHNICIAN\"}";

        // Act
        HttpResponse response = TestHelper.sendRequest("POST /models/registerUser HTTP/1.1", body);

        // Assert
        assertEquals(HttpStatus.CREATED, response.status);
        assertEquals("Robin", TestHelper.getValue("SELECT username FROM userLogins"));
        assertEquals("Richard", TestHelper.getValue("SELECT first_name FROM users"));
        assertEquals("Grayson", TestHelper.getValue("SELECT last_name FROM users"));
        assertEquals("Technician", TestHelper.getValue("SELECT permission FROM users"));
        assertNotEquals("password123", TestHelper.getValue("SELECT password_hash FROM userLogins"));
    }

    // Re2.1 - Invalid: a missing username is refused and nothing is saved
    @Test
    void Re2_1_noUsername_returns400AndSavesNothing() throws Exception {
        // Arrange (same as Re1 but with no username)
        String body = "{\"password\":\"password123\","
                + "\"firstName\":\"Richard\",\"lastName\":\"Grayson\",\"permissions\":\"TECHNICIAN\"}";

        // Act
        HttpResponse response = TestHelper.sendRequest("POST /models/registerUser HTTP/1.1", body);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.status);
        assertEquals(0, TestHelper.countRows("userLogins"));
        assertEquals(0, TestHelper.countRows("users"));
    }
}