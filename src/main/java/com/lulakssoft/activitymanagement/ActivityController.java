package com.lulakssoft.activitymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class ActivityController {

    public void addActivityToProject(Project project, Activity activity) {
        project.addActivity(activity);
    }

    public void removeActivityFromProject(Project project, Activity activity) {
        project.removeActivity(activity);
    }

    public List<Activity> getAllActivities(Project project) {
        return project.getActivityList();
    }

    // Weitere Methoden wie Activity filtern, sortieren, etc., könnten hier hinzugefügt werden
}
