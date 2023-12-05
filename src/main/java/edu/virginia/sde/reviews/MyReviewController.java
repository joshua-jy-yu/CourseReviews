package edu.virginia.sde.reviews;

import javafx.stage.Stage;
import org.hibernate.Session;

public class MyReviewController {
    private Stage primaryStage;
    private static Session session;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
