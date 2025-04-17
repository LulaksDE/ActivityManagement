package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.database.IActivityRepository;

public class ActivityManager {
    private static ActivityManager instance;
    private final IActivityRepository activityRepository;
    private Activity currentEditingActivity;

    private ActivityManager(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager(
                    ServiceLocator.getInstance().getService(IActivityRepository.class)
            );
        }
        return instance;
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