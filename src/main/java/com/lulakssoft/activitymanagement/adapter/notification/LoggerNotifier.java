package com.lulakssoft.activitymanagement.adapter.notification;

public interface LoggerNotifier extends NotificationSender {
    void logInfo(String message);
    void logWarning(String message);
    void logError(String message, Throwable throwable);
    void logDebug(String message);
}