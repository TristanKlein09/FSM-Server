package login;

import database.DatabaseManager;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AuthService {
    //TODO: Remember that bcrypt truncates the input at 72 bytes, so implement a character limit on the password when registering users. This can be done on the GUI

    //Checks if the sent username and password are correct
    //Password is sent over https, it then gets hashed and compared to the hash in the database

    //TODO: Remember that when implementing this function it must be run on its own thread due to SQL as well
    //Gets the hashedPassword that corresponds to the username and checks if it matches the password parameter
    //Returns true is password is correct, false if incorrect
    public boolean authenticate(String username, String password) throws SQLException, ExecutionException, InterruptedException {
        //Get the hashed password from the database that corresponds to the username
        Connection usersConnection = DatabaseManager.connectUsersDb();
        String getHashedPass = "SELECT password_hash FROM userLogins WHERE username = (?)";
        assert usersConnection != null; //usersConnection cannot be null
        PreparedStatement prepStmtGetHashedPass = usersConnection.prepareStatement(getHashedPass);
        prepStmtGetHashedPass.setString(1, username);
        ResultSet rsHashedPass = prepStmtGetHashedPass.executeQuery();
        String hashedPass = rsHashedPass.getString("password_hash");
        rsHashedPass.close();

        //BCrypt.checkpw is intensive enough to have to spin up a new thread
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> { //CompletableFuture sing
            Boolean valid = BCrypt.checkpw(password, hashedPass);
            return valid;
        });

        Boolean valid = future.get();
        System.out.println("Do passwords match: " + valid);

        return valid;
    }

    //Registering user, adds to the database to generate the ID, then returns User with a new id, or null if unsuccessful
    public User registerUser(User user) throws SQLException {
        //Check if user already has id, in which case it is already in the database
        if (user.getId() != 0) return null; //0 is the default value for int, so if it is not 0, the user already exists
        try {
            Connection usersConnection = DatabaseManager.connectUsersDb();

            //Inserting into userLogins table
            //Question marks are placeholders for prepared statement to prevent SQL injection
            String insertIntoUserLogins = "INSERT INTO userLogins (username, password_hash) VALUES (?, ?)";
            assert usersConnection != null; //usersConnection cannot be null
            PreparedStatement prepStmtUserLogins = usersConnection.prepareStatement(insertIntoUserLogins);
            prepStmtUserLogins.setString(1, user.getUsername()); //Adding the variables into the question mark
            prepStmtUserLogins.setString(2, user.getPasswordHash());
            prepStmtUserLogins.execute();
            prepStmtUserLogins.close();

            //Getting the automatically generated id from the userLogins table
            String getIdFromUserLogins = "SELECT user_id FROM userLogins WHERE username = (?)";
            PreparedStatement prepStmtGetId = usersConnection.prepareStatement(getIdFromUserLogins);
            prepStmtGetId.setString(1, user.getUsername());
            ResultSet rsId = prepStmtGetId.executeQuery();

            //Getting the id from the result set and setting it to the user object
            int id = rsId.getInt("user_id"); //Do not have to iterate through result set because the query will only return one item
            user.setId(id);
            prepStmtGetId.close();

            //Inserting into the users table
            String insertIntoUsers = "INSERT INTO users (id, first_name, last_name, permission) VALUES (?, ?, ?, ?)";
            PreparedStatement prepStmtUsers = usersConnection.prepareStatement(insertIntoUsers);
            prepStmtUsers.setInt(1, user.getId());
            prepStmtUsers.setString(2, user.getFirstName());
            prepStmtUsers.setString(3, user.getLastName());
            prepStmtUsers.setString(4, user.getPermission().getDescription()); //Description is used to store the permission in the database as a string/text
            prepStmtUsers.execute();
            prepStmtUsers.close();

            usersConnection.close();

            System.out.println("Registered user with ID: " + user.getId());
        } catch (Exception e) {
            System.err.println("Error registering user: " + e.getMessage());
        }

        return user; //Return the user with the new id
    }

}
