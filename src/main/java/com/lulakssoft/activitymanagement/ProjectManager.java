package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.User.User;
import com.lulakssoft.activitymanagement.User.UserManager;

import java.util.ArrayList;
import java.util.List;

// Managing class for different projects
public class ProjectManager {
    private static ProjectManager instance;
    private final List<Project> projects = new ArrayList<>();
    private Project currentProject;

    private ProjectManager() {
        // create some default projects
        UserManager userManager = UserManager.INSTANCE;
        projects.add(new Project("Project A", userManager.getCurrentUser(), userManager.getAllUsers()));
        projects.add(new Project("Project B", userManager.getCurrentUser(), userManager.getAllUsers()));
        projects.add(new Project("Project C", userManager.getCurrentUser(), userManager.getAllUsers()));
        projects.add(new Project("Project D", userManager.getCurrentUser(), userManager.getAllUsers()));
    }

    public static ProjectManager getInstance() {
        if (instance == null) {
            instance = new ProjectManager();
        }
        return instance;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void addProject(Project project) {
        projects.add(project);
    }

    public List<Project> getProjectsForUser(User user) {
        List<Project> userProjects = new ArrayList<>();
        for (Project project : projects) {
            if (project.getMembers().contains(user)) {
                userProjects.add(project);
            }
        }
        return userProjects;
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

}
