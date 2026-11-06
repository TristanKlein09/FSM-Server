import server.HttpServer;

import java.io.*;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

//TODO: Explore the idea of using thread pools for tasks
//TODO: Make the server multi-threaded

public class Main {
    public static void main(String[] args) throws IOException, SQLException, ExecutionException, InterruptedException {
        HttpServer server = HttpServer.getInstance();
        server.startServer();
    }
}


