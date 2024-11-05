package com.lulakssoft.activitymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActivityEditorController {

    @FXML
    private TextField titleField;

    @FXML
    private Label activityIdLabel;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private CheckBox completedCheckBox;

    @FXML
    private CheckBox keepPropertiesCheckBox;

    @FXML
    private ChoiceBox<String> priorityChoiceBox; // Referenz zur ChoiceBox für Priorität

    private List<Activity> activityList;

    private List<Activity> newActivities = new ArrayList<>(); // Korrigiert von FXCollections.observableArrayList()

    @FXML
    void initialize(List<Activity> activities) {
        activityList = activities;

        // Priorität Optionen hinzufügen
        priorityChoiceBox.getItems().addAll("Low", "Medium", "High"); // Hinzufügen von Auswahlmöglichkeiten

        addButton.setOnAction(e -> handleAddActivity());
        updateButton.setVisible(false);
        dueDatePicker.setValue(LocalDate.now().plusDays(7));
        completedCheckBox.setSelected(false);
        activityIdLabel.setText("");
        keepPropertiesCheckBox.setSelected(false);
    }

    @FXML
    void initialize(Activity activity) {
        titleField.setText(activity.getTitle());
        descriptionArea.setText(activity.getDescription());
        dueDatePicker.setValue(activity.getDueDate());
        completedCheckBox.setSelected(activity.isCompleted());
        activityIdLabel.setText(activity.getId());

        addButton.setVisible(false);
        updateButton.setOnAction(e -> handleUpdateActivity(activity));
        keepPropertiesCheckBox.setVisible(false);
    }

    private void handleAddActivity() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        LocalDate dueDate = dueDatePicker.getValue();
        boolean completed = completedCheckBox.isSelected();
        String priority = priorityChoiceBox.getValue(); // Holen der ausgewählten Priorität

        if (title.isEmpty()) {
            titleField.setStyle("-fx-border-color: red");
            titleField.requestFocus();
            return;
        } else {
            titleField.setStyle("");
        }

        // Erstellen einer neuen Aktivität mit den Prioritätsinformationen
        Activity newActivity = new Activity(new Admin("admin"), title, description, dueDate, completed);
        newActivity.setPriority(priority); // Setze die Priorität in der Aktivität
        newActivities.add(newActivity);

        if (!keepPropertiesCheckBox.isSelected()) {
            titleField.setText("");
            dueDatePicker.setValue(LocalDate.now().plusDays(7));
            descriptionArea.setText("");
            completedCheckBox.setSelected(false);
            activityIdLabel.setText("");
            priorityChoiceBox.setValue(null); // Setze die Auswahl auf null
        } else {
            titleField.setText(title);
            dueDatePicker.setValue(dueDate);
            descriptionArea.setText(description);
            completedCheckBox.setSelected(completed);
        }
    }

    private void handleUpdateActivity(Activity activity) {
        if (titleField.getText().isEmpty()) {
            titleField.setStyle("-fx-border-color: red");
            titleField.requestFocus();
            return;
        } else {
            titleField.setStyle("");
        }

        System.out.println("Updating activity" + activity);
        activity.setTitle(titleField.getText());
        activity.setDescription(descriptionArea.getText());
        activity.setDueDate(dueDatePicker.getValue());
        activity.setCompleted(completedCheckBox.isSelected());
        activity.setPriority(priorityChoiceBox.getValue()); // Aktualisiere die Priorität

        System.out.println("Updated activity" + activity);
    }

    public List<Activity> getNewActivities() {
        for (Activity activity : newActivities) {
            System.out.println("New activity: " + activity);
        }
        return newActivities;
    }
}
