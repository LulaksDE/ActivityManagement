package com.lulakssoft.activitymanagement.domain.entities.activity;

import com.lulakssoft.activitymanagement.ServiceLocator;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.domain.repositories.IActivityRepository;
import com.lulakssoft.activitymanagement.operation.ActivityRepositoryException;

public enum ActivityManager {
    INSTANCE;

    private final IActivityRepository activityRepository;
    private Activity currentEditingActivity;
    private LoggerNotifier logger = LoggerFactory.getLogger();

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
        try{
        activityRepository.save(activity);
        }catch (ActivityRepositoryException e){
            logger.logError("Error saving activity: ", e);
        }
    }

    public void deleteActivity(Activity activity) {
        activityRepository.delete(activity);
    }
}