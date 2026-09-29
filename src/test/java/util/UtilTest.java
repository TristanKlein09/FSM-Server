package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilTest {
    @Test
    @DisplayName("U1: A normal body returns correct length in bytes (valid)")
    void contentLengthValidTest() {
        byte[] body = "Test".getBytes(StandardCharsets.UTF_8);
        assertEquals(4, Util.contentLength(body));
    }

    @Test
    @DisplayName("U2: An empty body returns 0 bytes (boundary)")
    void contentLengthEmptyBody() {
        assertEquals(0, Util.contentLength(new byte[0]));
    }

    @Test
    @DisplayName("U3: A null body returns 0 bytes and does not crash (erroneous)")
    void contentLengthNullBody() {
        assertEquals(0, Util.contentLength(null));
    }

}


