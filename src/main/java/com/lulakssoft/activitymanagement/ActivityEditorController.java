package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.notification.UINotifier;
import com.lulakssoft.activitymanagement.user.UserManager;
import com.lulakssoft.activitymanagement.notification.Toast;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.time.LocalDate;
import java.util.ArrayList;


public class ActivityEditorController implements UINotifier {

    @FXML
    private TextField titleField;

    @FXML
    private Label activityIdLabel;

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

    private ObservableList<Activity> newActivities = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Check if an activity is being edited
        Activity editingActivity = ActivityManager.getInstance().getCurrentEditingActivity();
        if (editingActivity != null) {
            // Edit mode
            titleField.setText(editingActivity.getTitle());
            descriptionArea.setText(editingActivity.getDescription());

            // Set additional properties if available in Activity
            if (editingActivity.getDueDate() != null) {
                dueDatePicker.setValue(editingActivity.getDueDate());
            }

            completedCheckBox.setSelected(editingActivity.isCompleted());

            if (editingActivity.getPriority() != null) {
                priorityChoiceBox.setValue(editingActivity.getPriority());
            }

            if (editingActivity.getId() != null) {
                activityIdLabel.setText("ID: " + editingActivity.getId());
            }

            // Configure UI for edit mode
            saveButton.setText("Update");
        } else {
            clearFields();
            saveButton.setText("Create");
            completedCheckBox.setSelected(false);
        }

        // Initialize priority options
        priorityChoiceBox.setItems(FXCollections.observableArrayList("Low", "Medium", "High"));
        priorityChoiceBox.setValue(""); // Default value

        // Default due date: one week in the future
        dueDatePicker.setValue(LocalDate.now().plusDays(7));

        // Event handlers for buttons
        saveButton.setOnAction(event -> handleSave());
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void clearFields() {
        titleField.clear();
        descriptionArea.clear();
        activityIdLabel.setText("");
        dueDatePicker.setValue(LocalDate.now().plusDays(7));
        completedCheckBox.setSelected(false);
        priorityChoiceBox.setValue("");
    }

    public ObservableList<Activity> getNewActivities() {
        return newActivities;
    }

    @FXML
    private void handleSave() {
        // Validation
        if (titleField.getText().trim().isEmpty()) {
            showAlert("Please enter a title.");
            return;
        }

        Activity editingActivity = ActivityManager.getInstance().getCurrentEditingActivity();

        if (editingActivity != null) {
            editingActivity.setTitle(titleField.getText());
            editingActivity.setDescription(descriptionArea.getText());
            editingActivity.setDueDate(dueDatePicker.getValue());
            editingActivity.setCompleted(completedCheckBox.isSelected());
            editingActivity.setPriority(priorityChoiceBox.getValue());
            ActivityManager.getInstance().clearCurrentEditingActivity();

            showBannerNotification("Activity updated: " + editingActivity.getTitle());
            HistoryManager.getInstance().addLogEntry("Updated Activity: " + editingActivity.getTitle());
        } else {
            Activity newActivity = new Activity(
                    UserManager.INSTANCE.getCurrentUser(),
                    new ArrayList<>(),
                    titleField.getText(),
                    descriptionArea.getText(),
                    dueDatePicker.getValue(),
                    completedCheckBox.isSelected()
            );
            newActivity.setPriority(priorityChoiceBox.getValue());

            newActivities.add(newActivity);

            // Show notification for creation
            showBannerNotification("Activity created: " + newActivity.getTitle());
            HistoryManager.getInstance().addLogEntry("Created Activity: " + newActivity.getTitle());

            if (!keepPropertiesCheckBox.isSelected()) {
                clearFields();
            }
        }

        // Close dialog if in edit mode
        if (editingActivity != null) {
            closeWindow();
        }
    }

    @FXML
    private void handleCancel() {
        // In edit mode, ensure reference is cleared
        ActivityManager.getInstance().clearCurrentEditingActivity();
        closeWindow();
    }

    private void closeWindow() {
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
        Window currentWindow = saveButton.getScene().getWindow();
        Toast toast = Toast.makeText(currentWindow, message, 3000);
        toast.show();
    }

    @Override
    public void sendNotification(String message, String receiver) {
        showPopupNotification(message, receiver);
    }
}