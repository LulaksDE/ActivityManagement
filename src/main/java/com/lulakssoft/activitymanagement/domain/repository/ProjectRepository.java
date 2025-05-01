package com.lulakssoft.activitymanagement.domain.repository;

import com.lulakssoft.activitymanagement.domain.model.project.Project;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    void save(Project project);
    void delete(String projectId);
    Project findById(String id);
    List<Project> findAll();
    List<Project> findByMember(String userId);
}