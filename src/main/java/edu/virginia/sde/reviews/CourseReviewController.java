package edu.virginia.sde.reviews;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.Time;
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
    private Label selectedRating;
    @FXML
    private Label editReviewErrorLabel;

    @FXML
    private Label averageRatingLabel;

    @FXML
    private TableView<Review> reviewTableView;
    @FXML
    private TableColumn<Review, Integer> ratingColumn;
    @FXML
    private TableColumn<Review, String> commentColumn;
    @FXML
    private TableColumn<Review, Timestamp> timeColumn;

    @FXML
    private TextArea commentTextArea;
    @FXML
    private RadioButton rating1button, rating2button, rating3button, rating4button, rating5button;
    private ToggleGroup buttonGroup;
    @FXML
    private Button deleteReviewButton;
    @FXML
    private Label yourReviewRating;
    @FXML
    private Label yourReviewComment;
    @FXML
    private Label yourReviewTime;

    private Course selectedCourse;
    private LoggedUser loggedUser;
    private static Session session;
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setCourse(Course course){
        selectedCourse = course;
    }

    public void initialize() {
        editReviewErrorLabel.setVisible(false);
        buttonGroup = new ToggleGroup();
        rating1button.setToggleGroup(buttonGroup);
        rating2button.setToggleGroup(buttonGroup);
        rating3button.setToggleGroup(buttonGroup);
        rating4button.setToggleGroup(buttonGroup);
        rating5button.setToggleGroup(buttonGroup);
        deleteReviewButton.setVisible(false);
        reviewTableView.setPlaceholder(new Label("No reviews yet"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeColumn.setSortType(TableColumn.SortType.DESCENDING);
    }

    public void initializeCourse(Course course) {
        selectedCourse = course;
        loadCourseName();
        updateTable();
    }

    private void loadCourseName(){
        courseSubject.setText(selectedCourse.getSubject());
        courseNumber.setText(String.valueOf(selectedCourse.getNumber()));
        courseName.setText(selectedCourse.getTitle());
    }

    public void initializeUser(LoggedUser loggedUser){
        this.loggedUser = loggedUser;
        updateYourReview();
    }

    @FXML
    private void submitReview() {
        editReviewErrorLabel.setVisible(false);
        if (validateRating()){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            User user = getUser();
            if(!reviewExists()) {
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                Review review = new Review(getRating(), getComment(), user, selectedCourse, timestamp);
                session.persist(review);
                session.getTransaction().commit();
                session.close();
                updateTable();
                updateYourReview();
            } else {
                Review existingReview = getReview();
                existingReview.setTime(timestamp);
                existingReview.setComment(getComment());
                existingReview.setRating(getRating());
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                session.merge(existingReview);
                session.getTransaction().commit();
                session.close();
                updateTable();
                updateYourReview();
            }
            editReviewErrorLabel.setVisible(false);
        } else {
            editReviewErrorLabel.setText("A rating must be selected");
            editReviewErrorLabel.setVisible(true);
        }
    }

    private boolean reviewExists(){
        User user = getUser();
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            String hql = "SELECT r FROM Review r WHERE r.user = :user";
            TypedQuery<Review> reviewQuery = session.createQuery(hql, Review.class);
            reviewQuery.setParameter("user", user);
            Review review = reviewQuery.getSingleResult();
            deleteReviewButton.setVisible(true);
            return true;
        } catch(NoResultException e){
            return false;
        } finally {
            session.close();
        }
    }

    private User getUser() {
        String username = loggedUser.getUsername();
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "SELECT u FROM User u WHERE u.username = :username";
        TypedQuery<User> reviewQuery = session.createQuery(hql, User.class);
        reviewQuery.setParameter("username", username);
        User user = reviewQuery.getSingleResult();
        session.getTransaction().commit();
        session.close();
        return user;
    }

    private void updateTable(){
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "SELECT r FROM Review r WHERE r.course = :course";
        TypedQuery<Review> reviewQuery = session.createQuery(hql, Review.class);
        reviewQuery.setParameter("course", selectedCourse);
        ObservableList<Review> results = FXCollections.observableList(reviewQuery.getResultList());
        reviewTableView.setItems(results);
        reviewTableView.getSortOrder().add(timeColumn);
        session.close();
        updateAverageRating();
    }

    private void updateYourReview(){
        if(reviewExists()) {
            Review review = getReview();
            yourReviewRating.setText(String.valueOf(review.getRating()));
            yourReviewComment.setText(review.getComment());
            yourReviewTime.setText(String.valueOf(review.getTime()));
        } else {
            yourReviewRating.setText("");
            yourReviewComment.setText("No comments");
            yourReviewTime.setText("N/A");
        }

    }

    private Review getReview() {
        User user = getUser();
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "SELECT r FROM Review r WHERE r.user = :user";
        TypedQuery<Review> reviewQuery = session.createQuery(hql, Review.class);
        reviewQuery.setParameter("user", user);
        Review review = reviewQuery.getSingleResult();
        session.getTransaction().commit();
        session.close();
        return review;
    }

    private boolean validateRating() {
        var toggle = buttonGroup.getSelectedToggle();
        if(toggle==null) {
            editReviewErrorLabel.setText("A rating has not been selected");
            editReviewErrorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    private int getRating() {
        var toggle = (RadioButton) buttonGroup.getSelectedToggle();
        return Integer.parseInt(toggle.getText());
    }

    private String getComment() {
        if(commentTextArea.getText()==null) {
            return "";
        }
        return commentTextArea.getText();
    }

    @FXML
    private void deleteReview() {
        Review existingReview = getReview();
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.remove(existingReview);
        session.getTransaction().commit();
        session.close();
        updateTable();
        updateYourReview();
        deleteReviewButton.setVisible(false);
    }

    @FXML
    public void updateSelectedRating(ActionEvent e){
        RadioButton selectedButton = (RadioButton)e.getSource();
        selectedRating.setText(selectedButton.getText());
    }

    @FXML
    private void goBack() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("CourseSearch.fxml"));
            Scene courseScene = new Scene(fxmlLoader.load());
            var controller = (CourseSearchController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.initializeUser(loggedUser);
            primaryStage.setTitle("Course Review - Main Page");
            primaryStage.setScene(courseScene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAverageRating(){
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "from Review"; //class name, not Table name!
        Query<Review> reviewQuery = session.createQuery(hql, Review.class);
        List<Review> reviewList = reviewQuery.getResultList();
        if(reviewList.isEmpty()){
            averageRatingLabel.setText("N/A");
        } else {
            double total = 0.0;
            for(Review review: reviewList){
                total += review.getRating();
            }
            double average = total/reviewList.size();
            averageRatingLabel.setText(String.format("%.2f", average));
        }
        session.close();
    }
}
