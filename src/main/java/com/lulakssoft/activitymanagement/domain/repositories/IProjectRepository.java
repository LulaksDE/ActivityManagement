package com.lulakssoft.activitymanagement.domain.repositories;

import com.lulakssoft.activitymanagement.domain.entities.proejct.Project;

import java.util.List;

public interface IProjectRepository {
    Project save(Project project);
    void delete(Project project);
    List<Project> findAll();
    List<Project> findProjectsByUser(String userId);
}