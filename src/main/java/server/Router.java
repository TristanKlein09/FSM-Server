package server;

import models.server.HttpRequest;
import models.server.HttpResponse;

import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class Router {
    private BufferedReader reader;

    public Router(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Routes the request to the appropriate function based on the target of the request. It also checks if the method
     * is appropriate for the target, if not it returns a 405 Method Not Allowed response.
     * @param request
     * @return {@link HttpResponse} object containing the response to the request
     * @throws SQLException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public HttpResponse routeRequest(HttpRequest request) throws SQLException, ExecutionException, InterruptedException {
        HttpServer server = HttpServer.getInstance();
        String target = request.getTarget();
        HttpResponse response = null;

        switch (target) {
            case "/models/login":
                if (request.getMethod().equals("POST")) { //Only allow POST requests to login
                    response = server.userLogin(request);
                } else {
                    response = server.methodNotAllowed(request);
                }
                break;
            case "/models/registerUser":
                if (request.getMethod().equals("POST")) { //Only allow POST requests to registerUser
                    response = server.registerUser(request);
                } else {
                    response = server.methodNotAllowed(request);
                }
                break;
            case "/models/checkConnection":
                if (request.getMethod().equals("GET")) { //Only allow GET requests to checkConnection
                    response = server.checkConnectionResponse();
                } else {
                    response = server.methodNotAllowed(request);
                }
                break;
            default:
                System.out.println("Error in Router.routeRequest(): Unrecognised target");
                System.out.println(target);
                response = server.unrecognisedTarget(target);
        }

        return response;
    }

}
