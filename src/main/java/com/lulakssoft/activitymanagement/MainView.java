package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.database.DatabaseConnection;
import com.lulakssoft.activitymanagement.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.notification.LoggerNotifier;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class MainView extends Application {

    private final LoggerNotifier logger = LoggerFactory.getLogger();


    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.logInfo("Starting application");
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setMainScene(primaryStage, SceneManager.LOGIN_VIEW, "Activity Management System");
        LoginViewController controller = sceneManager.getLoader(SceneManager.LOGIN_VIEW).getController();
        controller.initialize();
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            logger.logInfo("Application is closing");
            DatabaseConnection.closeDataSource();
        });
    }

    @Override
    public void stop() {
        logger.logInfo("Closing database connection to prevent memory leaks");
        DatabaseConnection.closeDataSource();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
