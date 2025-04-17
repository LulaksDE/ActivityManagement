package com.lulakssoft.activitymanagement.database;

import com.lulakssoft.activitymanagement.Activity;

import java.util.List;

public interface IActivityRepository {
    Activity save(Activity activity);
    void delete(Activity activity);
    List<Activity> findByProjectId(String projectId);
}