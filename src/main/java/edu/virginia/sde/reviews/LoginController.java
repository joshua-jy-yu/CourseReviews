package edu.virginia.sde.reviews;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class LoginController{
    @FXML
    private Label errorLabel;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    private SessionFactory sessionFactory;
    private Session session;

    private void openDatabase(){
        try{
            var StandardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            var metaData = new MetadataSources(StandardRegistry).getMetadataBuilder().build();
            sessionFactory = metaData.getSessionFactoryBuilder().build();
            session = sessionFactory.openSession();
            session.beginTransaction();
        } catch (HibernateException e){
            // add some error messaging
        }
    }

    private boolean validateUsername(){
        return false;
    }

    private boolean validatePassword(){
        return false;
    }

    @FXML
    public void handleLogin(){
        openDatabase();
        if(validateUsername()){
            if(validatePassword()){
                // push user to course review page
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText("Password is incorrect, Try Again");
            }
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("Username is incorrect, Try again");
        }
        session.close();
    }

    @FXML
    public void handleCreateAccount() {
        openDatabase();

        session.close();
    }
}
