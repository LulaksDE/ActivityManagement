package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.User.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SceneManager {
    private static SceneManager instance;
    private final Map<String, FXMLLoader> loaderCache = new HashMap<>();

    // FXML-Dateinamen als Konstanten definieren
    public static final String LOGIN_VIEW = "LoginScreen.fxml";
    public static final String PROJECT_VIEW = "ProjectView.fxml";
    public static final String PROJECT_CREATION = "ProjectCreationScreen.fxml";
    public static final String ACTIVITY_LIST = "ActivityList.fxml";
    public static final String ACTIVITY_EDITOR = "ActivityEditor.fxml";
    public static final String HISTORY_VIEW = "HistoryView.fxml";

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
            Parent root = loader.load();
            loaderCache.put(fxmlPath, loader);
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
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

    public <T> T openModalWindow(Window owner, String fxmlPath, String title,
                                 Consumer<T> initializer) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        loaderCache.put(fxmlPath, loader);

        T controller = loader.getController();
        initializer.accept(controller);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        stage.showAndWait();
        return controller;
    }


    public <T> T openApplicationModalWindow(String fxmlPath, String title) throws IOException {
        T controller = loadFXML(fxmlPath);
        Stage stage = new Stage();
        stage.setScene(new Scene(getRoot(fxmlPath)));
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return controller;
    }

    public void closeWindow(Scene scene) {
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }
    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }
}
