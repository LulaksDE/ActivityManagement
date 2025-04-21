package com.lulakssoft.activitymanagement.adapter.notification;

import com.lulakssoft.activitymanagement.domain.entities.activity.Activity;

public interface ActivityNotifier {
    void notifyActivityCreated(Activity activity);
    void notifyActivityUpdated(Activity activity);
    void notifyActivityDeleted(Activity activity);
}