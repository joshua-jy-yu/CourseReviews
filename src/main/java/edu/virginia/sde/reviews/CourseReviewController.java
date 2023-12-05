package edu.virginia.sde.reviews;

import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

public class CourseReviewController {

    @FXML
    private Label courseSubject;
    @FXML
    private Label courseNumber;
    @FXML
    private Label courseName;

    @FXML
    private Label averageRatingLabel;

    @FXML
    private TableView<Review> reviewListView;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private Button submitButton;

    private Course selectedCourse;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setCourse(Course course){
        selectedCourse = course;
    }

    public void initialize() {
        loadCourseInformation();
        loadReviews();
    }

    private void loadCourseInformation() {
        loadCourseName();
    }

    private void loadCourseName(){

    }

    private void setReviews() {

    }

    @FXML
    private void submitReview() {

    }

    @FXML
    private void deleteReview() {

    }

    @FXML
    private void goBack() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("CourseSearch.fxml"));
            Scene courseScene = new Scene(fxmlLoader.load());
            var controller = (CourseSearchController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            primaryStage.setTitle("Course Review - Main Page");
            primaryStage.setScene(courseScene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
