package com.lulakssoft.activitymanagement.user.role;

import com.lulakssoft.activitymanagement.Activity;
import com.lulakssoft.activitymanagement.Project;
import com.lulakssoft.activitymanagement.user.Privilages;
import com.lulakssoft.activitymanagement.user.User;
import com.lulakssoft.activitymanagement.user.UserRole;


public class AdminRole implements UserRole {
    
    @Override
    public Privilages getPrivilege() {
        return Privilages.ADMIN;
    }

    @Override
    public boolean canCreateProject() {
        return true;
    }

    @Override
    public boolean canEditProject(Project project, User currentUser) {
        return true;
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
        return true;
    }

    @Override
    public boolean canSeeHistory() {
        return true;
    }

    @Override
    public String getRoleName() {
        return "Administrator";
    }
}
