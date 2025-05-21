package com.lulakssoft.activitymanagement.application.service;

import com.lulakssoft.activitymanagement.application.observer.ActivityObserverManager;
import com.lulakssoft.activitymanagement.domain.model.activity.Activity;
import com.lulakssoft.activitymanagement.domain.observer.ActivityEvent;
import com.lulakssoft.activitymanagement.domain.repository.ActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ActivityService {
    private final ActivityRepository activityRepository;
    private Activity currentEditingActivity;
    private final ActivityObserverManager observerManager;
    private final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
        this.observerManager = ActivityObserverManager.INSTANCE;
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
            Activity savedActivity = activityRepository.save(activity);
            observerManager.notifyObservers(savedActivity, ActivityEvent.CREATED);
            return savedActivity;
        } catch (Exception e) {
            logger.error("Error saving activity: ", e);
            throw e;
        }
    }

    public void updateActivity(Activity activity) {
        try {
            activityRepository.update(activity);
            observerManager.notifyObservers(activity, ActivityEvent.UPDATED);
            if(activity.isCompleted()) {
                observerManager.notifyObservers(activity, ActivityEvent.COMPLETED);
            }
        } catch (Exception e) {
            logger.error("Error updating activity: ", e);
            throw e;
        }
    }

    public void deleteActivity(Activity activity) {
        activityRepository.delete(activity.getId());
        observerManager.notifyObservers(activity, ActivityEvent.DELETED);
    }

    public List<Activity> findActivitiesByProject(String projectId) {
        return activityRepository.findByProjectId(projectId);
    }
}
