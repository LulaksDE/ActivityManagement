package com.lulakssoft.activitymanagement.operation;

import com.lulakssoft.activitymanagement.ProjectManager;
import com.lulakssoft.activitymanagement.SceneManager;
import com.lulakssoft.activitymanagement.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.notification.LoggerNotifier;
import javafx.stage.Window;

public class CreateActivityOperation implements ActivityOperation {
    private final Window ownerWindow;
    private boolean successful = false;

    private final LoggerNotifier logger = LoggerFactory.getLogger();

    public CreateActivityOperation(Window ownerWindow) {
        this.ownerWindow = ownerWindow;
    }

    @Override
    public void execute() {
        try {
            SceneManager sceneManager = SceneManager.getInstance();
            sceneManager.openModalWindow(
                    ownerWindow,
                    SceneManager.ACTIVITY_CREATOR,
                    "Create New Activity");
            successful = true;
        } catch (Exception e) {
            logger.logError("Failed to open activity creation window", e);
            successful = false;
        }
    }

    @Override
    public boolean wasSuccessful() {
        return successful;
    }
}