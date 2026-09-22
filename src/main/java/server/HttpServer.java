package server;

import models.HttpResponse;
import models.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServer {
    private int port; //Specifies the port to listen on
    static String crlf = "\r\n"; //Specifies the carriage return and line feed characters used in requests

    public HttpServer(int port) {
        this.port = port;
    }

    public void startServer() throws IOException {
        try {
            //Open server socket on the port to listen for connections
            ServerSocket serverSocket = new ServerSocket(this.port);

            //Accept connection from client
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress()); //TODO: change to actual IP address

            //Read message from client
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            RequestHandler requestHandler = new RequestHandler(reader);

            OutputStream outputStream = clientSocket.getOutputStream(); //Get output stream to send data to client

            byte[] body = "Hello World!".getBytes(StandardCharsets.UTF_8); //Splitting it into bytes

            HttpResponse responseObj = new HttpResponse(HttpStatus.OK, "text/plain", "close", body);

            outputStream.write(responseObj.responseBytes);
            outputStream.write(responseObj.body);
            outputStream.flush();

            //Close the sockets
            reader.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage()); //Output the error
        }
    }
}
