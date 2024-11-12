package com.lulakssoft.activitymanagement;

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

    private Project currentProject;

    // Liste für Historie-Logs
    private List<String> historyLogs = new ArrayList<>();

    @FXML
    public void initialize(Project project) {
        currentProject = project;
        activityList = FXCollections.observableArrayList(project.getActivityList());
        activityListView.setItems(activityList);

        // Filter-Listener für das Suchfeld
        FilteredList<Activity> filteredActivities = new FilteredList<>(activityList, p -> true);
        activityListView.setItems(filteredActivities);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredActivities.setPredicate(activity -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                return activity.getTitle().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        // Setze den benutzerdefinierten Zell-Renderer
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

        addButton.setOnAction(e -> handleAddActivity());
        deleteButton.setOnAction(e -> handleDeleteActivity());
        historieButton.setOnAction(e -> openHistorieView());  // Historie-Button Aktion

        activityListView.setOnMouseClicked(this::handleDoubleClick);
    }

    private void handleAddActivity() {
        openEditor();
        activityListView.refresh();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActivityEditor.fxml"));
            Parent root = loader.load();

            Stage editorStage = new Stage();
            editorStage.setTitle("Edit Activity");
            editorStage.initModality(Modality.WINDOW_MODAL);
            editorStage.initOwner(addButton.getScene().getWindow());
            editorStage.setScene(new Scene(root));

            ActivityEditorController controller = loader.getController();
            controller.initialize(selectedActivity);

            editorStage.showAndWait();
            historyLogs.add("Edited Activity: " + selectedActivity.getTitle()); // Hinzufügen zur Historie
            activityListView.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openHistorieView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HistorieView.fxml"));
            Parent root = loader.load();

            Stage historieStage = new Stage();
            historieStage.setTitle("Historie");
            historieStage.initModality(Modality.WINDOW_MODAL);
            historieStage.initOwner(historieButton.getScene().getWindow());
            historieStage.setScene(new Scene(root));
            HistorieViewController controller = loader.getController();
            controller.initialize(historyLogs);

            historieStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

