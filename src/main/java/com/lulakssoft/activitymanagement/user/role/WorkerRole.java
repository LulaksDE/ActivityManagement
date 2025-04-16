package com.lulakssoft.activitymanagement.user.role;

import com.lulakssoft.activitymanagement.Activity;
import com.lulakssoft.activitymanagement.Project;
import com.lulakssoft.activitymanagement.user.Privileges;
import com.lulakssoft.activitymanagement.user.User;
import com.lulakssoft.activitymanagement.user.UserRole;

public class WorkerRole implements UserRole {

    @Override
    public Privileges getPrivilege() {
        return Privileges.WORKER;
    }

    @Override
    public boolean canCreateProject() {
        return false;
    }

    @Override
    public boolean canEditProject(Project project, User currentUser) {
        return false;
    }

    @Override
    public boolean canCreateActivity(Project project, User currentUser) {
        return project.getMembers().contains(currentUser);
    }

    @Override
    public boolean canEditActivity(Activity activity, User currentUser) {
        return activity.getCreator() == currentUser;
    }

    @Override
    public boolean canManageUsers() {
        return false;
    }

    @Override
    public boolean canSeeHistory() {
        return false;
    }

    @Override
    public String getRoleName() {
        return "Worker";
    }
}