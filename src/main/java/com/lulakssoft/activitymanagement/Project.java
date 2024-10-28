package com.lulakssoft.activitymanagement;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;  // Name des Projekts
    private List<Activity> activityList;  // Liste der Aktivitäten im Projekt

    public Project(String name) {
        this.name = name;
        this.activityList = new ArrayList<>();
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
