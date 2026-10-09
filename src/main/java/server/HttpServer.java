package server;

import com.google.gson.Gson;
import json.jsonLogin;
import login.AuthService;
import models.HttpRequest;
import models.HttpResponse;
import models.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class HttpServer {
    //Http Server must be a singleton
    private static HttpServer instance;
    private final int port = 9999; //Specifies the port to listen on

    private HttpServer() {}

    public void startServer() throws IOException {
        try {
            //Open server socket on the port to listen for connections
            ServerSocket serverSocket = new ServerSocket(this.port);

            //Accept connection from client
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            //Read message from client
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Router router = new Router(reader);
            HttpRequest request = router.createRequestObj();

            OutputStream outputStream = clientSocket.getOutputStream(); //Get output stream to send data to client

            HttpResponse response = router.routeRequest(request);

            outputStream.write(response.responseBytes);
            outputStream.write(response.body);
            outputStream.flush();

            //Close the sockets
            reader.close();
            clientSocket.close();
            serverSocket.close();
            //TODO: Add print statements to the exceptions
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage()); //Output the error
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO: This only works if the specified content type is json
    public HttpResponse userLogin(HttpRequest request) throws SQLException, ExecutionException, InterruptedException {
        HttpResponse response;
        AuthService authService = new AuthService();
        Gson gson = new Gson();

        String body = request.getBody();
        jsonLogin credentials = gson.fromJson(body, jsonLogin.class);
        boolean valid = authService.authenticate(credentials.getUsername(), credentials.getPassword());

        //Login failed
        if (!valid) {
            response = new HttpResponse(HttpStatus.UNAUTHORIZED, "text/plain", "keep-alive", "".getBytes(StandardCharsets.UTF_8));
        } else {
            response = new HttpResponse(HttpStatus.ACCEPTED, "text/plain", "keep-alive", "".getBytes(StandardCharsets.UTF_8));
        }

        return response;
    }

    public void registerUser(HttpRequest request) {

    }

    //Getters and setters
    public static HttpServer getInstance() {
        if (instance == null) {
            instance = new HttpServer();
        }
        return instance;
    }

}
