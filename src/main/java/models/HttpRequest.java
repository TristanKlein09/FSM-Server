package models;

import java.io.BufferedReader;
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
    private List<Header> headers;
    private Header host; //Host the client is requesting
    private Header userAgent; //Sort of software made the request
    private Header accept; //Sort of content the client can accept
    private Header contentType; //Format of the request body
    private Header contentLength; //Length of the request body
    private Header connection; //Connection type (keep-alive, close, etc.)
    private Header authorization; //Authorization header (if present) - TODO: Figure out if needed

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
            setRequestLine(reader.readLine());
            System.out.println("Request line: " + requestLine);
            String[] requestLineArray = this.requestLine.split( " "); //Splits the request to form an array
            this.method = requestLineArray[0];
            this.target = requestLineArray[1];
            this.httpVersion = requestLineArray[2];
            System.out.println("Method: " + method);
            System.out.println("Target: " + target);
            System.out.println("HTTP Version: " + httpVersion);

            //Headers
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                System.out.println("Header: " + line);
            }

        } catch (Exception e) {
            System.out.println("Error parsing request line: " + e.getMessage());
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

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public Header getHost() {
        return host;
    }

    public void setHost(Header host) {
        this.host = host;
    }

    public Header getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(Header userAgent) {
        this.userAgent = userAgent;
    }

    public Header getAccept() {
        return accept;
    }

    public void setAccept(Header accept) {
        this.accept = accept;
    }

    public Header getContentType() {
        return contentType;
    }

    public void setContentType(Header contentType) {
        this.contentType = contentType;
    }

    public Header getContentLength() {
        return contentLength;
    }

    public void setContentLength(Header contentLength) {
        this.contentLength = contentLength;
    }

    public Header getConnection() {
        return connection;
    }

    public void setConnection(Header connection) {
        this.connection = connection;
    }

    public Header getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Header authorization) {
        this.authorization = authorization;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}