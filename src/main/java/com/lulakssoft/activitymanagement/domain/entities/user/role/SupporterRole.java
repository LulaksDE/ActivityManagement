package com.lulakssoft.activitymanagement.domain.entities.user.role;

import com.lulakssoft.activitymanagement.domain.entities.activity.Activity;
import com.lulakssoft.activitymanagement.domain.entities.proejct.Project;
import com.lulakssoft.activitymanagement.domain.entities.user.Privileges;
import com.lulakssoft.activitymanagement.domain.entities.user.User;
import com.lulakssoft.activitymanagement.domain.entities.user.UserRole;

public class SupporterRole implements UserRole {

    @Override
    public Privileges getPrivilege() {
        return Privileges.SUPPORTER;
    }

    @Override
    public boolean canCreateProject() {
        return false;
    }

    @Override
    public boolean canEditProject(Project project, String currentUser) {
        return false;
    }

    @Override
    public boolean canCreateActivity(Project project, String currentUser) {
        return true;
    }

    @Override
    public boolean canEditActivity(Activity activity, String currentUser) {
        return activity.getCreator().equals(currentUser);
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
        return "Supporter";
    }
}