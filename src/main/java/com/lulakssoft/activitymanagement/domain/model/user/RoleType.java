package com.lulakssoft.activitymanagement.domain.model.user;

public enum RoleType {
    ADMIN("Admin"),
    WORKER("Worker"),
    TECHNICIAN("Technician");

    private final String displayName;

    RoleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
