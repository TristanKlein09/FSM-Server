package models.server;

import util.Util;
import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private final String CRLF = "\r\n";
    private String response;
    private byte[] responseBytes;

    //Status Line
    private String statusLine;
    private String httpVersion = "HTTP/1.1";
    private HttpStatus status;

    //Headers
    private int contentLength;
    private String contentType;
    private String connection;
    //Server header not needed - only one server

    //Body
    private byte[] body;

    public HttpResponse(HttpStatus status, String contentType, String connection, byte[] body) {
        this.status = status;
        this.contentType = contentType;
        this.connection = connection;
        this.body = body;

        //Calculate contentLength
        this.contentLength = Util.contentLength(this.body);

        //Creating statusline and response
        createStatusLine();
        createResponse();
    }

    private void createResponse() {
        this.response = this.statusLine +
                "Content-Type: " + this.contentType + CRLF +
                "Content-Length: " + this.contentLength + CRLF +
                CRLF;
        //System.out.println(this.response);

        this.responseBytes = this.response.getBytes(StandardCharsets.UTF_8);
    }

    private void createStatusLine() {
        this.statusLine = httpVersion + " " + status.getCode() + " " + status.getReasonPhrase() + CRLF;
        //System.out.println(this.statusLine);
    }

    //Getters and setters
    public String getCRLF() {
        return CRLF;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public byte[] getResponseBytes() {
        return responseBytes;
    }

    public void setResponseBytes(byte[] responseBytes) {
        this.responseBytes = responseBytes;
    }

    public String getStatusLine() {
        return statusLine;
    }

    public void setStatusLine(String statusLine) {
        this.statusLine = statusLine;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
