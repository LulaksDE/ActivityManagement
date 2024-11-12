package com.lulakssoft.activitymanagement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Project {
    private final String id;  // ID des Projekts
    private String name;  // Name des Projekts
    private final List<Activity> activityList;  // Liste der Aktivitäten im Projekt
    private User creator;  // Benutzer, der das Projekt erstellt hat

    public Project(String name, User creator) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.activityList = new ArrayList<>();
        this.creator = creator;
        // Füge eine Standardaktivität hinzu
        activityList.add(new Activity(creator,"Activity 1", "Description 1", LocalDate.now().plusDays(7), false));
        activityList.add(new Activity(creator,"Activity 2", "Description 2", LocalDate.now().plusDays(14), false));
        activityList.add(new Activity(creator,"Activity 3", "Description 3", LocalDate.now().plusDays(21), false));
        activityList.add(new Activity(creator,"Activity 4", "Description 4", LocalDate.now().plusDays(28), false));
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

    public String getId() {
        return id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", activityList=" + this.activityList.toString() +
                '}';
    }
}
