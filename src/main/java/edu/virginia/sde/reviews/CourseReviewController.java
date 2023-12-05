package edu.virginia.sde.reviews;

import javafx.fxml.*;
import javafx.scene.control.*;
import java.sql.Timestamp;
import java.util.*;

public class CourseReviewController {
    @FXML
    private Label courseInfoLabel;

    @FXML
    private Label averageRatingLabel;

    @FXML
    private ListView<Review> reviewListView;

    @FXML
    private ComboBox<Integer> ratingComboBox;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private Button submitButton;

    private Course selectedCourse;

    public void initialize() {
        loadCourseInformation();
        loadReviews();
    }

    private void loadCourseInformation() {
    }

    private void loadReviews() {
    }

    @FXML
    private void submitReview() {
    }

    @FXML
    private void deleteReview() {
    }

    @FXML
    private void goBack() {
    }
}
