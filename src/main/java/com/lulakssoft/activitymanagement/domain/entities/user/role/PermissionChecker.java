package com.lulakssoft.activitymanagement.domain.entities.user.role;

import com.lulakssoft.activitymanagement.domain.entities.user.User;
import com.lulakssoft.activitymanagement.domain.entities.user.UserRole;
import javafx.scene.control.Control;

import java.util.function.Predicate;

public class PermissionChecker {

    private PermissionChecker() {
        // Private constructor to prevent instantiation
    }

    public static void configureUIComponent(Control component, User user,
                                            Predicate<UserRole> permissionCheck) {
        boolean hasPermission = user != null && permissionCheck.test(user.getRole());
        component.setVisible(hasPermission);
        component.setManaged(hasPermission);
    }

    public static boolean checkPermission(UserRole role, Predicate<UserRole> permissionCheck) {
        return permissionCheck.test(role);
    }
}
