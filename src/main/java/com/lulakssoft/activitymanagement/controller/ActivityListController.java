package com.lulakssoft.activitymanagement.controller;

import com.lulakssoft.activitymanagement.Activity;
import com.lulakssoft.activitymanagement.Project;
import com.lulakssoft.activitymanagement.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityListController {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Activity> activityListView;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button historieButton; // Button für Historie-Ansicht

    private ObservableList<Activity> activityList;

    private FilteredList<Activity> filteredActivities;


    private Project currentProject;

    // Liste für Historie-Logs
    private List<String> historyLogs = new ArrayList<>();

    @FXML
    public void initialize() {
        // Setup UI components and event handlers only
        addButton.setOnAction(e -> handleAddActivity());
        deleteButton.setOnAction(e -> handleDeleteActivity());
        historieButton.setOnAction(e -> openHistorieView());
        activityListView.setOnMouseClicked(this::handleDoubleClick);

        // Set custom cell factory
        setupListViewCellFactory();
    }

    public void initData(Project project) {
        this.currentProject = project;
        activityList = FXCollections.observableArrayList(project.getActivityList());

        // Setup filtered list
        filteredActivities = new FilteredList<>(activityList, p -> true);
        activityListView.setItems(filteredActivities);

        // Setup search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredActivities.setPredicate(activity -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                return activity.getTitle().toLowerCase().contains(newValue.toLowerCase());
            });
        });
    }

    private void setupListViewCellFactory() {
        activityListView.setCellFactory(listView -> new ListCell<Activity>() {
            private final Label iconLabel = new Label("🔍");

            @Override
            protected void updateItem(Activity activity, boolean empty) {
                super.updateItem(activity, empty);
                if (empty || activity == null) {
                    setGraphic(null);
                } else {
                    Label labelTitle = new Label(activity.getTitle());
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    HBox labelBox = new HBox(labelTitle, spacer);
                    labelBox.setAlignment(Pos.CENTER_LEFT);
                    labelBox.setOnMouseEntered(event -> {
                        if (!labelBox.getChildren().contains(iconLabel)) {
                            labelBox.getChildren().add(iconLabel);
                        }
                    });
                    labelBox.setOnMouseExited(event -> labelBox.getChildren().remove(iconLabel));
                    setGraphic(labelBox);
                }
            }
        });
    }

    private void handleAddActivity() {
        try {
            SceneManager sceneManager = SceneManager.getInstance();
            ActivityEditorController controller = sceneManager.showDialog("ActivityEditor.fxml", "Add New Activities");

            // Initialize the controller with data
            controller.initialize(currentProject.getActivityList());

            // Get new activities after dialog closes
            List<Activity> newActivities = controller.getNewActivities();
            if (newActivities != null) {
                for (Activity activity : newActivities) {
                    currentProject.addActivity(activity);
                    activityList.add(activity);
                    historyLogs.add("Added Activity: " + activity.getTitle());
                }
                activityListView.refresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteActivity() {
        Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();
        if (selectedActivity != null) {
            currentProject.removeActivity(selectedActivity);
            activityList.remove(selectedActivity);
            historyLogs.add("Deleted Activity: " + selectedActivity.getTitle()); // Hinzufügen zur Historie
            activityListView.refresh();
        }
    }

    private void handleDoubleClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();
            if (selectedActivity != null) {
                openActivityEditor(selectedActivity);
            }
        }
    }

    private void openEditor() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActivityEditor.fxml"));
            Parent root = loader.load();

            Stage editorStage = new Stage();
            editorStage.setTitle("Add New Activities");
            editorStage.initModality(Modality.WINDOW_MODAL);
            editorStage.initOwner(addButton.getScene().getWindow());
            editorStage.setScene(new Scene(root));

            ActivityEditorController controller = loader.getController();
            controller.initialize(currentProject.getActivityList());

            editorStage.showAndWait();

            List<Activity> newActivities = controller.getNewActivities();
            if (newActivities != null) {
                for (Activity activity : newActivities) {
                    currentProject.addActivity(activity);
                    activityList.add(activity);
                    historyLogs.add("Added Activity: " + activity.getTitle()); // Hinzufügen zur Historie
                }
                activityListView.refresh();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openActivityEditor(Activity selectedActivity) {
        try {
            SceneManager sceneManager = SceneManager.getInstance();
            ActivityEditorController controller = sceneManager.showDialog("ActivityEditor.fxml", "Edit Activity");

            // Initialize controller with selected activity
            controller.initialize(selectedActivity);

            historyLogs.add("Edited Activity: " + selectedActivity.getTitle());
            activityListView.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openHistorieView() {
        try {
            SceneManager sceneManager = SceneManager.getInstance();
            HistorieViewController controller = sceneManager.showDialog("resources/com/lulakssoft/activitymanagement/HistorieView.fxml", "Historie");

            // Initialize controller with history logs
            controller.initialize(historyLogs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

