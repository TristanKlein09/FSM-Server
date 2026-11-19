import database.implementations.SQLDatabaseInitialiser;
import database.interfaces.DatabaseInitialiser;
import server.HttpServer;
import storage.implementations.LocalStorageService;
import storage.interfaces.StorageService;

import java.io.*;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

//TODO: Explore the idea of using thread pools for tasks
//TODO: Make the server multi-threaded

public class Main {
    public static void main(String[] args) throws IOException, SQLException, ExecutionException, InterruptedException {
        //Initialise databases
        DatabaseInitialiser initialiser = new SQLDatabaseInitialiser();
        initialiser.initDatabase();
        //Initialise data directory
        StorageService storageService = new LocalStorageService();
        storageService.init();

        //Start server
        HttpServer server = HttpServer.getInstance();
        server.startServer();
    }
}


