package com.lulakssoft.activitymanagement;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.List;

public class HistoryViewController {

    @FXML
    private ListView<String> historyListView;

    @FXML
    private Button clearButton;

    private List<String> historyLogs;

    @FXML
    public void initialize(List<String> historyLogs) {
        this.historyLogs = historyLogs;

        historyListView.setItems(FXCollections.observableArrayList(historyLogs));
        clearButton.setOnAction(e -> clearHistory());
    }

    private void clearHistory() {
        historyLogs.clear();
        historyListView.getItems().clear();
    }

    public List<String> getHistoryData() {
        System.out.println("History-Logs: " + historyLogs);
        return historyLogs;
    }
}
