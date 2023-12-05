package edu.virginia.sde.reviews;

public class LoggedUser {
    private String username;
    private static LoggedUser user;

    public static LoggedUser getInstance() {
        if (user == null) {
            user = new LoggedUser();
        }
        return user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
