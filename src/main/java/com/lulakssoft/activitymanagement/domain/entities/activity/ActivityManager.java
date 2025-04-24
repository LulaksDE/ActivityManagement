package com.lulakssoft.activitymanagement.domain.entities.activity;

import com.lulakssoft.activitymanagement.ServiceLocator;
import com.lulakssoft.activitymanagement.domain.repositories.IActivityRepository;

public enum ActivityManager {
    INSTANCE;

    private final IActivityRepository activityRepository;
    private Activity currentEditingActivity;

    ActivityManager() {
        this.activityRepository = ServiceLocator.getInstance().getService(IActivityRepository.class);
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

    public void saveActivity(Activity activity) {
        activityRepository.save(activity);
    }

    public void deleteActivity(Activity activity) {
        activityRepository.delete(activity);
    }
}