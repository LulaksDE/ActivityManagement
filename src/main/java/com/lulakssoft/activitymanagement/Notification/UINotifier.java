package com.lulakssoft.activitymanagement.Notification;


public interface UINotifier extends NotificationSender {
    void showPopupNotification(String message, String title);
    void showBannerNotification(String message);
}
