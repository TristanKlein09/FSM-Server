package database.interfaces;

public interface DatabaseInitialiser {
    public void initDatabase();

    //Initialising the tables related to the users and their logins.
    public void initUsers();
    public void initUsersTable();
    public void initUserLoginsTable();
}
