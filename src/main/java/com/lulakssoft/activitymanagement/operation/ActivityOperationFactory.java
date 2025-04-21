package com.lulakssoft.activitymanagement.operation;

import com.lulakssoft.activitymanagement.domain.entities.activity.Activity;
import com.lulakssoft.activitymanagement.domain.repositories.IActivityRepository;
import javafx.stage.Window;

public class ActivityOperationFactory {

    public static ActivityOperation createEditOperation(Activity activity, Window ownerWindow, IActivityRepository activityRepository) {
        return new EditActivityOperation(activity, ownerWindow, activityRepository);
    }

    public static ActivityOperation createNewOperation(Window ownerWindow, IActivityRepository repository) {
        return new CreateActivityOperation(ownerWindow, repository);
    }
}