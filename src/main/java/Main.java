import server.RequestHandler;
import util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

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

            //Close the sockets
            reader.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage()); //Output the error
        }
    }
}


