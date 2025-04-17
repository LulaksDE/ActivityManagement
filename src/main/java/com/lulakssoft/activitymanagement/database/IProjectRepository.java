package com.lulakssoft.activitymanagement.database;

import com.lulakssoft.activitymanagement.Project;

import java.util.List;

public interface IProjectRepository {
    Project save(Project project);
    void delete(Project project);
    List<Project> findAll();
    List<Project> findProjectsByUser(String userId);
}