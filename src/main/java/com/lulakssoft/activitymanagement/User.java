package com.lulakssoft.activitymanagement;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;  // Benutzername
    private List<Project> projectList;  // Liste der Projekte des Benutzers

    public User(String username) {
        this.username = username;
        this.projectList = new ArrayList<>();
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