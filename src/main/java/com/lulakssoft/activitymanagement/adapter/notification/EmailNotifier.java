package com.lulakssoft.activitymanagement.adapter.notification;

public interface EmailNotifier extends NotificationSender {
    void sendEmailNotification(String subject, String message, String receiverEmail);
}
