package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.Notification.ActivityNotifier;
import com.lulakssoft.activitymanagement.Notification.Toast;
import com.lulakssoft.activitymanagement.Notification.UINotifier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.List;

public class ActivityListController implements UINotifier, ActivityNotifier {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Activity> activityListView;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button historyButton;

    private ObservableList<Activity> activityList;

    private Project currentProject;

    // Liste für Historie-Logs
    private List<String> historyLogs = new ArrayList<>();

    @FXML
    public void initialize() {
        ProjectManager projectManager = ProjectManager.getInstance();
        currentProject = projectManager.getCurrentProject();
        activityList = FXCollections.observableArrayList(currentProject.getActivityList());
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
        historyButton.setOnAction(e -> openHistoryView());  // Historie-Button Aktion

        activityListView.setOnMouseClicked(this::handleDoubleClick);
    }

    private void handleAddActivity() {
        openEditor();
        activityListView.refresh();
    }

    private void handleDeleteActivity() {
        Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();
        if (selectedActivity != null) {
            // Notify the activity deletion
            notifyActivityDeleted(selectedActivity);

            currentProject.removeActivity(selectedActivity);
            activityList.remove(selectedActivity);
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
            SceneManager sceneManager = SceneManager.getInstance();
            ActivityEditorController controller = sceneManager.openModalWindow(
                    addButton.getScene().getWindow(),
                    SceneManager.ACTIVITY_EDITOR,
                    "Add New Activities",
                    editorController -> editorController.initialize(currentProject.getActivityList())
            );

            List<Activity> newActivities = controller.getNewActivities();
            if (newActivities != null) {
                for (Activity activity : newActivities) {
                    currentProject.addActivity(activity);
                    activityList.add(activity);
                    // Notify the activity creation
                    notifyActivityCreated(activity);
                }
                activityListView.refresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error when opening activity editor: " + e.getMessage());
        }
    }

    private void openActivityEditor(Activity selectedActivity) {
        try {
            // Save original values for comparison
            String originalTitle = selectedActivity.getTitle();
            String originalDescription = selectedActivity.getDescription();

            SceneManager sceneManager = SceneManager.getInstance();
            ActivityEditorController controller = sceneManager.openModalWindow(
                    addButton.getScene().getWindow(),
                    SceneManager.ACTIVITY_EDITOR,
                    "Edit Activity",
                    editorController -> editorController.initialize(selectedActivity)
            );

            // Check if the activity was updated
            boolean wasUpdated = !originalTitle.equals(selectedActivity.getTitle()) ||
                    !originalDescription.equals(selectedActivity.getDescription());

            if (wasUpdated) {
                // Notify the activity update
                notifyActivityUpdated(selectedActivity);
            }

            activityListView.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error when opening activity editor: " + e.getMessage());
        }
    }

    private void openHistoryView() {
        try {
            SceneManager sceneManager = SceneManager.getInstance();
            // initializing need more work, values are initialized too late outside openModalWindow
            HistoryViewController controller = sceneManager.openModalWindow(
                    historyButton.getScene().getWindow(),
                    SceneManager.HISTORY_VIEW,
                    "Historie",
                    historyController -> historyController.initialize(historyLogs)
            ); // History should use managing class in the future
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error when opening history view: " + e.getMessage());
        }
    }

    @Override
    public void notifyActivityCreated(Activity activity) {
        showPopupNotification(activity.getTitle(), activity.getDescription());
        historyLogs.add("Created Activity: " + activity.getTitle());
    }

    @Override
    public void notifyActivityUpdated(Activity activity) {
        showBannerNotification(activity.getTitle() + " updated");
        historyLogs.add("Updated Activity: " + activity.getTitle());
    }

    @Override
    public void notifyActivityDeleted(Activity activity) {
        showPopupNotification(activity.getTitle(), activity.getDescription());
        historyLogs.add("Deleted Activity: " + activity.getTitle());
    }

    @Override
    public void showPopupNotification(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText("Activity Notification");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void showBannerNotification(String message) {
        // Show toast
        Window currentWindow = activityListView.getScene().getWindow();
        Toast toast = Toast.makeText(currentWindow, message, 3000);
        toast.show();
    }

    @Override
    public void sendNotification(String message, String receiver) {
        showPopupNotification(message, receiver);
    }
}

