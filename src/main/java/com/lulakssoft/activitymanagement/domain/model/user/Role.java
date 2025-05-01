package com.lulakssoft.activitymanagement.domain.model.user;

public interface Role {
    String getName();
    boolean canCreateProject();
    boolean canEditProject(String projectCreatorId, String userId);
    boolean canCreateActivity(boolean isProjectMember);
    boolean canEditActivity(String activityCreatorId, String userId);
    boolean canManageUsers();
    boolean canViewHistory();

    RoleType getType();
}