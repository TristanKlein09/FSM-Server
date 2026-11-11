package server;

import com.google.gson.Gson;
import models.json.jsonLogin;
import models.json.jsonRegisterUser;
import login.AuthService;
import models.login.User;
import models.server.HttpRequest;
import models.server.HttpResponse;
import models.server.HttpStatus;
import util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

//TODO: HTTPS!!!!
public class HttpServer {
    //Http Server must be a singleton
    private static HttpServer instance;
    private final int port = 9999; //Specifies the port to listen on

    private HttpServer() {}

    /**
     * Starts the server and listens for a connection. If a connection is established, it calls a function to handle the
     * client connection.
     * @throws IOException
     */
    public void startServer() throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("Server started on port " + this.port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                handleClient(clientSocket);
            }

        } catch (Exception e) {
            HttpResponse response = Util.handleException(e, "HttpServer.startServer()");
        }
    }

    //Handles the client connection

    /**
     * Handles the client connection by reading the request, routing it to the appropriate function and then sending
     * the response back to the client.
     * @param clientSocket
     */
    private void handleClient(Socket clientSocket) {
        try {
            //Read message from client
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream outputStream = clientSocket.getOutputStream(); //Get output stream to send data to client

            while (true) {
                Router router = new Router(reader);
                //HttpRequest request = router.createRequestObj();
                HttpRequest request = new HttpRequest(reader);
                HttpResponse response = router.routeRequest(request); //Create the response based on the request
                System.out.println(response.statusLine);

                //Flushing the streams
                try {
                    outputStream.write(response.responseBytes);
                    outputStream.write(response.body);
                    outputStream.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                //Check connection header to see if the connection should close
                if (request.getConnection().equalsIgnoreCase("close")) {
                    System.out.println("Closing connection with client: " + clientSocket.getInetAddress());
                    reader.close();
                    outputStream.close();
                    clientSocket.close();
                    break;
                }

            }

        } catch (Exception e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
    }

    //----- HTTP RESPONSES -----

    /**
     * Handles the entire login sequence including authenticating the user and sending the response back to the client.
     * @param request
     * @return {@link HttpResponse} with status code 202 if the login is successful or 401 if the login is unsuccessful
     * @throws SQLException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public HttpResponse userLogin(HttpRequest request) throws SQLException, ExecutionException, InterruptedException {
        //We can only accept requests with application/json so if no return 400 Bad Request response
        if (!(request.getContentType().equals("application/json"))) {
            return new HttpResponse(HttpStatus.BAD_REQUEST, "text/plain", "keep-alive",
                    "Content-Type must be application/json".getBytes(StandardCharsets.UTF_8));
        }

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

    /**
     * Handles the entire user registration sequence including hashing the password, creating the user in the database
     * and sending the response back to the client.
     * @param request
     * @return {@link HttpResponse} with status code 201 if the user is successfully created or 400 if the request is invalid
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws SQLException
     */
    public HttpResponse registerUser(HttpRequest request) throws ExecutionException, InterruptedException, SQLException {
        //We can only accept requests with application/json so if no return 400 Bad Request response
        if (!(request.getContentType().equals("application/json"))) {
            System.out.println("Content type must be application/json");
            return new HttpResponse(HttpStatus.BAD_REQUEST, "text/plain", "keep-alive",
                    "Content-Type must be application/json".getBytes(StandardCharsets.UTF_8));
        }

        HttpResponse response;

        try {
            AuthService authService = new AuthService();
            Gson gson = new Gson();

            String body = request.getBody();
            jsonRegisterUser userProperties = gson.fromJson(body, jsonRegisterUser.class);

            //Checking if username or password is null or empty, if so return a 400 Bad Request response
            if (userProperties.getUsername() == null || userProperties.getUsername().isEmpty()
                    || userProperties.getPassword() == null || userProperties.getPassword().isEmpty()
                    || userProperties.getFirstName() == null || userProperties.getFirstName().isEmpty()
                    || userProperties.getLastName() == null || userProperties.getLastName().isEmpty()
                    || userProperties.getPermissions() == null)
            {
                return new HttpResponse(HttpStatus.BAD_REQUEST, "text/plain", "keep-alive",
                        "".getBytes(StandardCharsets.UTF_8));
            }

            //Bcrypt truncates the input at 72 bytes, so the password must be less than 72 characters, otherwise return a 400 Bad Request response
            if (userProperties.getPassword().length() > 72) {
                return new HttpResponse(HttpStatus.BAD_REQUEST, "text/plain", "keep-alive",
                        "Password must be less than 72 characters".getBytes(StandardCharsets.UTF_8));
            }

            String passwordHash = Util.hashPlainText(userProperties.getPassword());

            //Replace password with the password hash
            User user = new User(userProperties.getUsername(), passwordHash, userProperties.getFirstName(), userProperties.getLastName(), userProperties.getPermissions());
            user = authService.registerUser(user);

            response = new HttpResponse(HttpStatus.CREATED, "text/plain", "keep-alive", "".getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            response = Util.handleException(e, "HttpServer.registerUser()");
        }

        return response;
    }

    /**
     * The response that is sent when the client wants to check if they can successfully establish a connection with the client.
     * @return {@link HttpResponse} with status code 200 and an empty body.
     */
    public HttpResponse checkConnectionResponse() {
        HttpResponse response;
        response = new HttpResponse(HttpStatus.OK, "text/plain", "keep-alive", "".getBytes(StandardCharsets.UTF_8));
        return response;
    }

    /**
     *
     * @param target The target the user is trying to access.
     * @return {@link HttpResponse} with status code 404 and body "Target: ... not found"
     */
    public HttpResponse unrecognisedTarget(String target) {
        return new HttpResponse(HttpStatus.NOT_FOUND, "text/plain", "keep-alive", ("Target: " + target + " not found").getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Returns a 405 Method Not Allowed response.
     * @param request
     * @return {@link HttpResponse} with status code 405 and body "Method: ... not allowed with target: ..."
     */
    public HttpResponse methodNotAllowed(HttpRequest request) {
        return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED, "text/plain", "keep-alive",
                ("Method: " + request.getMethod() + " not allowed with target: " + request.getTarget()).getBytes(StandardCharsets.UTF_8));
    }

    //Getters and setters
    public static HttpServer getInstance() {
        if (instance == null) {
            instance = new HttpServer();
        }
        return instance;
    }

}
