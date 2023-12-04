package edu.virginia.sde.reviews;

import jakarta.persistence.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
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
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private boolean validateUsername(){
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            String query = "SELECT u FROM User u WHERE u.username = :username";
            TypedQuery<User> userQuery = session.createQuery(query, User.class);
            userQuery.setParameter("username", usernameInput.getText());
            String user = userQuery.getSingleResult().getUsername();
            String in = usernameInput.getText();

            if (user.equals(in)) {
                dataId = userQuery.getSingleResult().getId();
                return true;
            }
            return false;
        } catch (NoResultException e) {
            return false;
        } finally {
            session.close();
        }
    }


    private boolean validatePassword(){
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            String query = "SELECT u FROM User u WHERE u.id = :id";
            TypedQuery<User> passQuery = session.createQuery(query, User.class);
            passQuery.setParameter("id", dataId);
            return passQuery.getSingleResult().getPassword().equals(passwordInput.getText());
        } catch (HibernateException e) {
            return false;
        } finally {
            session.close();
        }
    }

    @FXML
    private void close(){
        if (session != null && session.isOpen()){
            session.close();
        }
        javafx.application.Platform.exit();
    }

    @FXML
    private void handleLogin(){
        errorLabel.setVisible(false);
        if(validateUsername()){
            if(validatePassword()){
                errorLabel.setText("Successful login");
                errorLabel.setVisible(true);
                // push user to course review page
            } else {
                errorLabel.setText("Password is incorrect, Try Again");
                errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText("Username is incorrect or not in database, Try again");
            errorLabel.setVisible(true);
        }
        usernameInput.clear();
        passwordInput.clear();
    }

    @FXML
    private void handleCreateAccount() {
        errorLabel.setVisible(false);
        if(!validateUsername() && passwordInput.getText().length() >= 8){
            User user = new User(usernameInput.getText(), passwordInput.getText());
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                session.persist(user);
                session.getTransaction().commit();
            } catch (PersistenceException e) {
                errorLabel.setText("Cannot update database");
                errorLabel.setVisible(true);
            } finally {
                session.close();
            }
            errorLabel.setText("Account creation successful");
            errorLabel.setVisible(true);
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
        usernameInput.clear();
        passwordInput.clear();
    }
}
