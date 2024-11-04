package com.lulakssoft.activitymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
            private final Label iconLabel = new Label("🔍"); // Icon for hover

            @Override
            protected void updateItem(Activity activity, boolean empty) {
                super.updateItem(activity, empty);
                if (empty || activity == null) {
                    setGraphic(null);
                } else {

                    Label labelTitle = new Label(activity.getTitle());

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS); // Allow the spacer to grow

                    HBox content = new HBox(labelTitle,spacer);
                    content.setAlignment(Pos.CENTER_LEFT); // Align the label to the left

                    content.setOnMouseEntered(event -> {
                        if (!content.getChildren().contains(iconLabel)) {
                            content.getChildren().add(iconLabel);
                        }
                    });

                    content.setOnMouseExited(event -> {
                            content.getChildren().remove(iconLabel);
                    });
                    setGraphic(content);

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
                for (Activity activity : newActivities) {
                    currentProject.addActivity(activity);
                }
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
