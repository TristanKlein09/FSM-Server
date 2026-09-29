package server;

import models.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//Test not in use

class RequestHandlerTest {

    @Test
    @DisplayName("H1: RequestHandler builds an HttpRequest object")
    void createRequestObj_returnsRequestObject() {
        BufferedReader reader = new BufferedReader(new StringReader("GET / HTTP/1.1\r\n\r\n"));
        RequestHandler handler = new RequestHandler(reader);
        HttpRequest request = handler.createRequestObj();
        assertNotNull(request);
    }
}
