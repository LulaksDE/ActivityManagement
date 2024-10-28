package com.lulakssoft.activitymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ActivityController {
    private ObservableList<Activity> activityList = FXCollections.observableArrayList();

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void populateListView(ListView<String> listView) {
        for (Activity activity : activityList) {
            listView.getItems().add(activity.getTitel());
        }
    }
}
