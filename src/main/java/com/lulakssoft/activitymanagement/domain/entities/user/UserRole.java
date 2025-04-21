package com.lulakssoft.activitymanagement.domain.entities.user;

import com.lulakssoft.activitymanagement.domain.entities.activity.Activity;
import com.lulakssoft.activitymanagement.domain.entities.proejct.Project;

/**
 * Interface for defining user roles according to the Open/Closed Principle.
 * Each user role implements this interface.
 */
public interface UserRole {
    /**
     * Returns the privilege level of the role.
     * @return The privilege level as Privilages enum.
     */
    Privileges getPrivilege();

    /**
     * Checks if the role is authorized to create a project.
     * @return true if authorized, false otherwise.
     */
    boolean canCreateProject();

    /**
     * Checks if the role is authorized to edit a project.
     * @param project The project to be edited.
     * @param currentUser The current user.
     * @return true if authorized, false otherwise.
     */
    boolean canEditProject(Project project, User currentUser);

    /**
     * Checks if the role is authorized to create an activity.
     * @param project The project to which the activity belongs.
     * @param currentUser The current user.
     * @return true if authorized, false otherwise.
     */
    boolean canCreateActivity(Project project, User currentUser);

    /**
     * Checks if the role is authorized to edit an activity.
     * @param activity The activity to be edited.
     * @param currentUser The current user.
     * @return true if authorized, false otherwise.
     */
    boolean canEditActivity(Activity activity, User currentUser);

    /**
     * Checks if the role is authorized to manage users.
     * @return true if authorized, false otherwise.
     */
    boolean canManageUsers();

    /**
     * Checks if the role is authorized to view the history.
     * @return true if authorized, false otherwise.
     */
    boolean canSeeHistory();

    /**
     * Returns the name of the role.
     * @return The name of the role as a string.
     */
    String getRoleName();
}
