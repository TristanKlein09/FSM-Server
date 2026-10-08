package login;

public enum Permissions {
    //The values are used to help store the permission in the database as a string/text
    ADMIN("Admin"),
    MANAGER("Manager"),
    TECHNICIAN("Technician");

    private String description;

    Permissions(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }
}
