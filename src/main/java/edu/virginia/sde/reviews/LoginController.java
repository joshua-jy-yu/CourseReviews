package edu.virginia.sde.reviews;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class LoginController{
    @FXML
    private Label errorLabel;

    public void handleLogin(){
        errorLabel.setVisible(true);
        errorLabel.setText("Incorrect Username or Password, Try Again");
    }
}
