package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private static SceneManager instance;
    private final Map<String, FXMLLoader> loaderCache = new HashMap<>();

    private final LoggerNotifier logger = LoggerFactory.getLogger();

    public static final String LOGIN_VIEW = "LoginScreen.fxml";
    public static final String PROJECT_VIEW = "ProjectView.fxml";
    public static final String PROJECT_CREATION = "ProjectCreationScreen.fxml";
    public static final String ACTIVITY_LIST = "ActivityList.fxml";
    public static final String ACTIVITY_EDITOR = "ActivityEditor.fxml";
    public static final String ACTIVITY_CREATOR = "ActivityCreator.fxml";
    public static final String HISTORY_VIEW = "HistoryView.fxml";
    public static final String USER_MANAGEMENT = "UserManagement.fxml";

    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public <T> T loadFXML(String fxmlPath) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.load();
            loaderCache.put(fxmlPath, loader);
            return loader.getController();
        } catch (IOException e) {
            logger.logError("Error loading FXML file: " + fxmlPath, e);
            throw new IOException("Error loading FXML file: " + fxmlPath, e);
        }

    }

    public FXMLLoader getLoader(String fxmlPath) {
        return loaderCache.get(fxmlPath);
    }

    public Parent getRoot(String fxmlPath) {
        return loaderCache.get(fxmlPath).getRoot();
    }

    public void setMainScene(Stage stage, String fxmlPath, String title) throws IOException {
        loadFXML(fxmlPath);
        Scene scene = new Scene(getRoot(fxmlPath));
        stage.setScene(scene);
        stage.setTitle(title);
    }

    public <T> T openModalWindow(Window owner, String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        loaderCache.put(fxmlPath, loader);

        T controller = loader.getController();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        stage.showAndWait();
        return controller;
    }
}
