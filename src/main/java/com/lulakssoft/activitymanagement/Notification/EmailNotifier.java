package com.lulakssoft.activitymanagement.Notification;

public interface EmailNotifier extends NotificationSender {
    void sendEmailNotification(String subject, String message, String receiverEmail);
}
