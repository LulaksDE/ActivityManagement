package com.lulakssoft.activitymanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class MainViewController {
    @FXML
    private Label titleLabel;  // Zugriff auf das Label aus FXML

    @FXML
    private ListView<String> activityListView;  // Zugriff auf die ListView aus FXML

    @FXML
    private Button addButton;  // Zugriff auf den Button aus FXML

    // Initialisierung und Event-Handler
    @FXML
    private void initialize() {
        titleLabel.setText("Activity Management System");  // Beispiel: Überschreiben des Texts
        addButton.setOnAction(e -> handleAddActivity());
    }

    private void handleAddActivity() {
        // Code, der beim Klicken des "Add Task" Buttons ausgeführt wird
        System.out.println("Activity added!");
    }
}
