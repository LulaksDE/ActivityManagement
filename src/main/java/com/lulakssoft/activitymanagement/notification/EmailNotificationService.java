package com.lulakssoft.activitymanagement.notification;

public class EmailNotificationService implements EmailNotifier{
    @Override
    public void sendNotification(String message, String receiver) {
        // Implementation for sending a notification
        System.out.println("Sending notification to " + receiver + ": " + message);
        // Implementation without recursion
    }

    @Override
    public void sendEmailNotification(String subject, String message, String receiverEmail) {
        // Implementation for sending an email notification
        System.out.println("Sending email to " + receiverEmail + ": " + subject + "\n" + message);
        // Implementation without recursion
    }
}
