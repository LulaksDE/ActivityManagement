package com.lulakssoft.activitymanagement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public abstract class User {
    private String username;  // Benutzername
    private String password;  // Passwort
    private final List<Project> projectList;  // Liste der Projekte des Benutzers

    public User(String username, String password) {
        this.username = username;
        this.password = encodePassword(password);

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = encodePassword(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", projectList=" + projectList.toString() +
                '}';
    }

    private String encodePassword(String password) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(password.getBytes());
    }
}