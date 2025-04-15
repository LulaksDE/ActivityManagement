package com.lulakssoft.activitymanagement;

public class ActivityManager {
    private static final ActivityManager INSTANCE = new ActivityManager();

    private Activity currentEditingActivity;

    private ActivityManager() {}

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
}