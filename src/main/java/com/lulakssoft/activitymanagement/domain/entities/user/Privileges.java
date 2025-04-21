package com.lulakssoft.activitymanagement.domain.entities.user;

public enum Privileges {
    TECHNICIAN,
    SUPPORTER,
    ADMIN,
    WORKER;

    @Override
    public String toString() {
        // Return capitalized name (e.g., "Admin" instead of "ADMIN")
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
