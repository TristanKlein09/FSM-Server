package json;

import login.Permissions;

public class jsonRegisterUser {
    private String username;
    private String password; //Since we are retrieving the password from the request body, it is not hashed yet
    private String firstName;
    private String lastName;
    private Permissions permissions;

    public jsonRegisterUser(String username, String password, String firstName, String lastName, Permissions permissions) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.permissions = permissions;
    }

    //Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }
}