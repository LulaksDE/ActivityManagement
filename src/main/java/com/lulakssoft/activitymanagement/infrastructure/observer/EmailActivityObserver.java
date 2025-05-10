package com.lulakssoft.activitymanagement.infrastructure.observer;

import com.lulakssoft.activitymanagement.adapter.notification.EmailNotifier;
import com.lulakssoft.activitymanagement.domain.model.activity.Activity;
import com.lulakssoft.activitymanagement.domain.observer.ActivityObserver;
import com.lulakssoft.activitymanagement.application.service.EmailNotificationService;

public class EmailActivityObserver implements ActivityObserver {

    private final EmailNotifier emailNotifier;

    public EmailActivityObserver() {
        this.emailNotifier = new EmailNotificationService();
    }

    @Override
    public void onActivityCreated(Activity activity) {
        if (activity.getCreatorId() != null) {
            emailNotifier.sendEmailNotification(
                    "New Activity Created",
                    "Activity '" + activity.getTitle() + "' has been created.",
                    activity.getCreatorId() + "@example.com"
            );
        }
    }

    @Override
    public void onActivityUpdated(Activity activity) {
        if (activity.getCreatorId() != null) {
            emailNotifier.sendEmailNotification(
                    "Activity Updated",
                    "Activity '" + activity.getTitle() + "' has been updated.",
                    activity.getCreatorId() + "@example.com"
            );
        }
    }

    @Override
    public void onActivityCompleted(Activity activity) {
        if (activity.getCreatorId() != null) {
            emailNotifier.sendEmailNotification(
                    "Activity Completed",
                    "Activity '" + activity.getTitle() + "' has been marked as completed.",
                    activity.getCreatorId() + "@example.com"
            );
        }
    }

    @Override
    public void onActivityDeleted(Activity activity) {
        if (activity.getCreatorId() != null) {
            emailNotifier.sendEmailNotification(
                    "Activity Deleted",
                    "Activity '" + activity.getTitle() + "' has been deleted.",
                    activity.getCreatorId() + "@example.com"
            );
        }
    }
}