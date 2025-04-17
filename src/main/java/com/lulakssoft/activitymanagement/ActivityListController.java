package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.operation.ActivityOperation;
import com.lulakssoft.activitymanagement.operation.ActivityOperationFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

import java.util.List;

public class ActivityListController {

    @FXML
    private ListView<Activity> activityListView;

    @FXML
    private TextField searchField;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button historyButton;

    @FXML
    private Label titleLabel;

    private ObservableList<Activity> observableActivityList;
    private FilteredList<Activity> filteredActivityList;
    private final LoggerNotifier logger = LoggerFactory.getLogger();

    @FXML
    public void initialize() {
        ProjectManager projectManager = ProjectManager.getInstance();
        Project currentProject = projectManager.getCurrentProject();

        if (currentProject != null) {
            titleLabel.setText("Activities: " + currentProject.getName());
            List<Activity> activities = currentProject.getActivityList();
            observableActivityList = FXCollections.observableArrayList(activities);

            filteredActivityList = new FilteredList<>(observableActivityList, p -> true);
            activityListView.setItems(filteredActivityList);

            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredActivityList.setPredicate(activity -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return activity.getTitle().toLowerCase().contains(lowerCaseFilter) ||
                            (activity.getDescription() != null && activity.getDescription().toLowerCase().contains(lowerCaseFilter));
                });
            });

            activityListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Activity activity, boolean empty) {
                    super.updateItem(activity, empty);
                    if (empty || activity == null) {
                        setText(null);
                    } else {
                        setText(activity.getTitle() + (activity.isCompleted() ? " (✓)" : ""));
                    }
                }
            });

            activityListView.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    handleEditActivity();
                }
            });

            addButton.setOnAction(e -> handleAddActivity());
            deleteButton.setOnAction(e -> handleDeleteActivity());
            historyButton.setOnAction(e -> openHistoryView());
        }
    }

    private void handleEditActivity() {
        Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();
        if (selectedActivity != null) {
            ActivityOperation editOperation = ActivityOperationFactory.createEditOperation(
                    selectedActivity, activityListView.getScene().getWindow());
            editOperation.execute();
            refreshActivityList();
        }
    }

    private void handleAddActivity() {
        ActivityOperation createOperation = ActivityOperationFactory.createNewOperation(
                addButton.getScene().getWindow());
        createOperation.execute();
        refreshActivityList();
    }

    private void handleDeleteActivity() {
        Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();
        if (selectedActivity != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Activity");
            confirmation.setHeaderText("Delete Activity");
            confirmation.setContentText("Are you sure you want to delete \"" + selectedActivity.getTitle() + "\"?");

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    ProjectManager.getInstance().getCurrentProject().removeActivity(selectedActivity);
                    refreshActivityList();
                }
            });
        }
    }

    private void openHistoryView() {
        try {
            SceneManager sceneManager = SceneManager.getInstance();
            sceneManager.openModalWindow(
                    historyButton.getScene().getWindow(),
                    SceneManager.HISTORY_VIEW,
                    "Activity History");
        } catch (Exception e) {
            logger.logWarning("Failed to open history view: " + e.getMessage());
        }
    }

    private void refreshActivityList() {
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        if (currentProject != null) {
            currentProject.refreshActivities();
            observableActivityList.setAll(currentProject.getActivityList());
        }
    }
}