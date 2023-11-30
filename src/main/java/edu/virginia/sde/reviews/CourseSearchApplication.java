package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CourseSearchApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CourseSearch.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Course Review Application - CS 3140");
        stage.setScene(scene);
        stage.show();
    }
}
