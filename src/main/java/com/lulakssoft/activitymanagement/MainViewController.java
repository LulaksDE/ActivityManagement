package com.lulakssoft.activitymanagement;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class MainViewController {

    @FXML
    private ListView<String> activityListView;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button addButton;

    @FXML
    private DatePicker dueDatePicker;

    private Project currentProject = new Project("Default Project");

    @FXML
    private void initialize() {

        addButton.setOnAction(e -> handleAddActivity());
        dueDatePicker.setValue(LocalDate.now().plusDays(7));
    }

    private void handleAddActivity() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        LocalDate dueDate = dueDatePicker.getValue();

        Activity activity = new Activity(title, description, dueDate);
        currentProject.addActivity(activity);
        activityListView.getItems().add(activity.getTitle());
        titleField.setText("");
        descriptionArea.setText("");
    }
}
