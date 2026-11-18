package models.network.response;

import models.network.HttpStatus;
import util.Util;

import java.nio.charset.StandardCharsets;

public class Response {
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

    private Response(Builder builder) {
        this.response = builder.response;
        this.responseBytes = builder.responseBytes;
        this.statusLine = builder.statusLine;
        this.status = builder.status;
        this.contentLength = builder.contentLength;
        this.connection = builder.connection;
        this.body = builder.body;
    }

    public static class Builder {
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

        //Body
        private byte[] body;

        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder connection(String connection) {
            this.connection = connection;
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public Response build() {
            //Validate if all the required fields are there
            if (this.status == null) {
                throw new IllegalStateException("Status is required");
            }
            if (this.contentType == null || this.contentType.isBlank()) {
                this.contentType = "text/plain";
            }
            if (this.connection == null || this.connection.isBlank()) {
                this.connection = "keep-alive";
            }
            if (this.body == null) {
                this.body = "".getBytes(StandardCharsets.UTF_8);
            }

            createStatusLine();
            createResponse();

            return new Response(this);
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
