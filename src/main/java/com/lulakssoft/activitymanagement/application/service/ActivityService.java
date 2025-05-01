package com.lulakssoft.activitymanagement.application.service;

import com.lulakssoft.activitymanagement.domain.model.activity.Activity;
import com.lulakssoft.activitymanagement.domain.repository.ActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ActivityService {
    private final ActivityRepository activityRepository;
    private Activity currentEditingActivity;
    private final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public void setCurrentEditingActivity(Activity activity) {
        this.currentEditingActivity = activity;
    }

    public Activity getCurrentEditingActivity() {
        return currentEditingActivity;
    }

    public void clearCurrentEditingActivity() {
        this.currentEditingActivity = null;
    }

    public Activity saveActivity(Activity activity) {
        try {
            return activityRepository.save(activity);
        } catch (Exception e) {
            logger.error("Error saving activity: ", e);
            throw e;
        }
    }

    public void deleteActivity(String activityId) {
        activityRepository.delete(activityId);
    }

    public List<Activity> findActivitiesByProject(String projectId) {
        return activityRepository.findByProjectId(projectId);
    }
}
