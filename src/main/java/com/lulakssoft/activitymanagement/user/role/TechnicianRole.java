package com.lulakssoft.activitymanagement.user.role;

import com.lulakssoft.activitymanagement.Activity;
import com.lulakssoft.activitymanagement.Project;
import com.lulakssoft.activitymanagement.user.Privileges;
import com.lulakssoft.activitymanagement.user.User;
import com.lulakssoft.activitymanagement.user.UserRole;

public class TechnicianRole implements UserRole {

    @Override
    public Privileges getPrivilege() {
        return Privileges.TECHNICIAN;
    }

    @Override
    public boolean canCreateProject() {
        return true;
    }

    @Override
    public boolean canEditProject(Project project, User currentUser) {
        return project.getCreator() == currentUser;
    }

    @Override
    public boolean canCreateActivity(Project project, User currentUser) {
        return true;
    }

    @Override
    public boolean canEditActivity(Activity activity, User currentUser) {
        return true;
    }

    @Override
    public boolean canManageUsers() {
        return false;
    }

    @Override
    public boolean canSeeHistory() {
        return true;
    }

    @Override
    public String getRoleName() {
        return "Technician";
    }
}