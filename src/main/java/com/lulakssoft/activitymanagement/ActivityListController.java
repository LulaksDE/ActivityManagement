package com.lulakssoft.activitymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ActivityListController {

    @FXML
    private ListView<Activity> activityListView;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    private Activity selectedActivity;

    private Project currentProject;

    @FXML
    public void initialize(Project project) {
        currentProject = project;
        ObservableList<Activity> activityList = FXCollections.observableArrayList(project.getActivityList());
        activityListView.setItems(activityList);

        // Setze den benutzerdefinierten Zell-Renderer für die Anzeige der Titel
        activityListView.setCellFactory(listView -> new ListCell<Activity>() {
            @Override
            protected void updateItem(Activity activity, boolean empty) {
                super.updateItem(activity, empty);
                if (empty || activity == null) {
                    setText(null);
                } else {
                    setText(activity.getTitle());  // Zeige nur den Titel der Aktivität an
                }
            }
        });

        addButton.setOnAction(e -> handleAddActivity());
        deleteButton.setOnAction(e -> handleDeleteActivity());
        activityListView.setOnMouseClicked(this::handleDoubleClick);

    }

    private void openEditor(){
        try {
            // Lade das ActivityEditor-FXML-Layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActivityEditor.fxml"));
            Parent root = loader.load();

            // Erstelle eine neue Stage (Fenster) für den Editor
            Stage editorStage = new Stage();
            editorStage.setTitle("Add New Activities");
            editorStage.initModality(Modality.WINDOW_MODAL);
            editorStage.initOwner(addButton.getScene().getWindow());
            editorStage.setScene(new Scene(root));
            ActivityEditorController controller = loader.getController();
            controller.initialize(currentProject.getActivityList());

            // Zeige das Fenster an und warte, bis es geschlossen wird
            editorStage.showAndWait();


            List<Activity> newActivities = controller.getNewActivities();
            if (newActivities != null) {
                currentProject.getActivityList().addAll(newActivities);
                activityListView.getItems().addAll(newActivities);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAddActivity() {
        openEditor();
    }

    private void handleDoubleClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();
            if (selectedActivity != null) {
                openActivityEditor(selectedActivity);
            }
        }
    }

    private void openActivityEditor(Activity selectedActivity) {
        try {
            // Lade das ActivityEditor-FXML-Layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActivityEditor.fxml"));
            Parent root = loader.load();

            // Erstelle eine neue Stage (Fenster) für den Editor
            Stage editorStage = new Stage();
            editorStage.setTitle("Edit Activity");
            editorStage.initModality(Modality.WINDOW_MODAL);
            editorStage.initOwner(addButton.getScene().getWindow());
            editorStage.setScene(new Scene(root));
            ActivityEditorController controller = loader.getController();
            controller.initialize(selectedActivity);

            // Zeige das Fenster an und warte, bis es geschlossen wird
            editorStage.showAndWait();

            // Aktualisiere die Anzeige, wenn die Aktivität geändert wurde
            activityListView.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteActivity() {
        Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();
        currentProject.removeActivity(selectedActivity);
        activityListView.getItems().remove(selectedActivity);
    }
}
