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
import java.util.List;

public class ActivityListController {

    @FXML
    private TextField searchField; // Suchfeld

    @FXML
    private ListView<Activity> activityListView; // ListView für die Aktivitäten

    @FXML
    private Button addButton; // Button zum Hinzufügen einer Aktivität

    @FXML
    private Button deleteButton; // Button zum Löschen einer Aktivität

    private ObservableList<Activity> activityList; // Liste der Aktivitäten

    private Project currentProject; // Aktuelles Projekt

    @FXML
    public void initialize(Project project) {
        currentProject = project;
        activityList = FXCollections.observableArrayList(project.getActivityList());
        activityListView.setItems(activityList);

        // Erstelle eine gefilterte Liste und setze sie in die ListView
        FilteredList<Activity> filteredActivities = new FilteredList<>(activityList, p -> true);
        activityListView.setItems(filteredActivities);

        // Filter-Listener für das Suchfeld
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredActivities.setPredicate(activity -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return activity.getTitle().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Setze den benutzerdefinierten Zell-Renderer für die Anzeige der Titel
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

        // Aktion für den Hinzufügen-Button
        addButton.setOnAction(e -> handleAddActivity());

        // Aktion für den Entfernen-Button
        deleteButton.setOnAction(e -> handleDeleteActivity());

        // Doppelklick-Handler für die ListView
        activityListView.setOnMouseClicked(this::handleDoubleClick);
    }

    private void handleAddActivity() {
        System.out.println("Opening Activity Editor for new activity.");
        openEditor();
        activityListView.refresh();
    }

    private void handleDeleteActivity() {
        Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();
        if (selectedActivity != null) {
            System.out.println("Deleting activity: " + selectedActivity.getTitle());
            currentProject.removeActivity(selectedActivity);
            activityList.remove(selectedActivity); // Entfernen aus der ObservableList
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
                    currentProject.addActivity(activity); // Zum Projekt hinzufügen
                    activityList.add(activity);           // Zur ObservableList hinzufügen
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
            activityListView.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
