package com.lulakssoft.activitymanagement.adapter.ui;

import com.lulakssoft.activitymanagement.domain.entities.activity.ActivityManager;
import com.lulakssoft.activitymanagement.HistoryManager;
import com.lulakssoft.activitymanagement.domain.entities.activity.Activity;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.adapter.notification.Toast;
import com.lulakssoft.activitymanagement.adapter.notification.UINotifier;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ActivityEditorController implements UINotifier {

    @FXML
    private TextField titleField;

    @FXML
    private Label activityIdLabel;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button updateButton;

    @FXML
    private Button cancelButton;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private CheckBox completedCheckBox;

    @FXML
    private ChoiceBox<String> priorityChoiceBox;

    private final LoggerNotifier logger = LoggerFactory.getLogger();

    @FXML
    public void initialize() {
        ActivityManager activityManager = ActivityManager.INSTANCE;
        Activity editingActivity = activityManager.getCurrentEditingActivity();
        if (editingActivity != null) {
            titleField.setText(editingActivity.getTitle());
            descriptionArea.setText(editingActivity.getDescription());
            activityIdLabel.setText("ID: " + editingActivity.getId());

            if (editingActivity.getDueDate() != null) {
                dueDatePicker.setValue(editingActivity.getDueDate());
            }

            completedCheckBox.setSelected(editingActivity.isCompleted());

            priorityChoiceBox.setItems(FXCollections.observableArrayList("Low", "Medium", "High"));
            priorityChoiceBox.setValue(editingActivity.getPriority());

            updateButton.setText("Update");
        } else {
            showAlert("No activity selected for editing.");
            logger.logWarning("No activity selected for editing.");
            closeWindow();
        }

        updateButton.setOnAction(event -> handleUpdate());
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void handleUpdate() {
        ActivityManager activityManager = ActivityManager.INSTANCE;
        if (titleField.getText().trim().isEmpty()) {
            showAlert("Please enter a title.");
            return;
        }

        Activity editingActivity = activityManager.getCurrentEditingActivity();
        if (editingActivity != null) {
            editingActivity.setTitle(titleField.getText());
            editingActivity.setDescription(descriptionArea.getText());
            editingActivity.setDueDate(dueDatePicker.getValue());
            editingActivity.setCompleted(completedCheckBox.isSelected());
            editingActivity.setPriority(priorityChoiceBox.getValue());

            activityManager.saveActivity(editingActivity);
            activityManager.clearCurrentEditingActivity();

            showBannerNotification("Activity updated: " + editingActivity.getTitle());
            HistoryManager.getInstance().addLogEntry("Updated Activity: " + editingActivity.getTitle());
            logger.logInfo("Activity updated: " + editingActivity.getTitle());

            closeWindow();
        }
    }

    private void handleCancel() {
        ActivityManager.INSTANCE.clearCurrentEditingActivity();
        closeWindow();
    }

    private void closeWindow() {
        logger.logInfo("Closing Activity Editor");
        Stage stage = (Stage) updateButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // UINotifier interface implementation
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
        Window currentWindow = updateButton.getScene().getWindow();
        Toast toast = Toast.makeText(currentWindow, message, 3000);
        toast.show();
    }

    @Override
    public void sendNotification(String message, String receiver) {
        showPopupNotification(message, receiver);
    }
}