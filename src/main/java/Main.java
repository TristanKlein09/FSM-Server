import models.HttpResponse;
import models.HttpStatus;
import server.HttpServer;
import server.RequestHandler;
import util.Util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(9191);
        server.startServer();
    }
}


