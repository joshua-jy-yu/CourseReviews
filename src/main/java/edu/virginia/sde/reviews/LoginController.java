package edu.virginia.sde.reviews;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class LoginController{
    @FXML
    private Label errorLabel;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    private static Session session;
    private int dataId;

    private void openDatabase(){
        try{
            var StandardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            var metaData = new MetadataSources(StandardRegistry).getMetadataBuilder().build();
            SessionFactory sessionFactory = metaData.getSessionFactoryBuilder().build();
            session = sessionFactory.openSession();
            session.beginTransaction();
        } catch (HibernateException e){
            errorLabel.setText("Database cannot open");
            errorLabel.setVisible(true);
        }
    }

    private boolean validateUsername(){
        try{
            String query = "SELECT u FROM User u WHERE u.username = :username";
            TypedQuery<User> userQuery = session.createQuery(query, User.class);
            userQuery.setParameter("username", usernameInput);
            if(userQuery.getSingleResult().getUsername().equals(usernameInput.getText())) {
                dataId = userQuery.getSingleResult().getId();
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e){
            return false;
        }
    }

    private boolean validatePassword(){
        try{
            String query = "SELECT u FROM User u WHERE u.id = :id";
            TypedQuery<User> passQuery = session.createQuery(query, User.class);
            passQuery.setParameter("id", dataId);
            return passQuery.getSingleResult().getPassword().equals(passwordInput.getText());
        } catch (HibernateException e){
            return false;
        }
    }

    @FXML
    public void handleLogin(){
        errorLabel.setVisible(false);
        openDatabase();
        if(validateUsername()){
            if(validatePassword()){
                // push user to course review page
                errorLabel.setText("Successful login");
                errorLabel.setVisible(true);
                session.close();
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
        openDatabase();

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
    }
}
