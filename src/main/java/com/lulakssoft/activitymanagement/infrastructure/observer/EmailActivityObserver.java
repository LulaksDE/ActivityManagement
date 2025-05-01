package com.lulakssoft.activitymanagement.infrastructure.observer;

import com.lulakssoft.activitymanagement.adapter.notification.EmailNotifier;
import com.lulakssoft.activitymanagement.application.service.UserService;
import com.lulakssoft.activitymanagement.config.ApplicationContext;
import com.lulakssoft.activitymanagement.domain.model.activity.Activity;
import com.lulakssoft.activitymanagement.domain.model.user.User;
import com.lulakssoft.activitymanagement.domain.observer.ActivityObserver;
import com.lulakssoft.activitymanagement.domain.services.EmailNotificationService;

public class EmailActivityObserver implements ActivityObserver {

    private final EmailNotifier emailNotifier;
    private final UserService userService;

    public EmailActivityObserver() {
        this.emailNotifier = new EmailNotificationService();
        this.userService = ApplicationContext.getInstance().getUserService();
    }

    @Override
    public void onActivityCreated(Activity activity) {
        User creator = userService.findUserById(activity.getCreatorId());
        if (creator != null) {
            emailNotifier.sendEmailNotification(
                    "New Activity Created",
                    "Activity '" + activity.getTitle() + "' has been created.",
                    creator.getUsername() + "@example.com"
            );
        }
    }

    @Override
    public void onActivityUpdated(Activity activity) {
        User creator = userService.findUserById(activity.getCreatorId());
        if (creator != null) {
            emailNotifier.sendEmailNotification(
                    "Activity Updated",
                    "Activity '" + activity.getTitle() + "' has been updated.",
                    creator.getUsername() + "@example.com"
            );
        }
    }

    @Override
    public void onActivityCompleted(Activity activity) {
        User creator = userService.findUserById(activity.getCreatorId());
        if (creator != null) {
            emailNotifier.sendEmailNotification(
                    "Activity Completed",
                    "Activity '" + activity.getTitle() + "' has been marked as completed.",
                    creator.getUsername() + "@example.com"
            );
        }
    }

    @Override
    public void onActivityDeleted(Activity activity) {
        User creator = userService.findUserById(activity.getCreatorId());
        if (creator != null) {
            emailNotifier.sendEmailNotification(
                    "Activity Deleted",
                    "Activity '" + activity.getTitle() + "' has been deleted.",
                    creator.getUsername() + "@example.com"
            );
        }
    }
}