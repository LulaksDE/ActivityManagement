package com.lulakssoft.activitymanagement.notification;

import com.lulakssoft.activitymanagement.Activity;

public interface ActivityNotifier {
    void notifyActivityCreated(Activity activity);
    void notifyActivityUpdated(Activity activity);
    void notifyActivityDeleted(Activity activity);
}