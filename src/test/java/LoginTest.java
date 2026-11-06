import models.server.HttpResponse;
import models.server.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginTest {

    // Runs before every test: empty tables, then one user (BatMan, password "password") to log in as
    @BeforeEach
    void setUp() throws Exception {
        TestHelper.resetDatabase();
        TestHelper.addUser("BatMan", "password");
    }

    // L1 - Valid: correct username and password are accepted
    @Test
    void L1_correctCredentials_returns202AndEmptyBody() throws Exception {
        // Arrange
        String body = "{\"username\":\"BatMan\",\"password\":\"password\"}";

        // Act
        HttpResponse response = TestHelper.sendRequest("POST /models/login HTTP/1.1", body);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.status);
        assertEquals(0, response.body.length);
    }

    // L2 - Invalid: right username, wrong password is refused
    @Test
    void L2_wrongPassword_returns401AndEmptyBody() throws Exception {
        // Arrange
        String body = "{\"username\":\"BatMan\",\"password\":\"password123\"}";

        // Act
        HttpResponse response = TestHelper.sendRequest("POST /models/login HTTP/1.1", body);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.status);
        assertEquals(0, response.body.length);
    }

    // L3 - Invalid: a username with no account is refused
    @Test
    void L3_unknownUsername_returns401AndEmptyBody() throws Exception {
        // Arrange
        String body = "{\"username\":\"Joker\",\"password\":\"password123\"}";

        // Act
        HttpResponse response = TestHelper.sendRequest("POST /models/login HTTP/1.1", body);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.status);
        assertEquals(0, response.body.length);
    }
}