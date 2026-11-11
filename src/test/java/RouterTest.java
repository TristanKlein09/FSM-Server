import models.server.HttpResponse;
import models.server.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouterTest {

    @Test
    @DisplayName("Ro1: checkConnection returns 200 and empty body (valid)")
    void checkConnection() throws Exception {
        HttpResponse response = TestHelper.sendRequest("GET /models/checkConnection HTTP/1.1", "");

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(0, response.getBody().length);
    }

    //Invalid: login only accepts POST, so GET should be refused
    @Test
    @DisplayName("Ro2: GET /login returns 405 (invalid)")
    void incorrectMethodLogin() throws Exception {
        HttpResponse response = TestHelper.sendRequest("GET /models/login HTTP/1.1", "");

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatus());
    }

    //Erroneous: an unknown target gets a 404 and an error message
    @Test
    @DisplayName("Ro3: unknown target returns 404 with message")
    void unknownTarget() throws Exception {
        HttpResponse response = TestHelper.sendRequest("GET /doesntExist HTTP/1.1", "");

        String body = new String(response.getBody(), StandardCharsets.UTF_8);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Target: /doesntExist not found", body);
    }
}

