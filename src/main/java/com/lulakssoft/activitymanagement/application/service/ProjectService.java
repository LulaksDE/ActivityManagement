package com.lulakssoft.activitymanagement.application.service;

import com.lulakssoft.activitymanagement.domain.model.project.Project;
import com.lulakssoft.activitymanagement.domain.repository.IProjectRepository;

import java.util.List;

public class ProjectService {
    private final IProjectRepository projectRepository;
    private Project currentProject;

    public ProjectService(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    public void saveProject(Project project) {
        projectRepository.save(project);
    }
    public void deleteProject(String projectId) {
        projectRepository.delete(projectId);
    }
    public Project findProjectById(String projectId) {
        return projectRepository.findById(projectId);
    }
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }
    public List<Project> findProjectsByMember(String userId) {
        return projectRepository.findByMember(userId);
    }
    public void setCurrentProject(Project project) {
        this.currentProject = project;
    }
    public Project getCurrentProject() {
        return currentProject;
    }
}
