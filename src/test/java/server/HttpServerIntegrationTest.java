package server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;


class HttpServerIntegrationTest {
    private static final String CRLF = "\r\n";

    //An integration server which starts a server and sends correct response
    @Test
    @DisplayName("I1: The server responds with a valid response containing 200 OK (valid)")
    void serverRespondsToRequestValid() throws Exception {
        //A new thread must be created because starting the server blocks the main thread
        //meaning the test wont be able to run
        Thread serverThread = new Thread(() -> { //Creating a new thread
            try {
                new HttpServer(6173).startServer();
            } catch (IOException e) {
                System.out.println("An error occurred while starting the server" + e.getMessage());
            }
        });

        serverThread.start(); //Actually starting the new thread

        Socket testSocket = new Socket("localhost", 6173);

        //Simulating a request that a console would send by using the output stream
        OutputStream outputStream = testSocket.getOutputStream();
        outputStream.write(("GET / HTTP/1.1" + CRLF + "Host: localhost" + CRLF + CRLF).getBytes(StandardCharsets.UTF_8));

        //Getting response from the server
        String response = new String(testSocket.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        assertTrue(response.startsWith("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Content-Length: 4"));
        assertTrue(response.endsWith("Test"));
    }
}