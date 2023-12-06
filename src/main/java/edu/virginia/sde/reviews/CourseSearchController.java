package edu.virginia.sde.reviews;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import java.io.IOException;
import java.util.*;


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
    public ListView<Course> list;

    private LoggedUser loggedUser;
    private Stage primaryStage;
    private static Session session;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
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

    @FXML
    private void search() {
        errorLabel.setVisible(false);
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = builder.createQuery(Course.class);
        Root<Course> root = criteriaQuery.from(Course.class);
        List<Predicate> predicates = new ArrayList<>();

        boolean subject = !courseSubject.getText().isEmpty();
        boolean number = !courseNumber.getText().isEmpty();
        boolean title = !courseTitle.getText().isEmpty();

        if (!subject && !number && !title){
            session.close();
            updateTable();
            return;
        }

        if (subject){
            subject = validateSubject(courseSubject.getText().strip());
        }
        if (number){
            number = validateNumber(courseNumber.getText().strip());
        }

        if (subject){
            predicates.add(builder.equal(builder.lower(root.get("subject")), courseSubject.getText().strip().toLowerCase()));
        }
        if (number){
            predicates.add(builder.equal(root.get("number"), Integer.parseInt(courseNumber.getText().strip())));
        }
        if (title){
            predicates.add(builder.like(root.get("title"), "%" + courseTitle.getText().strip() + "%"));
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[0])));
        }

        TypedQuery<Course> query = session.createQuery(criteriaQuery);
        ObservableList<Course> results = FXCollections.observableList(query.getResultList());
        list.getItems().clear();
        list.setItems(results);
        session.close();
    }

    @FXML
    private void addCourse() {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        errorLabel.setVisible(false);
        if (validateSubject(courseSubject.getText().strip()) && validateNumber(courseNumber.getText().strip()) && validateTitle(courseTitle.getText().strip())){
            try {
                Course course = new Course(courseSubject.getText().strip().toUpperCase(), Integer.parseInt(courseNumber.getText().strip()), courseTitle.getText().strip());

                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<Course> criteriaQuery = builder.createQuery(Course.class);
                Root<Course> root = criteriaQuery.from(Course.class);
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(builder.like(builder.lower(root.get("subject")), courseSubject.getText().strip().toLowerCase()));
                predicates.add(builder.equal(root.get("number"), Integer.parseInt(courseNumber.getText().strip())));
                predicates.add(builder.like(root.get("title"),  courseTitle.getText().strip().toLowerCase()));
                criteriaQuery.where(builder.and(predicates.toArray(new Predicate[0])));
                TypedQuery<Course> query = session.createQuery(criteriaQuery);
                boolean check = query.getResultList().isEmpty();
                session.close();
                if (check){
                    session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction();
                    session.persist(course);
                    session.getTransaction().commit();
                    session.close();
                } else {
                    errorLabel.setText("Course Already Exists");
                    errorLabel.setVisible(true);
                }
            } catch (HibernateException e) {
                errorLabel.setText("IO Error, Retry");
                errorLabel.setVisible(true);
                updateTable();
                session.close();
            }
        }
        updateTable();
        session.close();
    }

    @FXML public void handleMouseClick(MouseEvent arg0) {
        Course course = list.getSelectionModel().getSelectedItem();
        if (course == null){
            return; // give error
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseReviewController.class.getResource("CourseReview.fxml"));
            Scene courseScene = new Scene(fxmlLoader.load());
            var controller = (CourseReviewController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            primaryStage.setTitle("Course Review - Update Page");
            primaryStage.setScene(courseScene);
            primaryStage.show();
        } catch (Exception e){
            errorLabel.setText("Try again, IO error");
            errorLabel.setVisible(true);
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
            errorLabel.setText("The number is incorrect, has to be 4 digits exactly");
            errorLabel.setVisible(true);
            return false;
        }
    }

    private boolean validateTitle(String title){
        if (!title.isEmpty() && title.length() <= 50){
            return true;
        } else {
            errorLabel.setText("The title is incorrect, title has to be from 1 to 50 characters");
            errorLabel.setVisible(true);
            return false;
        }
    }

    @FXML
    private void logout() {
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
    private void gotoReviews() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("MyReview.fxml"));
            Scene courseScene = new Scene(fxmlLoader.load());
            var controller = (MyReviewController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.initializeUser(loggedUser);
            primaryStage.setTitle("Course Review - My Reviews");
            primaryStage.setScene(courseScene);
            primaryStage.show();
        } catch (Exception e){
            errorLabel.setText("Try again, IO error");
            errorLabel.setVisible(true);
            throw e;
        }
    }

    @FXML
    private void goToCourseReview() throws IOException {
        Course course = list.getSelectionModel().getSelectedItem();
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
            errorLabel.setText("Try again, IO error");
            errorLabel.setVisible(true);
            throw e;
        }
    }

    public void initializeUser(LoggedUser user) {
        this.loggedUser = user;
    }
}
