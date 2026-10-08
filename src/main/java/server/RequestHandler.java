package server;

import models.HttpRequest;

import java.io.BufferedReader;

public class RequestHandler {
    private BufferedReader reader;

    public RequestHandler(BufferedReader reader) {
        this.reader = reader;

        HttpRequest request = createRequestObj();
    }

    //TODO: This is completely useless - fix it
    public HttpRequest createRequestObj() {
        return new HttpRequest(reader);
    }

    //TODO: Create a function that takes in the target of the request and executes code based on that e.g the /login is for when a user needs to log in
}
