package com.lulakssoft.activitymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class MainViewController {

    @FXML
    private ListView<Activity> activityListView;

    @FXML
    private TextField titleField;

    @FXML
    private Label activityIdLabel;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private CheckBox completedCheckBox;

    private ObservableList<Activity> activityList = FXCollections.observableArrayList();  // Liste der Aktivitäten

    private Project currentProject = new Project("Default Project");

    private Activity currentActivity;

    @FXML
    private void initialize() {

        // Setze den benutzerdefinierten Zell-Renderer für die Anzeige der Titel
        activityListView.setCellFactory(listView -> new ListCell<Activity>() {
            @Override
            protected void updateItem(Activity activity, boolean empty) {
                super.updateItem(activity, empty);
                if (empty || activity == null) {
                    setText(null);
                } else {
                    setText(activity.getTitle());  // Zeige nur den Titel der Aktivität an
                }
            }
        });

        if (activityList.isEmpty()) {
            activityList.add(new Activity("1", "Activity 1", "Description 1", LocalDate.now().plusDays(7), false));
            activityList.add(new Activity("2", "Activity 2", "Description 2", LocalDate.now().plusDays(14), false));
            activityList.add(new Activity("3", "Activity 3", "Description 3", LocalDate.now().plusDays(21), false));
        }

        // Weise die ObservableList dem ListView zu
        activityListView.setItems(activityList);



        addButton.setOnAction(e -> handleAddActivity());
        updateButton.setOnAction(e -> handleUpdateActivity());
        deleteButton.setOnAction(e -> handleDeleteActivity());
        dueDatePicker.setValue(LocalDate.now().plusDays(7));
        completedCheckBox.setSelected(false);
        activityIdLabel.setText("");
        activityListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleActivitySelection());
    }

    private void handleActivitySelection() {
        Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();

        if (selectedActivity != null) {
            titleField.setText(selectedActivity.getTitle());
            descriptionArea.setText(selectedActivity.getDescription());
            dueDatePicker.setValue(selectedActivity.getDueDate());
            completedCheckBox.setSelected(selectedActivity.isCompleted());
            activityIdLabel.setText(selectedActivity.getId());
        }
    }


    private void handleAddActivity() {
        String id = String.valueOf(currentProject.getActivityList().size() + 1);
        String title = titleField.getText();
        String description = descriptionArea.getText();
        LocalDate dueDate = dueDatePicker.getValue();
        boolean completed = completedCheckBox.isSelected();

        if (title.isEmpty()) {
            titleField.setStyle("-fx-border-color: red");
            return;
        }

        Activity activity = new Activity(id, title, description, dueDate, completed);
        currentProject.addActivity(activity);
        activityListView.getItems().add(activity);
        titleField.setText("");
        dueDatePicker.setValue(LocalDate.now().plusDays(7));
        descriptionArea.setText("");
        completedCheckBox.setSelected(false);
        activityIdLabel.setText("");
    }

    private void handleUpdateActivity() {
        Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();

        if (selectedActivity == null) {
            return;
        }
        selectedActivity.setTitle(titleField.getText());
        selectedActivity.setDescription(descriptionArea.getText());
        selectedActivity.setDueDate(dueDatePicker.getValue());
        selectedActivity.setCompleted(completedCheckBox.isSelected());

        activityListView.refresh();
    }

    private void handleDeleteActivity() {
        Activity selectedActivity = activityListView.getSelectionModel().getSelectedItem();
        currentProject.removeActivity(selectedActivity);
        activityListView.getItems().remove(selectedActivity);

        titleField.setText("");
        descriptionArea.setText("");
        dueDatePicker.setValue(LocalDate.now().plusDays(7));
        completedCheckBox.setSelected(false);
        activityIdLabel.setText("");
    }
}
