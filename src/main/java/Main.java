import models.HttpResponse;
import models.HttpStatus;
import server.RequestHandler;
import util.Util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

//TODO: Add enums for the methods

public class Main {
     static int port = 9191; //Specifies the port to listen on
     static String crlf = "\r\n"; //Specifies the carriage return and line feed characters used in requests

    public static void main(String[] args) {
        try {
            System.out.println("Server boot");

            //Open server socket on the port to listen for connections
            ServerSocket serverSocket = new ServerSocket(port);

            //Accept connection from client
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            //Read message from client
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            RequestHandler requestHandler = new RequestHandler(reader);

            OutputStream outputStream = clientSocket.getOutputStream(); //Get output stream to send data to client

            byte[] body = "Hello World!".getBytes(StandardCharsets.UTF_8); //Splitting it into bytes

            String response1 =
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "Content-Length: " + body.length + "\r\n" +
                            "\r\n";

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


