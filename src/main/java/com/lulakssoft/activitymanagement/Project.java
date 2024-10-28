package com.lulakssoft.activitymanagement;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;
    private List<Activity> activityList;

    public Project(String name) {
        this.name = name;
        this.activityList = new ArrayList<>();
    }

    public void addAufgabe(Activity activity) {
        activityList.add(activity);
    }

    public void removeAufgabe(Activity activity) {
        activityList.remove(activity);
    }

    public List<Activity> getActivityList() {
        return activityList;
    }
}
