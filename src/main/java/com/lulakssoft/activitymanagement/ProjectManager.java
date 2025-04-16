package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.database.ProjectRepository;
import com.lulakssoft.activitymanagement.user.User;
import com.lulakssoft.activitymanagement.user.UserManager;

import java.util.ArrayList;
import java.util.List;

// Managing class for different projects
public class ProjectManager {
    private static ProjectManager instance;
    private final ProjectRepository projectRepository;
    private Project currentProject;

    private ProjectManager() {
        projectRepository = new ProjectRepository();
        initializeDefaultProjects();
    }

    private void initializeDefaultProjects() {
        // Nur wenn die Tabelle leer ist
        if (projectRepository.findAll().isEmpty()) {
            UserManager userManager = UserManager.INSTANCE;
            User currentUser = userManager.getCurrentUser();
            if (currentUser != null) {
                List<User> allUsers = userManager.getAllUsers();
                projectRepository.save(new Project("Project A", currentUser, allUsers));
                projectRepository.save(new Project("Project B", currentUser, allUsers));
                projectRepository.save(new Project("Project C", currentUser, allUsers));
            }
        }
    }

    public static ProjectManager getInstance() {
        if (instance == null) {
            instance = new ProjectManager();
        }
        return instance;
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public void addProject(Project project) {
        projectRepository.save(project);
    }

    public List<Project> getProjectsForUser(User user) {
        return projectRepository.findProjectsByUser(user.getId());
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }
}
