package com.lulakssoft.activitymanagement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class User {
    private String username;  // Benutzername
    private List<Project> projectList;  // Liste der Projekte des Benutzers
    private List<Activity> activityList;  // Liste der Aktivitäten des Benutzers

    public User(String username) {
        this.username = username;
        this.projectList = new ArrayList<>();

        // Füge ein Standardprojekt hinzu
        projectList.add(new Project("Default Project", this));
        projectList.add(new Project("Project 1", this));
        projectList.add(new Project("Project 2", this));
    }

    public void addProject(Project project) {
        projectList.add(project);
    }

    public void removeProject(Project project) {
        projectList.remove(project);
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", projectList=" + projectList +
                '}';
    }
}