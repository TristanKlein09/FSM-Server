import models.server.HttpResponse;
import models.server.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginTest {

    //Runs before every test to add empty tables and one user: BatMan with password "password" to log in as
    @BeforeEach
    void setUp() throws Exception {
        TestHelper.resetDatabase();
        TestHelper.addUser("BatMan", "password");
    }

    //Valid: correct username and password are accepted
    @Test
    @DisplayName("L1: Correct credentials returns 202 and empty body (valid)")
    void correctCredentials() throws Exception {
        String body = "{\"username\":\"BatMan\",\"password\":\"password\"}";

        HttpResponse response = TestHelper.sendRequest("POST /models/login HTTP/1.1", body);

        assertEquals(HttpStatus.ACCEPTED, response.status);
        assertEquals(0, response.body.length);
    }

    //Erroneous: right username and wrong password is not accepted
    @Test
    @DisplayName("L2: Wrong password returns 401 and empty body (erroneous)")
    void wrongPassword() throws Exception {
        String body = "{\"username\":\"BatMan\",\"password\":\"password123\"}";

        HttpResponse response = TestHelper.sendRequest("POST /models/login HTTP/1.1", body);

        assertEquals(HttpStatus.UNAUTHORIZED, response.status);
        assertEquals(0, response.body.length);
    }

    //Erroneous: wrong username (no account exists) is no accepted
    @Test
    @DisplayName("L3: Unknown username returns 401 and empty body (erroneous)")
    void unknownUsername() throws Exception {
        String body = "{\"username\":\"Joker\",\"password\":\"password123\"}";

        HttpResponse response = TestHelper.sendRequest("POST /models/login HTTP/1.1", body);

        assertEquals(HttpStatus.UNAUTHORIZED, response.status);
        assertEquals(0, response.body.length);
    }
}

