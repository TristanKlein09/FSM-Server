package server;

import com.google.gson.Gson;
import models.json.jsonLogin;
import models.json.jsonRegisterUser;
import login.AuthService;
import models.login.User;
import models.network.HttpRequest;
import models.network.HttpResponse;
import models.network.HttpStatus;
import models.network.response.Response;
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
    private final int port = 9998; //Specifies the port to listen on

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
            Response response = Util.handleException(e, "HttpServer.startServer()");
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
                System.out.println(request.getRequestLine());
                System.out.println(request.getContentType());
                System.out.println(request.getBody());
                Response response = router.routeRequest(request); //Create the response based on the request
                System.out.println(response.getStatusLine());

                //Flushing the streams
                try {
                    outputStream.write(response.getResponseBytes());
                    outputStream.write(response.getBody());
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
    public Response userLogin(HttpRequest request) throws SQLException, ExecutionException, InterruptedException {
        //We can only accept requests with application/json so if no return 400 Bad Request response
        if (!(request.getContentType().contains("application/json"))) {
//            return new HttpResponse(HttpStatus.BAD_REQUEST, "text/plain", "keep-alive",
//                    "Content-Type must be application/json".getBytes(StandardCharsets.UTF_8));
            return new Response.Builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Content-Type must be application/json".getBytes(StandardCharsets.UTF_8))
                    .build();
        }

        Response response;
        AuthService authService = new AuthService();
        Gson gson = new Gson();

        String body = request.getBody();
        jsonLogin credentials = gson.fromJson(body, jsonLogin.class);
        boolean valid = authService.authenticate(credentials.getUsername(), credentials.getPassword());

        //Login failed
        if (!valid) {
            //response = new HttpResponse(HttpStatus.UNAUTHORIZED, "text/plain", "keep-alive", "".getBytes(StandardCharsets.UTF_8));
            response = new Response.Builder()
                    .status(HttpStatus.UNAUTHORIZED).build();
        } else {
            //response = new HttpResponse(HttpStatus.ACCEPTED, "text/plain", "keep-alive", "".getBytes(StandardCharsets.UTF_8));
            response = new Response.Builder()
                    .status(HttpStatus.ACCEPTED).build();
        }

        return response;
    }

    /**
     * Handles the entire user registration sequence including hashing the password, creating the user in the database
     * and sending the response back to the client.
     * @param request
     * @return {@link Response} with status code 201 if the user is successfully created or 400 if the request is invalid
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws SQLException
     */
    public Response registerUser(HttpRequest request) throws ExecutionException, InterruptedException, SQLException {
        //We can only accept requests with application/json so if no return 400 Bad Request response
        if (!(request.getContentType().contains("application/json"))) {
            System.out.println("Content type must be application/json");
//            return new HttpResponse(HttpStatus.BAD_REQUEST, "text/plain", "keep-alive",
//                    "Content-Type must be application/json".getBytes(StandardCharsets.UTF_8));
            return new Response.Builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Content-Type must be application/json".getBytes(StandardCharsets.UTF_8)).build();
        }

        Response response;

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
//                return new HttpResponse(HttpStatus.BAD_REQUEST, "text/plain", "keep-alive",
//                        "".getBytes(StandardCharsets.UTF_8));
                return new Response.Builder()
                        .status(HttpStatus.BAD_REQUEST).build();
            }

            //Bcrypt truncates the input at 72 bytes, so the password must be less than 72 characters, otherwise return a 400 Bad Request response
            if (userProperties.getPassword().length() > 72) {
//                return new HttpResponse(HttpStatus.BAD_REQUEST, "text/plain", "keep-alive",
//                        "Password must be less than 72 characters".getBytes(StandardCharsets.UTF_8));
                return new Response.Builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Password must be less than 72 characters".getBytes(StandardCharsets.UTF_8)).build();
            }

            String passwordHash = Util.hashPlainText(userProperties.getPassword());

            //Replace password with the password hash
            User user = new User(userProperties.getUsername(), passwordHash, userProperties.getFirstName(), userProperties.getLastName(), userProperties.getPermissions());
            user = authService.registerUser(user);

//            response = new HttpResponse(HttpStatus.CREATED, "text/plain", "keep-alive", "".getBytes(StandardCharsets.UTF_8));
            response = new Response.Builder()
                    .status(HttpStatus.CREATED).build();

        } catch (Exception e) {
            response = Util.handleException(e, "HttpServer.registerUser()");
        }

        return response;
    }

    /**
     * The response that is sent when the client wants to check if they can successfully establish a connection with the client.
     * @return {@link Response} with status code 200 and an empty body.
     */
    public Response checkConnectionResponse() {
//        HttpResponse response;
//        response = new HttpResponse(HttpStatus.OK, "text/plain", "keep-alive", "".getBytes(StandardCharsets.UTF_8));
//        return response;
        return new Response.Builder()
                .status(HttpStatus.OK).build();
    }

    /**
     *
     * @param target The target the user is trying to access.
     * @return {@link Response} with status code 404 and body "Target: ... not found"
     */
    public Response unrecognisedTarget(String target) {
//        return new HttpResponse(HttpStatus.NOT_FOUND, "text/plain", "keep-alive", ("Target: " + target + " not found").getBytes(StandardCharsets.UTF_8));
        return new Response.Builder()
                .status(HttpStatus.NOT_FOUND)
                .body(("Target: " + target + " not found").getBytes(StandardCharsets.UTF_8)).build();
    }

    /**
     * Returns a 405 Method Not Allowed response.
     * @param request
     * @return {@link Response} with status code 405 and body "Method: ... not allowed with target: ..."
     */
    public Response methodNotAllowed(HttpRequest request) {
//        return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED, "text/plain", "keep-alive",
//                ("Method: " + request.getMethod() + " not allowed with target: " + request.getTarget()).getBytes(StandardCharsets.UTF_8));
        return new Response.Builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(("Method: " + request.getMethod() + " not allowed with target: " + request.getTarget()).getBytes(StandardCharsets.UTF_8))
                .build();
    }

    //Getters and setters
    public static HttpServer getInstance() {
        if (instance == null) {
            instance = new HttpServer();
        }
        return instance;
    }

}
