package com.lulakssoft.activitymanagement.domain.observer;

import com.lulakssoft.activitymanagement.domain.model.activity.Activity;

public interface ActivityObserver {
    void onActivityCreated(Activity activity);
    void onActivityUpdated(Activity activity);
    void onActivityCompleted(Activity activity);
    void onActivityDeleted(Activity activity);
}