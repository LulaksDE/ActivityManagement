package com.lulakssoft.activitymanagement;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;  // Benutzername
    private List<Project> projectList;  // Liste der Projekte des Benutzers

    public User(String username) {
        this.username = username;
        this.projectList = new ArrayList<>();

        // Füge ein Standardprojekt hinzu
        projectList.add(new Project("Default Project"));
        projectList.add(new Project("Project 1"));
        projectList.add(new Project("Project 2"));
    }

    public static User getCurrentUser() {
        return new User("Default User");
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
}