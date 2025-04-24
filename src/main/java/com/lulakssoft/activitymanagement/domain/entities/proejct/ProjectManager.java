package com.lulakssoft.activitymanagement.domain.entities.proejct;

import com.lulakssoft.activitymanagement.ServiceLocator;
import com.lulakssoft.activitymanagement.domain.repositories.IProjectRepository;
import com.lulakssoft.activitymanagement.domain.entities.user.User;

import java.util.List;

public enum ProjectManager {
    INSTANCE;

    private final IProjectRepository projectRepository;
    private Project currentProject;

    ProjectManager() {
        this.projectRepository = ServiceLocator.getInstance().getService(IProjectRepository.class);
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public void addProject(Project project) {
        projectRepository.save(project);
    }

    public void removeProject(Project project) {
        projectRepository.delete(project);
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