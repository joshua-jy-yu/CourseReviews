package edu.virginia.sde.reviews;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
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
    @FXML
    public ListView list;

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
        boolean subject = false, number = false, title = false;
        errorLabel.setVisible(false);
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = builder.createQuery(Course.class);
        Root<Course> root = criteriaQuery.from(Course.class);
        List<Predicate> predicates = new ArrayList<>();



        if (!courseSubject.getText().isBlank()){
            subject = true;
        }
        if (!courseNumber.getText().isBlank()){
            number = true;
        }
        if (!courseTitle.getText().isBlank()){
            title = true;
        }
        if (!subject && !number && !title){
            session.close();
            return;
        }
        if (subject){
            predicates.add(builder.like(root.get("subject"), "%" + courseSubject.getText().strip() + "%"));
        }
        if (subject){
            predicates.add(builder.like(root.get("number"), "%" + Integer.parseInt(courseNumber.getText().strip()) + "%"));
        }
        if (subject){
            predicates.add(builder.like(root.get("title"), "%" + courseTitle.getText().strip() + "%"));
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(builder.or(predicates.toArray(new Predicate[0])));
        }

        TypedQuery<Course> query = session.createQuery(criteriaQuery);
        ObservableList<Course> results = FXCollections.observableList(query.getResultList());
        list.getItems().clear();
        list.getItems().addAll(results);
        session.close();
    }

    @FXML
    private void addCourse(ActionEvent actionEvent) {
        errorLabel.setVisible(false);
        if (validateSubject(courseSubject.getText().strip()) && validateNumber(courseNumber.getText().strip()) && validateTitle(courseTitle.getText().strip())){
            Course course = new Course(courseSubject.getText().strip().toUpperCase(), Integer.parseInt(courseNumber.getText().strip()), courseTitle.getText().strip());
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
