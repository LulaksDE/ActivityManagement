package com.lulakssoft.activitymanagement;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class MainViewController {

    @FXML
    private ComboBox<String> projectComboBox;

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button loadButton;

    private List<Project> projectList;

    @FXML
    private void initialize() {

        // Lade die Projekte des Benutzers
        projectList = User.getCurrentUser().getProjectList();

        // Füge die Projekte zur ComboBox hinzu

        projectList.forEach(project -> projectComboBox.getItems().add(project.getName()));

        createButton.setOnAction(e -> {handleCreate();});

        deleteButton.setOnAction(e -> {
            handleDelete();
        });
        loadButton.setOnAction(e -> {
            handleLoad();
        });
    }

    private void handleCreate() {

        for (Project project : projectList) {
            if (project.getName().equals(projectComboBox.getValue())) {

                System.out.println("Projekt bereits vorhanden");

                // Projekt bereits vorhanden
                return;
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectCreationScreen.fxml"));
            Parent root = loader.load();

            Stage creationStage = new Stage();
            creationStage.setTitle("Projekt erstellen");
            creationStage.setScene(new Scene(root));
            creationStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            creationStage.showAndWait();

            // Update the project list
            projectComboBox.getItems().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDelete() {

            if (projectComboBox.getSelectionModel().getSelectedIndex() == -1) {
                return;
            }

            projectList.remove(projectComboBox.getSelectionModel().getSelectedIndex());
            projectComboBox.getItems().remove(projectComboBox.getSelectionModel().getSelectedIndex());
    }

    private void handleLoad() {

        if (projectComboBox.getSelectionModel().getSelectedIndex() == -1) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActivityList.fxml"));
            Parent root = loader.load();

            // Erstelle eine neue Stage (Fenster) für den Editor
            Stage activityListStage = new Stage();
            activityListStage.setTitle("Add New Activities");
            activityListStage.initModality(Modality.WINDOW_MODAL);
            activityListStage.initOwner(loadButton.getScene().getWindow());
            activityListStage.setScene(new Scene(root));
            ActivityListController controller = loader.getController();
            controller.initialize(projectList.get(projectComboBox.getSelectionModel().getSelectedIndex()));
            activityListStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
