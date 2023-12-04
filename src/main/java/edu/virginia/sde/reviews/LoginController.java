package edu.virginia.sde.reviews;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;

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
        try{
            String query = "SELECT u FROM User u WHERE u.username = :username";
            TypedQuery<User> userQuery = session.createQuery(query, User.class);
            userQuery.setParameter("username", usernameInput.getText());
            if(userQuery.getSingleResult().getUsername().equals(usernameInput.getText())) {
                dataId = userQuery.getSingleResult().getId();
                session.close();
                return true;
            } else {
                session.close();
                return false;
            }
        } catch (HibernateException | NoResultException e){
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
            boolean validPassword = passQuery.getSingleResult().getPassword().equals(passwordInput.getText());
            session.close();
            return validPassword;
        } catch (HibernateException e){
            session.close();
            return false;
        }
    }

    @FXML
    public void handleLogin(){
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
            errorLabel.setText("Username is incorrect, Try again");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void handleCreateAccount() {
        errorLabel.setVisible(false);

        if(!validateUsername() && passwordInput.getText().length() >= 8){
            try {
                User user = new User(usernameInput.getText(), passwordInput.getText());
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                session.persist(user);
                session.getTransaction().commit();
                errorLabel.setText("Account creation successful");
                errorLabel.setVisible(true);
                session.close();
            } catch (PersistenceException e) {
                errorLabel.setText("Cannot update");
                errorLabel.setVisible(true);
                session.close();
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


    }

    @FXML
    public void handleClose(){
        primaryStage.close();
    }
}
