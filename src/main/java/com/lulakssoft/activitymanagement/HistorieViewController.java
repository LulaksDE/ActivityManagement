package com.lulakssoft.activitymanagement;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class HistorieViewController {

    @FXML
    private ListView<String> historieListView; // ListView für Historie-Logs

    @FXML
    private Button clearButton; // Button zum Löschen der Historie-Logs

    private List<String> historyLogs;

    @FXML
    public void initialize(List<String> historyLogs) {
        this.historyLogs = historyLogs;

        historieListView.setItems(FXCollections.observableArrayList(historyLogs));
        clearButton.setOnAction(e -> clearHistorie());
    }

    private void clearHistorie() {
        historyLogs.clear();
        historieListView.getItems().clear();
    }

    public List<String> getHistorieData() {
        System.out.println("Historie-Logs: " + historyLogs);
        return historyLogs;
    }
}
