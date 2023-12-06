package edu.virginia.sde.reviews;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;

public class MyReviewController {
    private Stage primaryStage;
    private static Session session;
    @FXML
    public ListView<Review> list;
    private LoggedUser loggedUser;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    @FXML
    private void back() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("CourseSearch.fxml"));
            Scene courseScene = new Scene(fxmlLoader.load());
            var controller = (CourseSearchController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.initializeUser(loggedUser);
            primaryStage.setTitle("Course Review - Main Page");
            primaryStage.setScene(courseScene);
            primaryStage.show();
        } catch (Exception e){
//            errorLabel.setText("Try again, IO error");
//            errorLabel.setVisible(true);
        }
    }

    public void initializeUser(LoggedUser loggedUser){
        this.loggedUser = loggedUser;
        updateTable();
    }

    private void updateTable(){
        User user = getUser();
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Review> criteria = builder.createQuery(Review.class);
        Root<Review> root = criteria.from(Review.class);

        Predicate userReviews = builder.equal(root.get("user"), user);
        criteria.select(root).where(builder.and(userReviews));
        TypedQuery<Review> query = session.createQuery(criteria);
        ObservableList<Review> results = FXCollections.observableList(query.getResultList());
        list.getItems().clear();
        list.setItems(results);
        session.close();
    }

    @FXML
    private void goToCourseReview(){
        Course course = list.getSelectionModel().getSelectedItem().getCourse();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("CourseReview.fxml"));
            Scene courseScene = new Scene(fxmlLoader.load());
            var controller = (CourseReviewController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.initializeUser(loggedUser);
            controller.initializeCourse(course);
            primaryStage.setTitle("Course Review - "+ course.getSubject()+" "+course.getNumber()+" - "+course.getTitle());
            primaryStage.setScene(courseScene);
            primaryStage.show();
        } catch (IOException e){

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
}
