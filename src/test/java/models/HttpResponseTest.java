package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HttpResponseTest {

    /*
    private HttpResponse helloResponse() {
        byte[] body = "Hello World!".getBytes(StandardCharsets.UTF_8);
        return new HttpResponse(HttpStatus.OK, "text/plain", "close", body);
    }


    @Test
    @DisplayName("R1: 200 status line is correct")
    void statusLine_ok_isCorrect() {
        assertEquals("HTTP/1.1 200 OK\r\n", helloResponse().statusLine);
    }

    @Test
    @DisplayName("R2: 404 status line is correct")
    void statusLine_notFound_isCorrect() {
        HttpResponse r = new HttpResponse(HttpStatus.NOT_FOUND, "text/plain", "close", new byte[0]);
        assertEquals("HTTP/1.1 404 Not Found\r\n", r.statusLine);
    }

     */

    /*
    @Test
    @DisplayName("R3: Content-Type and Content-Length headers are present and correct")
    void response_containsContentHeaders() {
        String response = helloResponse().response;
        assertTrue(response.contains("Content-Type: text/plain\r\n"));
        assertTrue(response.contains("Content-Length: 12\r\n"));
    }

    @Test
    @DisplayName("R4: headers end with a blank line")
    void response_endsWithBlankLine() {
        assertTrue(helloResponse().response.endsWith("\r\n\r\n"));
    }

    @Test
    @DisplayName("R5: responseBytes matches the response string")
    void responseBytes_matchesResponseString() {
        HttpResponse r = helloResponse();
        assertArrayEquals(r.response.getBytes(StandardCharsets.UTF_8), r.responseBytes);
    }

    @Test
    @DisplayName("R6: empty body gives Content-Length 0 (boundary)")
    void emptyBody_contentLengthIsZero() {
        HttpResponse r = new HttpResponse(HttpStatus.NO_CONTENT, "text/plain", "close", new byte[0]);
        assertEquals(0, r.contentLength);
        assertTrue(r.response.contains("Content-Length: 0\r\n"));
    }

    @Test
    @DisplayName("R7: null body does not crash (erroneous)")
    void nullBody_doesNotCrash() {
        assertDoesNotThrow(() -> new HttpResponse(HttpStatus.OK, "text/plain", "close", null));
    }

    // KNOWN FAULT: the Connection value is stored but never written into the response.
    // This test is expected to FAIL until the code is fixed.
    @Test
    @DisplayName("R8: Connection header is included in the response")
    void response_containsConnectionHeader() {
        assertTrue(helloResponse().response.contains("Connection: close\r\n"));
    }

     */
}
