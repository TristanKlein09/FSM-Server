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
    @DisplayName("Q1: valid GET request line is split correctly")
    void parseRequestLineValidGET() {
        HttpRequest r = parse("GET /index.html HTTP/1.1" + CRLF + CRLF);
        assertEquals("GET", r.getMethod());
        assertEquals("/index.html", r.getTarget());
        assertEquals("HTTP/1.1", r.getHttpVersion());
    }

    /*
    @Test
    @DisplayName("Q2: valid POST request line is split correctly")
    void parseRequestLine_validPost_setsMethod() {
        HttpRequest r = parse("POST /submit HTTP/1.1" + CRLF + CRLF);
        assertEquals("POST", r.getMethod());
        assertEquals("/submit", r.getTarget());
    }

     */

    @Test
    @DisplayName("Q2: request line with missing parts does not crash (erroneous)")
    void parseRequestLineMissingParts() {
        HttpRequest r = assertDoesNotThrow(() -> parse("GET" + CRLF + CRLF));
        assertEquals("GET", r.getMethod());
        assertNull(r.getTarget());
    }

    @Test
    @DisplayName("Q3: empty request does not crash (erroneous)")
    void parseRequestEmptyInput() {
        HttpRequest r = assertDoesNotThrow(() -> parse(""));
        assertNull(r.getMethod());
    }

    //Headers

    @Test
    @DisplayName("Q4: known headers are stored")
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

    /*
    @Test
    @DisplayName("Q6: unknown header is still kept in the headers list")
    void parseHeaders_unknownHeader_isKeptInHeaderList() {
        HttpRequest r = parse("GET / HTTP/1.1" + CRLF + "X-Test: 1" + CRLF + CRLF);
        assertTrue(r.getHeaders().contains("X-Test: 1"));
    }

     */

    // KNOWN FAULT: split(":")[1] cuts the value at the second colon, so the port is lost.
    @Test
    @DisplayName("Q5: Host header keeps its port number (boundary)")
    void parseHeadersHostKeepsPortNumber() {
        HttpRequest r = parse("GET / HTTP/1.1" + CRLF + "Host: localhost:6173" + CRLF + CRLF);
        assertEquals("localhost:6173", r.getHost());
    }

    /*
    // KNOWN FAULT: the switch statement is case-sensitive, but HTTP header names are not.
    @Test
    @DisplayName("Q7: header names are case-insensitive (boundary)")
    void parseHeaders_lowercaseName_isRecognised() {
        HttpRequest r = parse("GET / HTTP/1.1" + CRLF + "host: localhost" + CRLF + CRLF);
        assertEquals("localhost", r.getHost());
    }

     */

    // KNOWN FAULT: "Host:" with no value throws inside parseHeaders, which stops
    // the loop, so every header after it is silently skipped.
    @Test
    @DisplayName("Q6: a header with no value does not stop later headers being read (erroneous)")
    void parseHeadersHeaderNoValue() {
        HttpRequest r = parse("GET / HTTP/1.1" + CRLF
                + "Host:" + CRLF
                + "User-Agent: test" + CRLF
                + CRLF);
        assertEquals("test", r.getUserAgent());
    }

    //Body
    @Test
    @DisplayName("Q7: body is read using Content-Length")
    void parseBodyValid() {
        HttpRequest r = parse("POST / HTTP/1.1" + CRLF
                + "Content-Length: 4" + CRLF
                + CRLF
                + "test");
        assertEquals("test", r.getBody());
    }

    @Test
    @DisplayName("Q8: only Content-Length characters are read (boundary)")
    void parseBodyContentLengthShorterThanBody() {
        HttpRequest r = parse("POST / HTTP/1.1" + CRLF
                + "Content-Length: 3" + CRLF
                + CRLF
                + "test");
        assertEquals("tes", r.getBody());
    }

    @Test
    @DisplayName("Q9: non-numeric Content-Length does not crash (erroneous)")
    void parseBodyNonNumericContentLength() {
        HttpRequest r = assertDoesNotThrow(() -> parse("POST / HTTP/1.1" + CRLF
                + "Content-Length: abc" + CRLF
                + CRLF
                + "hello"));
        assertNull(r.getBody());
    }

    /*
    @Test
    @DisplayName("Q11: GET request with no body leaves body null")
    void parseBody_noContentLength_bodyIsNull() {
        HttpRequest r = parse("GET / HTTP/1.1" + CRLF + CRLF);
        assertNull(r.getBody());
    }

     */


/*
    @Test
    @DisplayName("Q13: Content-Length of 0 gives an empty body (boundary)")
    void parseBody_contentLengthZero_emptyBody() {
        HttpRequest r = parse("POST / HTTP/1.1" + CRLF
                + "Content-Length: 0" + CRLF
                + CRLF);
        assertEquals("", r.getBody());
    }

 */

}
