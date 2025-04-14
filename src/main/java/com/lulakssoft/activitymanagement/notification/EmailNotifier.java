package com.lulakssoft.activitymanagement.notification;

public interface EmailNotifier extends NotificationSender {
    void sendEmailNotification(String subject, String message, String receiverEmail);
}
