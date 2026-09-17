package models;

import util.Util;
import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private String crlf = "\r\n";
    public String response;
    public byte[] responseBytes;

    //Status Line
    public String statusLine;
    public String httpVersion = "HTTP/1.1";
    public HttpStatus status;

    //Headers
    public int contentLength;
    public String contentType;
    public String connection;
    //Server header not needed - only one server

    //Body
    public byte[] body;

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
                "Content-Type: " + this.contentType + crlf +
                "Content-Length: " + this.contentLength + crlf +
                crlf;
        System.out.println(this.response);

        this.responseBytes = this.response.getBytes(StandardCharsets.UTF_8);
    }

    private void createStatusLine() {
        this.statusLine = httpVersion + " " + status.getCode() + " " + status.getReasonPhrase() + crlf;
        System.out.println(this.statusLine);
    }
}
