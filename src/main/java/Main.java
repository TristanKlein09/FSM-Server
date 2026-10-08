import login.AuthService;
import login.Permissions;
import login.User;
import server.HttpServer;
import util.Util;

import java.io.*;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

//TODO: Explore the idea of using thread pools for tasks

public class Main {
    public static void main(String[] args) throws IOException, SQLException, ExecutionException, InterruptedException {
        HttpServer server = new HttpServer(6173);
        server.startServer();

        /*
        AuthService authService = new AuthService();
        User newUser = new User("testuser", "hashedpassword", "Bruce", "Wayne", Permissions.ADMIN);
        newUser = authService.registerUser(newUser);
         */

        String password = "password";
        String hashedPassword = Util.hashPlainText(password);
        AuthService authService = new AuthService();
        User user = new User("BatMan", hashedPassword, "Bruce", "Wayne", Permissions.ADMIN);
        user = authService.registerUser(user); //User added to db
        authService.authenticate("BatMan", "password");

    }
}


