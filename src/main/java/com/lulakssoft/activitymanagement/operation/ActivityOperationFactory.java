package com.lulakssoft.activitymanagement.operation;

import com.lulakssoft.activitymanagement.domain.entities.activity.Activity;
import javafx.stage.Window;

public class ActivityOperationFactory {

    public static ActivityOperation createEditOperation(Activity activity, Window ownerWindow) {
        return new EditActivityOperation(activity, ownerWindow);
    }

    public static ActivityOperation createNewOperation(Window ownerWindow) {
        return new CreateActivityOperation(ownerWindow);
    }
}