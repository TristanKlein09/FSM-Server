package models;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpRequest {
    private BufferedReader reader;

    private String request;

    //Request line
    private String requestLine;
    private String method; //GET, POST, etc.
    private String target; //URL
    private String httpVersion; //HTTP/1.1, HTTP2, etc.

    //Headers
    private List<String> headers = new ArrayList<>(); //Initialise as an empty list to avoid null pointer exceptions
    private String host; //Host the client is requesting
    private String userAgent; //Sort of software made the request
    private String accept; //Sort of content the client can accept
    private String contentType; //Format of the request body
    private String contentLength; //Length of the request body
    private String connection; //Connection type (keep-alive, close, etc.)
    private String authorization; //Authorization header (if present) - TODO: Figure out if needed

    //Body
    private String body;

    public HttpRequest(BufferedReader reader) {
        this.reader = reader;
        //TODO: Read the request from the BufferedReader and initialize the fields
        parseRequest();
    }

    private void parseRequest() {
        try {
            //Request line
            this.requestLine = reader.readLine();
            System.out.println("Request line: " + requestLine);
            String[] requestLineArray = this.requestLine.split(" "); //Splits the request to form an array

            this.method = requestLineArray[0];
            this.target = requestLineArray[1];
            this.httpVersion = requestLineArray[2];

            System.out.println("Method: " + method);
            System.out.println("Target: " + target);
            System.out.println("HTTP Version: " + httpVersion);

            //Headers
            String line;
            //Ends until an empty line is reached - after this there will be body or end of request
            while (!(line = reader.readLine()).isEmpty()) {
                System.out.println("Header line: " + line);
                headers.add(line); //Adds the header line to the headers list
                handleHeaderLine(line);

                System.out.println("Header: " + line);
            }

        } catch (Exception e) {
            System.out.println("Error parsing request line: " + e.getMessage());
        }
    }

    //Take a header line and finds its corresponding header and sets the value of that header in the HttpRequest object
    private void handleHeaderLine(String line) {
        switch (line.split(":")[0].trim()) { //Keeping on the first part of the header line before the colon and trimming it to remove whitespace
            case "Host":
                this.host = line.split(":")[1].trim();
                break;
            case "User-Agent":
                this.userAgent = line.split(":")[1].trim();
                break;
            case "Accept":
                this.accept = line.split(":")[1].trim();
                break;
            case "Content-Type":
                this.contentType = line.split(":")[1].trim();
                break;
            case "Content-Length":
                this.contentLength = line.split(":")[1].trim();
                break;
            case "Connection":
                this.connection = line.split(":")[1].trim();
                break;
            case "Authorization":
                this.authorization = line.split(":")[1].trim();
                break;
            default:
                System.out.println("Unknown header: " + line); //TODO: Log unknown headers
                break;

        }
    }

    //Getters and Setters
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRequestLine() {
        return requestLine;
    }

    public void setRequestLine(String requestLine) {
        this.requestLine = requestLine;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentLength() {
        return contentLength;
    }

    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}