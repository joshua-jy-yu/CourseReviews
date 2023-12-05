package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;

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
    private static Session session;
    private LoggedUser loggedUser;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void updateTable(){
        // update list on button press
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();
        session.close();
    }

    @FXML
    private void search(ActionEvent actionEvent) {
        errorLabel.setVisible(false);
    }

    @FXML
    private void addCourse(ActionEvent actionEvent) {
        errorLabel.setVisible(false);
        if (validateSubject(courseSubject.getText().strip()) && validateNumber(courseNumber.getText().strip()) && validateTitle(courseTitle.getText().strip())){
            Course course = new Course(courseSubject.getText().strip(), Integer.parseInt(courseNumber.getText().strip()), courseTitle.getText().strip());
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.persist(course);
            session.getTransaction().commit();
            session.close();
            updateTable();
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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("Login.fxml"));
            Scene courseScene = new Scene(fxmlLoader.load());
            var controller = (LoginController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            LoggedUser user = LoggedUser.getInstance();
            user.setUsername("");
            primaryStage.setTitle("Course Review - Log-in");
            primaryStage.setScene(courseScene);
            primaryStage.show();
        } catch (Exception e){
            errorLabel.setText("Try again, IO error");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void gotoReviews(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("MyReview.fxml"));
            Scene courseScene = new Scene(fxmlLoader.load());
            var controller = (MyReviewController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            primaryStage.setTitle("Course Review - My Reviews");
            primaryStage.setScene(courseScene);
            primaryStage.show();
        } catch (Exception e){
            errorLabel.setText("Try again, IO error");
            errorLabel.setVisible(true);
        }
    }
}
