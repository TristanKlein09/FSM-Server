package models;

public class HttpResponse {
    public String response;

    //Status Line
    public int statusLine;
    public int statusCode;

    //Headers
    public int contentLength;
    public int contentType;
    public String connection;
    //Server header not needed - only one server

    //Body
    public byte[] body;

    public HttpResponse(String response) {

    }
}
