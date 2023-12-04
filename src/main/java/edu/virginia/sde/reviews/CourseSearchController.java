package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class CourseSearchController {
    @FXML
    public TextField courseSubject;
    @FXML
    public TextField courseNumber;
    @FXML
    public TextField courseTitle;
    @FXML
    public Label errorLabel;
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void updateTable(){

    }

    @FXML
    private void search(ActionEvent actionEvent) {
        errorLabel.setVisible(false);
    }

    @FXML
    private void addCourse(ActionEvent actionEvent) {
        errorLabel.setVisible(false);
        if (validateSubject(courseSubject.getText()) && validateNumber(courseNumber.getText()) && validateTitle(courseTitle.getText())){

        }
    }

    private boolean validateSubject(String subject){
        if (subject.length() <= 4 && subject.length() >= 2){
            for (int i = 0; i < subject.length(); i++) {
                if(!Character.isLetter(subject.charAt(i))) {
                    errorLabel.setText("The subject is not all letters");
                    errorLabel.setVisible(true);
                    return false;
                }
            }
            return true;
        } else {
            errorLabel.setText("The subject is incorrect, has to be 2-4 letters");
            errorLabel.setVisible(true);
            return false;
        }
    }

    private boolean validateNumber(String number){
        if (number.length() == 4){
            for (int i = 0; i < 4; i++) {
                if(!Character.isDigit(number.charAt(i))) {
                    errorLabel.setText("The number is not all digits");
                    errorLabel.setVisible(true);
                    return false;
                }
            }
            return true;
        } else {
            errorLabel.setText("The number is too long, has to be 4 digits");
            errorLabel.setVisible(true);
            return false;
        }
    }

    private boolean validateTitle(String title){
        if (title.length() >= 1 && title.length() <= 50){
            return true;
        } else {
            errorLabel.setText("The title is incorrect, title has to be from 1 to 50 characters");
            errorLabel.setVisible(true);
            return false;
        }
    }

    @FXML
    private void logout(ActionEvent actionEvent) {
    }

    @FXML
    private void gotoReviews(ActionEvent actionEvent) {
    }
}
