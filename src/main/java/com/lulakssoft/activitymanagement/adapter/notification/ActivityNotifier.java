package com.lulakssoft.activitymanagement.adapter.notification;

import com.lulakssoft.activitymanagement.domain.model.activity.Activity;

public interface ActivityNotifier {
    void notifyActivityCreated(Activity activity);
    void notifyActivityUpdated(Activity activity);
    void notifyActivityDeleted(Activity activity);
}