package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CourseSearchController {
    @FXML
    public TextField courseSubject;
    @FXML
    public TextField courseNumber;
    @FXML
    public TextField courseTitle;
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void search(ActionEvent actionEvent) {
    }

    @FXML
    public void addCourse(ActionEvent actionEvent) {
    }

    @FXML
    public void logout(ActionEvent actionEvent) {
    }

    @FXML
    public void gotoReviews(ActionEvent actionEvent) {
    }
}
