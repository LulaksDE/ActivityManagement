package com.lulakssoft.activitymanagement.domain.observer;

import com.lulakssoft.activitymanagement.domain.model.activity.Activity;

public interface ActivitySubject {
    void registerObserver(ActivityObserver observer);
    void removeObserver(ActivityObserver observer);
    void notifyObservers(Activity activity, ActivityEvent eventType);
}