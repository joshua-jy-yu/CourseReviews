package edu.virginia.sde.reviews;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.sql.Timestamp;

public class CourseReviewController {
    @FXML
    private Label courseMnemonicLabel;
    @FXML
    private Label courseNumberLabel;

    @FXML
    private Label courseTitleLabel;

    @FXML
    private Label averageRatingLabel;

    @FXML
    private TableView<Review> reviewTable;

    @FXML
    private RadioButton rating1;

    @FXML
    private RadioButton rating2;

    @FXML
    private RadioButton rating3;

    @FXML
    private RadioButton rating4;

    @FXML
    private RadioButton rating5;

    @FXML
    private TableColumn<Review, Integer> ratingColumn;

    @FXML
    private TableColumn<Review, String> commentColumn;

    @FXML
    private TableColumn<Review, Timestamp> timestampColumn;
    @FXML
    private Button submitButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextArea commentTextArea;
    @FXML
    private ComboBox<Integer> ratingComboBox;
    @FXML
    private Label editReviewErrorLabel;
    @FXML
    private Label deleteErrorLabel;
    @FXML
    public Label errorLabel;

    private Stage primaryStage;

    private static Session session;

    private Course selectedCourse;

    private User currentUser;

    public ListView<Course> list;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initialize();
        updateTable();
    }

    private void updateTable(){
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = builder.createQuery(Course.class);
        Root<Course> root = criteriaQuery.from(Course.class);
        criteriaQuery.select(root);
        TypedQuery<Course> query = session.createQuery(criteriaQuery);
        ObservableList<Course> results = FXCollections.observableList(query.getResultList());
        list.getItems().clear();
        list.setItems(results);
        session.close();
    }

    public void initialize() {
        loadCourseInformation();
        loadReviews();
    }

    private void loadCourseInformation() {
        courseMnemonicLabel.setText(selectedCourse.getSubject());
        courseNumberLabel.setText(String.valueOf(selectedCourse.getNumber()));
        courseTitleLabel.setText(selectedCourse.getTitle());
    }

    private double calculateAverageRating(Course course){
        return 0.0;
    }

    private void loadReviews() {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = builder.createQuery(Course.class);
        Root<Review> root = criteriaQuery.from(Review.class);


        session.close();

    }

    private void showReview(Review review){
        if (review != null) {
            ratingColumn.setText(Integer.toString(review.getRating()));
            commentColumn.setText(review.getComment());
        }
    }

    @FXML
    private void submitReview() {
        int rating = Integer.parseInt(ratingColumn.getText());
        String comment = commentColumn.getText();

        if (validateRating(rating)) {
            if (reviewExists()) {
                Review existingReview = getExistingReview();
                existingReview.setRating(rating);
                existingReview.setComment(comment);
                existingReview.setTime();
            } else {
                Review newReview = new Review(rating, comment, currentUser, selectedCourse);
                session.getTransaction().begin();
                session.persist(newReview);
                session.getTransaction().commit();
            }

            editReviewErrorLabel.setText("");
            updateTable();
        } else {
            editReviewErrorLabel.setText("Invalid rating. Please enter a rating between 1 and 5.");
        }
    }

    private boolean reviewExists() {
        TypedQuery<Long> query = session.createQuery(
                "SELECT COUNT(r) FROM Review r WHERE r.course = :course AND r.user = :user", Long.class);
        query.setParameter("course", selectedCourse);
        query.setParameter("user", currentUser);
        return query.getSingleResult() > 0;
    }

    private Review getExistingReview() {
        TypedQuery<Review> query = session.createQuery(
                "SELECT r FROM Review r WHERE r.course = :course AND r.user = :user", Review.class);
        query.setParameter("course", selectedCourse);
        query.setParameter("user", currentUser);
        return query.getSingleResult();
    }


    private boolean validateRating(int rating) {
        return rating >= 1 && rating <= 5;
    }

    @FXML
    private void deleteReview() {
        Review selectedReview = reviewTable.getSelectionModel().getSelectedItem();
        if (selectedReview != null) {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.remove(selectedReview);
            session.getTransaction().commit();
            deleteErrorLabel.setText("");
            updateTable();
        } else {
            deleteErrorLabel.setText("Please select a review to delete.");
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("CourseSearch.fxml"));
            Scene courseScene = new Scene(fxmlLoader.load());
            var controller = (CourseSearchController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            primaryStage.setTitle("Course Review - Main Page");
            primaryStage.setScene(courseScene);
            primaryStage.show();
        } catch (Exception e){
            errorLabel.setText("Try again, IO error");
            errorLabel.setVisible(true);
        }
    }
}
