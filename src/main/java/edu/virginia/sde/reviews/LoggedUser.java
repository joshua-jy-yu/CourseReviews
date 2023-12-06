package edu.virginia.sde.reviews;

public class LoggedUser {
    private String username;
    private int id;



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
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
