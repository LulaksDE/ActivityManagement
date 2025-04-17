package com.lulakssoft.activitymanagement.notification;

public class LoggerFactory {

    private static final LoggerNotifier INSTANCE = new LoggingService();

    private LoggerFactory() {}

    public static LoggerNotifier getLogger() {
        return INSTANCE;
    }
}