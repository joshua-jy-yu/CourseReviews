package edu.virginia.sde.reviews;

import jakarta.persistence.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
            userQuery.setParameter("username", usernameInput.getText().strip());
            String user = userQuery.getSingleResult().getUsername();
            String in = usernameInput.getText().strip();

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
            return passQuery.getSingleResult().getPassword().equals(passwordInput.getText().strip());
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

                loginSuccessful();
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
        if(!validateUsername() && passwordInput.getText().strip().length() >= 8){
            User user = new User(usernameInput.getText().strip(), passwordInput.getText().strip());
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

    private void loginSuccessful(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("CourseSearch.fxml"));
            Scene courseScene = new Scene(fxmlLoader.load());
            var controller = (CourseSearchController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            LoggedUser user = LoggedUser.getInstance();
            user.setUsername(usernameInput.getText().strip());
            controller.initializeUser(user);
            primaryStage.setTitle("Course Review - Main Page");
            primaryStage.setScene(courseScene);
            primaryStage.show();
        } catch (Exception e){
            errorLabel.setText("Try again, IO error");
            errorLabel.setVisible(true);
        }
    }
}
