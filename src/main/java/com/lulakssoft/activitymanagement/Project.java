package com.lulakssoft.activitymanagement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;  // Name des Projekts
    private List<Activity> activityList;  // Liste der Aktivitäten im Projekt

    public Project(String name) {
        this.name = name;
        this.activityList = new ArrayList<>();

        // Füge eine Standardaktivität hinzu
        activityList.add(new Activity("Activity 1", "Description 1", LocalDate.now().plusDays(7), false));
        activityList.add(new Activity("Activity 2", "Description 2", LocalDate.now().plusDays(14), false));
        activityList.add(new Activity("Activity 3", "Description 3", LocalDate.now().plusDays(21), false));
        activityList.add(new Activity("Activity 4", "Description 4", LocalDate.now().plusDays(28), false));
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
