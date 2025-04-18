package com.lulakssoft.activitymanagement.operation;

import com.lulakssoft.activitymanagement.Activity;
import com.lulakssoft.activitymanagement.ActivityManager;
import com.lulakssoft.activitymanagement.SceneManager;
import com.lulakssoft.activitymanagement.database.ActivityRepository;
import com.lulakssoft.activitymanagement.database.IActivityRepository;
import com.lulakssoft.activitymanagement.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.notification.LoggerNotifier;
import javafx.stage.Window;

public class EditActivityOperation implements ActivityOperation {
    private final Activity activity;
    private final Window ownerWindow;
    private boolean successful = false;

    private final LoggerNotifier logger = LoggerFactory.getLogger();

    public EditActivityOperation(Activity activity, Window ownerWindow) {
        this.activity = activity;
        this.ownerWindow = ownerWindow;

        IActivityRepository activityRepository = new ActivityRepository();
        ActivityManager.initialize(activityRepository);
    }

    @Override
    public void execute() {
        try {
            ActivityManager.getInstance().setCurrentEditingActivity(activity);
            SceneManager sceneManager = SceneManager.getInstance();
            sceneManager.openModalWindow(
                    ownerWindow,
                    SceneManager.ACTIVITY_EDITOR,
                    "Edit Activity: " + activity.getTitle());
            successful = true;
        } catch (Exception e) {
            logger.logError("Failed to open activity editor window", e);
            successful = false;
        }
    }

    @Override
    public boolean wasSuccessful() {
        return successful;
    }
}