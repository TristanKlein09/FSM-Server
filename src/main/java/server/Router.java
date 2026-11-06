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

    //TODO: This is completely useless - fix it
    public HttpRequest createRequestObj() {
        return new HttpRequest(reader);
    }

    //TODO: Have it do it correctly based on the method GET, POST etc
    //TODO: Make this be POST
    //TODO: Check if the break can be put outside the if statement

    //Takes in the target of a request and calls the corresponding function
    public HttpResponse routeRequest(HttpRequest request) throws SQLException, ExecutionException, InterruptedException {
        HttpServer server = HttpServer.getInstance();
        String target = request.getTarget();
        HttpResponse response = null;

        switch (target) {
            case "/models/login":
                if (request.getMethod().equals("POST")) { //Only allow POST requests to login
                    response = server.userLogin(request);
                    break;
                } else {
                    response = server.methodNotAllowed(request);
                    break;
                }
            case "/models/registerUser":
                if (request.getMethod().equals("POST")) { //Only allow POST requests to registerUser
                    response = server.registerUser(request);
                    break;
                } else {
                    response = server.methodNotAllowed(request);
                    break;
                }
            case "/models/checkConnection":
                if (request.getMethod().equals("GET")) { //Only allow GET requests to checkConnection
                    response = server.checkConnectionResponse();
                    break;
                } else {
                    response = server.methodNotAllowed(request);
                    break;
                }
            default:
                System.out.println("Error in Router.routeRequest(): Unrecognised target");
                System.out.println(target);
                response = server.unrecognisedTarget(target);
        }

        return response;
    }

}
