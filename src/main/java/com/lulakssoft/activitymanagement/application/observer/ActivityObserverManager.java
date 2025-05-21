package com.lulakssoft.activitymanagement.application.observer;

import com.lulakssoft.activitymanagement.domain.model.activity.Activity;
import com.lulakssoft.activitymanagement.domain.observer.ActivityEvent;
import com.lulakssoft.activitymanagement.domain.observer.ActivityObserver;
import com.lulakssoft.activitymanagement.domain.observer.ActivitySubject;

import java.util.ArrayList;
import java.util.List;

public enum ActivityObserverManager implements ActivitySubject {
    INSTANCE;

    private final List<ActivityObserver> observers = new ArrayList<>();

    @Override
    public void registerObserver(ActivityObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(ActivityObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Activity activity, ActivityEvent eventType) {
        for (ActivityObserver observer : observers) {
            switch (eventType) {
                case CREATED:
                    observer.onActivityCreated(activity);
                    break;
                case UPDATED:
                    observer.onActivityUpdated(activity);
                    break;
                case COMPLETED:
                    observer.onActivityCompleted(activity);
                    break;
                case DELETED:
                    observer.onActivityDeleted(activity);
                    break;
            }
        }
    }
}