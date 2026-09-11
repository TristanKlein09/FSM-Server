package server;

import models.HttpRequest;

import java.io.BufferedReader;

public class RequestHandler {
    private BufferedReader reader;

    public RequestHandler(BufferedReader reader) {
        this.reader = reader;

        HttpRequest request = createRequestObj();
    }



    public HttpRequest createRequestObj() {
        return new HttpRequest(reader);
    }
}
