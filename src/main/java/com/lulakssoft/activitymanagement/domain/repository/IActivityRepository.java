package com.lulakssoft.activitymanagement.domain.repository;

import com.lulakssoft.activitymanagement.domain.model.activity.Activity;
import java.util.List;
import java.util.Optional;

public interface IActivityRepository {
    Activity save(Activity activity);
    void update(Activity activity);
    void delete(String activityId);
    Optional<Activity> findById(String id);
    List<Activity> findByProjectId(String projectId);
    List<Activity> findByCreator(String creatorId);
}