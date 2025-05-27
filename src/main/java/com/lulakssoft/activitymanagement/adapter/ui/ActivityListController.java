package com.lulakssoft.activitymanagement.adapter.ui;

import com.lulakssoft.activitymanagement.HistoryManager;
import com.lulakssoft.activitymanagement.application.service.ActivityService;
import com.lulakssoft.activitymanagement.application.service.ProjectService;
import com.lulakssoft.activitymanagement.domain.model.project.Project;
import com.lulakssoft.activitymanagement.SceneManager;
import com.lulakssoft.activitymanagement.domain.model.activity.Activity;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;
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
    private final ActivityService activityService;
    private final ProjectService projectService;

    public ActivityListController(ActivityService activityService, ProjectService projectService) {
        this.activityService = activityService;
        this.projectService = projectService;
    }
    @FXML
    public void initialize() {
        Project currentProject = projectService.getCurrentProject();


        if (currentProject != null) {
            titleLabel.setText("Activities: " + currentProject.getName());
            List<Activity> activities = activityService.findActivitiesByProject(currentProject.getId());
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
                        String completionStatus = activity.isCompleted() ? " (✓)" : "";
                        setText(activity.getTitle() + completionStatus);
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
        activityService.setCurrentEditingActivity(activityListView.getSelectionModel().getSelectedItem());
        if (activityService.getCurrentEditingActivity() == null) {
            logger.logWarning("No activity selected for editing.");
        } else {
            try {
                SceneManager sceneManager = SceneManager.getInstance();
                ActivityEditorController controller = sceneManager.openModalWindow(
                        activityListView.getScene().getWindow(),
                        SceneManager.ACTIVITY_EDITOR,
                        "Edit Activity");
                controller.initialize();

                refreshActivityList();

            } catch (Exception e) {
                logger.logError("Failed to edit activity: " + e.getMessage(), e);
            }
        }
    }

    private void handleAddActivity() {
        try {
            SceneManager sceneManager = SceneManager.getInstance();
            ActivityCreatorController controller = sceneManager.openModalWindow(
                    addButton.getScene().getWindow(),
                    SceneManager.ACTIVITY_CREATOR,
                    "Create Activity");
            controller.initialize();

            refreshActivityList();

        } catch (Exception e) {
            logger.logError("Failed to create activity: " + e.getMessage(), e);
        }
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
                    activityService.deleteActivity(selectedActivity);
                    logger.logInfo("Deleted activity: " + selectedActivity.getTitle());
                    HistoryManager.getInstance().addLogEntry("Deleted Activity: " + selectedActivity.getTitle());
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
        Project currentProject = projectService.getCurrentProject();
        if (currentProject != null) {
            observableActivityList.setAll(activityService.findActivitiesByProject(currentProject.getId()));
        }
    }
}