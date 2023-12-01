package edu.virginia.sde.reviews;

import jakarta.persistence.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hibernate.*;


public class LoginController{
    @FXML
    private Label errorLabel;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    private static Session session;
    private int dataId;

    private boolean validateUsername(){
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try{
            String query = "SELECT u FROM User u WHERE u.username = :username";
            TypedQuery<User> userQuery = session.createQuery(query, User.class);
            userQuery.setParameter("username", usernameInput);
            if(userQuery.getSingleResult().getUsername().equals(usernameInput.getText())) {
                dataId = userQuery.getSingleResult().getId();
                session.close();
                return true;
            } else {
                session.close();
                return false;
            }
        } catch (HibernateException e){
            session.close();
            return false;
        }
    }

    private boolean validatePassword(){
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try{
            String query = "SELECT u FROM User u WHERE u.id = :id";
            TypedQuery<User> passQuery = session.createQuery(query, User.class);
            passQuery.setParameter("id", dataId);
            session.close();
            return passQuery.getSingleResult().getPassword().equals(passwordInput.getText());
        } catch (HibernateException e){
            session.close();
            return false;
        }
    }

    @FXML
    public void handleLogin(){
        errorLabel.setVisible(false);

        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        if(validateUsername()){
            if(validatePassword()){
                errorLabel.setText("Successful login");
                errorLabel.setVisible(true);
                session.close();
                // push user to course review page
            } else {
                errorLabel.setText("Password is incorrect, Try Again");
                errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText("Username is incorrect, Try again");
            errorLabel.setVisible(true);
        }
        session.close();
    }

    @FXML
    public void handleCreateAccount() {
        errorLabel.setVisible(false);

        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        if(!validateUsername() && passwordInput.getText().length() >= 8){
            try {
                User user = new User(usernameInput.getText(), passwordInput.getText());
                session.persist(user);
                session.getTransaction().commit();
                errorLabel.setText("Account creation successful");
                errorLabel.setVisible(true);
            } catch (PersistenceException e) {
                errorLabel.setText("Cannot update");
                errorLabel.setVisible(true);
            }
        } else {
            if(validateUsername()){
                errorLabel.setText("Username already exists, try again");
                errorLabel.setVisible(true);
            }
            else {
                errorLabel.setText("Password needs to be at least 8 characters long");
                errorLabel.setVisible(true);
            }
        }

        session.close();
    }
}
