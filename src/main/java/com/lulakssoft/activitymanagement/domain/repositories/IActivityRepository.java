package com.lulakssoft.activitymanagement.domain.repositories;

import com.lulakssoft.activitymanagement.domain.entities.activity.Activity;
import com.lulakssoft.activitymanagement.operation.ActivityRepositoryException;

import java.util.List;

public interface IActivityRepository {
    Activity save(Activity activity) throws ActivityRepositoryException;
    void delete(Activity activity);
    List<Activity> findByProjectId(String projectId);
}