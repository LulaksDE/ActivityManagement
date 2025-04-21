package com.lulakssoft.activitymanagement.adapter.notification;


public interface UINotifier extends NotificationSender {
    void showPopupNotification(String message, String title);
    void showBannerNotification(String message);
}
