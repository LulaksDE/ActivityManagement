package com.lulakssoft.activitymanagement.domain.model.user;

public class AdminRole implements Role {
    @Override public String getName() { return "Admin"; }
    @Override public boolean canCreateProject() { return true; }
    @Override public boolean canEditProject(String projectCreatorId, String userId) { return true; }
    @Override public boolean canCreateActivity(boolean isProjectMember) { return true; }
    @Override public boolean canEditActivity(String activityCreatorId, String userId) { return true; }
    @Override public boolean canManageUsers() { return true; }
    @Override public boolean canViewHistory() { return true; }

    @Override
    public RoleType getType() {
        return RoleType.ADMIN;
    }
}