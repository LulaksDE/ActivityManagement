package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.notification.*;
import com.lulakssoft.activitymanagement.user.role.PermissionChecker;
import com.lulakssoft.activitymanagement.user.User;
import com.lulakssoft.activitymanagement.user.UserManager;
import com.lulakssoft.activitymanagement.user.UserRole;
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

    private final LoggerNotifier logger = LoggerFactory.getLogger();


    @FXML
    public void initialize() {
        ProjectManager projectManager = ProjectManager.getInstance();
        UserManager userManager = UserManager.INSTANCE;
        currentProject = projectManager.getCurrentProject();
        activityList = FXCollections.observableArrayList(currentProject.getActivityList());
        activityListView.setItems(activityList);

        User currentUser = userManager.getCurrentUser();
        PermissionChecker.configureUIComponent(historyButton, currentUser,
                UserRole::canSeeHistory);

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

        // Custom cell factory for the ListView to show the activity title and icon
        activityListView.setCellFactory(listView -> new ListCell<>() {
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
        historyButton.setOnAction(e -> openHistoryView());

        activityListView.setOnMouseClicked(this::handleDoubleClick);
    }

    private void handleAddActivity() {
        openEditor();
        activityListView.refresh();
    }

    private void handleDeleteActivity() {
        Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();
        if (selectedActivity != null) {
            notifyActivityDeleted(selectedActivity);

            currentProject.removeActivity(selectedActivity);
            activityList.remove(selectedActivity);

            ActivityManager.getInstance().deleteActivity(selectedActivity);

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
            ActivityManager.getInstance().clearCurrentEditingActivity();

            SceneManager sceneManager = SceneManager.getInstance();
            ActivityEditorController controller = sceneManager.openModalWindow(
                    addButton.getScene().getWindow(),
                    SceneManager.ACTIVITY_EDITOR,
                    "Add New Activities"
            );
            controller.initialize();

            List<Activity> newActivities = controller.getNewActivities();
            if (newActivities != null) {
                for (Activity activity : newActivities) {
                    currentProject.addActivity(activity);
                    activityList.add(activity);
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
            String originalTitle = selectedActivity.getTitle();
            String originalDescription = selectedActivity.getDescription();

            ActivityManager.getInstance().setCurrentEditingActivity(selectedActivity);

            SceneManager sceneManager = SceneManager.getInstance();
            ActivityEditorController controller = sceneManager.openModalWindow(
                    addButton.getScene().getWindow(),
                    SceneManager.ACTIVITY_EDITOR,
                    "Edit Activity"
            );
            controller.initialize();


            boolean wasUpdated = !originalTitle.equals(selectedActivity.getTitle()) ||
                    !originalDescription.equals(selectedActivity.getDescription());

            if (wasUpdated) {
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
            User currentUser = UserManager.INSTANCE.getCurrentUser();
            if (!PermissionChecker.checkPermission(currentUser.getRole(),
                    UserRole::canSeeHistory)) {
                showBannerNotification("You don't have permissions to see the history.");
                return;
            }

            SceneManager sceneManager = SceneManager.getInstance();
            HistoryViewController controller = sceneManager.openModalWindow(
                    historyButton.getScene().getWindow(),
                    SceneManager.HISTORY_VIEW,
                    "History View"
            );
            controller.initialize();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error when opening history view: " + e.getMessage());
        }
    }

    @Override
    public void notifyActivityCreated(Activity activity) {
        showPopupNotification(activity.getTitle(), activity.getDescription());
        logger.logInfo("Activity created: " + activity.getTitle());
        HistoryManager.getInstance().addLogEntry("Created Activity: " + activity.getTitle());
    }

    @Override
    public void notifyActivityUpdated(Activity activity) {
        showBannerNotification(activity.getTitle() + " updated");
        logger.logInfo("Activity updated: " + activity.getTitle());
        HistoryManager.getInstance().addLogEntry("Updated Activity: " + activity.getTitle());
    }

    @Override
    public void notifyActivityDeleted(Activity activity) {
        showPopupNotification(activity.getTitle(), activity.getDescription());
        logger.logInfo("Activity deleted: " + activity.getTitle());
        HistoryManager.getInstance().addLogEntry("Deleted Activity: " + activity.getTitle());
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
        Window currentWindow = activityListView.getScene().getWindow();
        Toast toast = Toast.makeText(currentWindow, message, 3000);
        toast.show();
    }

    @Override
    public void sendNotification(String message, String receiver) {
        showPopupNotification(message, receiver);
    }
}

