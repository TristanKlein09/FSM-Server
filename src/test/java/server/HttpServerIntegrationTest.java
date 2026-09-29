package server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

// Integration test - starts the real server and sends it a real request.
// Port 6173 needs to be free, so make sure the server isn't already running.
class HttpServerIntegrationTest {

    @Test
    @DisplayName("I1: server responds to a GET request with 200 OK")
    void serverRespondsToGetRequest() throws Exception {
        // startServer() blocks, so it needs to run on its own thread
        Thread serverThread = new Thread(() -> {
            try {
                new HttpServer(6173).startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        Thread.sleep(500); // give the server a moment to start listening

        Socket socket = new Socket("localhost", 6173);
        OutputStream out = socket.getOutputStream();
        out.write("GET / HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes(StandardCharsets.UTF_8));
        out.flush();

        String response = new String(socket.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        socket.close();

        assertTrue(response.startsWith("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Content-Length: 4"));
        assertTrue(response.endsWith("Test"));
    }
}
