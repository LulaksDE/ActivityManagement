package com.lulakssoft.activitymanagement.adapter.ui;

import com.lulakssoft.activitymanagement.domain.entities.activity.ActivityManager;
import com.lulakssoft.activitymanagement.HistoryManager;
import com.lulakssoft.activitymanagement.domain.entities.proejct.ProjectManager;
import com.lulakssoft.activitymanagement.domain.entities.activity.Activity;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.adapter.notification.Toast;
import com.lulakssoft.activitymanagement.adapter.notification.UINotifier;
import com.lulakssoft.activitymanagement.domain.entities.user.UserManager;
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

    private ProjectManager projectManager;

    @FXML
    public void initialize() {
        this.projectManager = ProjectManager.INSTANCE;
        dueDatePicker.setValue(LocalDate.now().plusDays(7));
        completedCheckBox.setSelected(false);

        priorityChoiceBox.setItems(FXCollections.observableArrayList("Low", "Medium", "High"));
        priorityChoiceBox.setValue("Low");

        saveButton.setOnAction(event -> handleCreate());
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void handleCreate() {
        if (titleField.getText().trim().isEmpty()) {
            showAlert("Please enter a title.");
            return;
        }

        Activity newActivity = new Activity(
                UserManager.INSTANCE.getCurrentUser().getId(),
                titleField.getText(),
                descriptionArea.getText(),
                priorityChoiceBox.getValue(),
                dueDatePicker.getValue(),
                completedCheckBox.isSelected()
        );

        ActivityManager.INSTANCE.saveActivity(newActivity);
        projectManager.getCurrentProject().addActivity(newActivity);

        showBannerNotification("Activity created: " + newActivity.getTitle());
        HistoryManager.getInstance().addLogEntry("Created Activity: " + newActivity.getTitle());

        if (!keepPropertiesCheckBox.isSelected()) {
            clearFields();
        }

        if (!keepPropertiesCheckBox.isSelected()) {
            closeWindow();
        }
    }

    private void clearFields() {
        titleField.clear();
        descriptionArea.clear();
        dueDatePicker.setValue(LocalDate.now().plusDays(7));
        completedCheckBox.setSelected(false);
        priorityChoiceBox.setValue("Low");
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