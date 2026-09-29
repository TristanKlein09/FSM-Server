import server.HttpServer;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(6173);
        server.startServer();
    }
}


