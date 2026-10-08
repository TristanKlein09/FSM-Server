package login;

import java.security.Permission;

public class User {
    private int id; //ID will be got from the database
    private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private Permissions permission;

    //Constructor with id
    public User() {

    }

    //Constructor without id, for registering a new user
    public User(String username, String passwordHash, String firstName, String lastName, Permissions permission) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.permission = permission;
    }

    //Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Permissions getPermission() {
        return permission;
    }

    public void setPermission(Permissions permission) {
        this.permission = permission;
    }
}
