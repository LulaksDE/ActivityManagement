package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.database.ActivityRepository;

public class ActivityManager {
    private static final ActivityManager INSTANCE = new ActivityManager();
    private final ActivityRepository activityRepository;
    private Activity currentEditingActivity;

    private ActivityManager() {
        activityRepository = new ActivityRepository();
    }

    public static ActivityManager getInstance() {
        return INSTANCE;
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