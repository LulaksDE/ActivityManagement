package com.lulakssoft.activitymanagement.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingService implements LoggerNotifier {

    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);

    @Override
    public void sendNotification(String message, String receiver) {
        logger.info("Notification an {}: {}", receiver, message);
    }

    @Override
    public void logInfo(String message) {
        logger.info(message);
    }

    @Override
    public void logWarning(String message) {
        logger.warn(message);
    }

    @Override
    public void logError(String message, Throwable throwable) {
        if (throwable != null) {
            logger.error(message, throwable);
        } else {
            logger.error(message);
        }
    }

    @Override
    public void logDebug(String message) {
        logger.debug(message);
    }
}