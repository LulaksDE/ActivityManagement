package com.lulakssoft.activitymanagement.adapter.ui;

import com.lulakssoft.activitymanagement.HistoryManager;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;
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

    private LoggerNotifier logger = LoggerFactory.getLogger();

    @FXML
    public void initialize() {
        this.historyLogs = HistoryManager.getInstance().getHistoryLogs();

        historyListView.setItems(FXCollections.observableArrayList(historyLogs));
        clearButton.setOnAction(e -> clearHistory());
    }

    private void clearHistory() {
        historyLogs.clear();
        historyListView.getItems().clear();
        logger.logInfo("History cleared");
    }

    public List<String> getHistoryData() {
        logger.logInfo("getHistoryData");
        return historyLogs;
    }
}
