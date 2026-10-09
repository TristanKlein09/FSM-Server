package server;

import models.HttpRequest;
import models.HttpResponse;

import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class Router {
    private BufferedReader reader;

    public Router(BufferedReader reader) {
        this.reader = reader;

        HttpRequest request = createRequestObj();
    }

    //TODO: This is completely useless - fix it
    public HttpRequest createRequestObj() {
        return new HttpRequest(reader);
    }

    //Takes in the target of a request and calls the corresponding function
    public HttpResponse routeRequest(HttpRequest request) throws SQLException, ExecutionException, InterruptedException {
        HttpServer server = HttpServer.getInstance();
        String target = request.getTarget();
        HttpResponse response = null;
        //TODO: Have it do it correctly based on the method GET, POST etc
        switch (target) {
            case "/login":
                //TODO: Make this be POST
                response = server.userLogin(request);
                break;
            case "/registerUser":
                server.registerUser(request);
                break;
            default:
                System.out.println("Error in Router.routeRequest(): Unrecognised target");
        }

        return response;
    }

}
