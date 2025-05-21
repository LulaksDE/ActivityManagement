package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.infrastructure.di.DependencyInjector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private static SceneManager instance;
    private final Map<String, FXMLLoader> loaderCache = new HashMap<>();
    private final Map<String, Parent> rootCache = new HashMap<>();

    private final LoggerNotifier logger = LoggerFactory.getLogger();

    public static final String LOGIN_VIEW = "LoginScreen.fxml";
    public static final String PROJECT_VIEW = "ProjectView.fxml";
    public static final String PROJECT_CREATION = "ProjectCreationScreen.fxml";
    public static final String ACTIVITY_LIST = "ActivityList.fxml";
    public static final String ACTIVITY_EDITOR = "ActivityEditor.fxml";
    public static final String ACTIVITY_CREATOR = "ActivityCreator.fxml";
    public static final String HISTORY_VIEW = "HistoryView.fxml";
    public static final String USER_MANAGEMENT = "UserManagement.fxml";
    private static final String FXML_PATH = "/com/lulakssoft/activitymanagement/";


    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public <T> T loadFXML(String fxmlFile) throws IOException {
        if (!loaderCache.containsKey(fxmlFile)) {
            String fxmlPath = FXML_PATH + fxmlFile;
            URL url = getClass().getResource(fxmlPath);
            if (url == null) {
                throw new IOException("FXML file not found: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(url);

            // Controller-Factory setzt DI ein
            loader.setControllerFactory(controllerClass ->
                    DependencyInjector.getInstance().createInstance(controllerClass));

            Parent root = loader.load();
            rootCache.put(fxmlFile, root);
            loaderCache.put(fxmlFile, loader);
        }

        return loaderCache.get(fxmlFile).getController();
    }

    public Parent getRoot(String fxmlFile) {
        return rootCache.get(fxmlFile);
    }

    public FXMLLoader getLoader(String fxmlFile) {
        return loaderCache.get(fxmlFile);
    }

    public void setMainScene(Stage stage, String fxmlFile, String title) throws IOException {
        loadFXML(fxmlFile);
        Parent root = getRoot(fxmlFile);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
    }

    public <T> T openModalWindow(Window owner, String fxmlFile, String title) throws IOException {
        loadFXML(fxmlFile);
        Parent root = getRoot(fxmlFile);

        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.showAndWait();

        return loaderCache.get(fxmlFile).getController();
    }
}
