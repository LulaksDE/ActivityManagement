package com.lulakssoft.activitymanagement.notification;


public interface UINotifier extends NotificationSender {
    void showPopupNotification(String message, String title);
    void showBannerNotification(String message);
}
