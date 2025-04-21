package com.lulakssoft.activitymanagement.domain.repositories;

import com.lulakssoft.activitymanagement.domain.entities.activity.Activity;

import java.util.List;

public interface IActivityRepository {
    Activity save(Activity activity);
    void delete(Activity activity);
    List<Activity> findByProjectId(String projectId);
}