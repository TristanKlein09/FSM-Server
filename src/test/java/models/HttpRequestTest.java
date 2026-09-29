package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {
    private static final String CRLF = "\r\n";

    //Turns a String into a request, so no real network connection is needed
    private HttpRequest parse(String rawRequest) {
        return new HttpRequest(new BufferedReader(new StringReader(rawRequest)));
    }

    //Request line
    @Test
    @DisplayName("Q1: GET request line is split correctly (valid)")
    void parseRequestLineValidGET() {
        HttpRequest r = parse("GET /index.html HTTP/1.1" + CRLF + CRLF);
        assertEquals("GET", r.getMethod());
        assertEquals("/index.html", r.getTarget());
        assertEquals("HTTP/1.1", r.getHttpVersion());
    }

    @Test
    @DisplayName("Q2: Request line with missing parts does not crash (erroneous)")
    void parseRequestLineMissingParts() {
        HttpRequest r = assertDoesNotThrow(() -> parse("GET" + CRLF + CRLF));
        assertEquals("GET", r.getMethod());
        assertNull(r.getTarget());
    }

    @Test
    @DisplayName("Q3: An empty request does not crash (erroneous)")
    void parseRequestEmptyInput() {
        HttpRequest r = assertDoesNotThrow(() -> parse(""));
        assertNull(r.getMethod());
    }

    //Headers
    @Test
    @DisplayName("Q4: The known headers are stored correctly (valid)")
    void parseHeadersValid() {
        HttpRequest r = parse("GET / HTTP/1.1" + CRLF
                + "Host: localhost" + CRLF
                + "User-Agent: test" + CRLF
                + "Accept: text/html" + CRLF
                + "Connection: close" + CRLF
                + CRLF);
        assertEquals("localhost", r.getHost());
        assertEquals("test", r.getUserAgent());
        assertEquals("text/html", r.getAccept());
        assertEquals("close", r.getConnection());
    }

    // KNOWN FAULT: split(":")[1] cuts the value at the second colon, so the port is lost.
    @Test
    @DisplayName("Q5: The host header keeps its port number (boundary)")
    void parseHeadersHostKeepsPortNumber() {
        HttpRequest r = parse("GET / HTTP/1.1" + CRLF + "Host: localhost:6173" + CRLF + CRLF);
        assertEquals("localhost:6173", r.getHost());
    }

    // KNOWN FAULT: "Host:" with no value throws inside parseHeaders, which stops
    @Test
    @DisplayName("Q6: A header with no value doesn't stop the other headers from being read correctly (erroneous)")
    void parseHeadersHeaderNoValue() {
        HttpRequest r = parse("GET / HTTP/1.1" + CRLF
                + "Host:" + CRLF
                + "User-Agent: test" + CRLF
                + CRLF);
        assertEquals("test", r.getUserAgent());
    }


    //Body
    @Test
    @DisplayName("Q7: The body is read correctly (valid)")
    void parseBodyValid() {
        HttpRequest r = parse("POST / HTTP/1.1" + CRLF
                + "Content-Length: 4" + CRLF
                + CRLF
                + "test");
        assertEquals("test", r.getBody());
    }

    @Test
    @DisplayName("Q8: The body that is read is the exact length that content-length specifies (boundary)")
    void parseBodyContentLengthShorterThanBody() {
        HttpRequest r = parse("POST / HTTP/1.1" + CRLF
                + "Content-Length: 3" + CRLF
                + CRLF
                + "test");
        assertEquals("tes", r.getBody());
    }

    @Test
    @DisplayName("Q9: A non-numeric content-length does not crash (erroneous)")
    void parseBodyNonNumericContentLength() {
        HttpRequest r = assertDoesNotThrow(() -> parse("POST / HTTP/1.1" + CRLF
                + "Content-Length: abc" + CRLF
                + CRLF
                + "test"));
        assertNull(r.getBody());
    }

}
