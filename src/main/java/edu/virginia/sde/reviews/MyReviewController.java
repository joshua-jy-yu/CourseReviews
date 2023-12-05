package edu.virginia.sde.reviews;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.hibernate.Session;

public class MyReviewController {
    private Stage primaryStage;
    private static Session session;
    @FXML
    public ListView<Review> list;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        updateTable();
    }
    @FXML
    private void back() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("CourseSearch.fxml"));
            Scene courseScene = new Scene(fxmlLoader.load());
            var controller = (CourseSearchController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            primaryStage.setTitle("Course Review - Main Page");
            primaryStage.setScene(courseScene);
            primaryStage.show();
        } catch (Exception e){
//            errorLabel.setText("Try again, IO error");
//            errorLabel.setVisible(true);
        }
    }
    private void updateTable(){
        //Course course;
        //User user;
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Review> criteriaQuery = builder.createQuery(Review.class);
        Root<Review> root = criteriaQuery.from(Review.class);
        criteriaQuery.select(root);
        TypedQuery<Review> query = session.createQuery(criteriaQuery);
        ObservableList<Review> results = FXCollections.observableList(query.getResultList());
        list.getItems().clear();
        list.setItems(results);
        //user = session.get(User.class,0);
        //course = session.get(Course.class,0);
        //session.close();
        session.close();
    }
}
