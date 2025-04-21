package com.lulakssoft.activitymanagement.adapter.notification;

import com.lulakssoft.activitymanagement.domain.services.LoggingService;

public class LoggerFactory {

    private static final LoggerNotifier INSTANCE = new LoggingService();

    private LoggerFactory() {}

    public static LoggerNotifier getLogger() {
        return INSTANCE;
    }
}