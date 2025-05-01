package com.lulakssoft.activitymanagement.adapter.ui;

import com.lulakssoft.activitymanagement.application.service.ActivityService;
import com.lulakssoft.activitymanagement.application.service.ProjectService;
import com.lulakssoft.activitymanagement.application.service.UserService;
import com.lulakssoft.activitymanagement.config.ApplicationContext;
import com.lulakssoft.activitymanagement.HistoryManager;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.adapter.notification.Toast;
import com.lulakssoft.activitymanagement.adapter.notification.UINotifier;
import com.lulakssoft.activitymanagement.domain.model.activity.Activity;
import com.lulakssoft.activitymanagement.domain.model.activity.Priority;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.time.LocalDate;

public class ActivityCreatorController implements UINotifier {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private CheckBox completedCheckBox;

    @FXML
    private CheckBox keepPropertiesCheckBox;

    @FXML
    private ChoiceBox<String> priorityChoiceBox;

    private final LoggerNotifier logger = LoggerFactory.getLogger();

    private ProjectService projectService;
    private ActivityService activityService;
    private UserService userService;

    @FXML
    public void initialize() {
        ApplicationContext context = ApplicationContext.getInstance();
        this.projectService = context.getProjectService();
        this.activityService = context.getActivityService();
        this.userService = context.getUserService();
        dueDatePicker.setValue(LocalDate.now().plusDays(7));
        completedCheckBox.setSelected(false);

        priorityChoiceBox.setItems(FXCollections.observableArrayList("Low", "Medium", "High"));
        priorityChoiceBox.setValue("Low");

        saveButton.setOnAction(event -> handleCreate());
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void handleCreate() {
        if (!validateInputFields()) {
            return;
        }

        Activity newActivity = createActivityFromInputs();

        saveActivityToProject(newActivity);

        handlePostSave();
    }

    private boolean validateInputFields() {
        if (titleField.getText().trim().isEmpty()) {
            showAlert("Please enter a title.");
            return false;
        }
        return true;
    }

    private Activity createActivityFromInputs() {
        return new Activity(
                projectService.getCurrentProject().getId(),
                userService.getCurrentUser().getId(),
                titleField.getText(),
                descriptionArea.getText(),
                Priority.fromString(priorityChoiceBox.getValue()),
                dueDatePicker.getValue()
        );
    }

    private void saveActivityToProject(Activity activity) {

        activityService.saveActivity(activity);

        showBannerNotification("Activity created: " + activity.getTitle());
        HistoryManager.getInstance().addLogEntry("Aktivität created: " + activity.getTitle());
    }

    private void clearFields() {
        titleField.clear();
        descriptionArea.clear();
        dueDatePicker.setValue(LocalDate.now().plusDays(7));
        completedCheckBox.setSelected(false);
        priorityChoiceBox.setValue("Low");
    }

    private void handlePostSave() {
        if (!keepPropertiesCheckBox.isSelected()) {
            clearFields();
            closeWindow();
        }
    }

    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        logger.logInfo("Closing Activity Creator");
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
        Window currentWindow = saveButton.getScene().getWindow();
        Toast toast = Toast.makeText(currentWindow, message, 3000);
        toast.show();
    }

    @Override
    public void sendNotification(String message, String receiver) {
        showPopupNotification(message, receiver);
    }
}