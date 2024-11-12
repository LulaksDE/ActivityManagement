package com.lulakssoft.activitymanagement;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class HistorieViewController {

    @FXML
    private ListView<String> historieListView; // ListView für Historie-Logs

    public void setHistorieData(List<String> historyLogs) {
        historieListView.setItems(FXCollections.observableArrayList(historyLogs));
    }
}
