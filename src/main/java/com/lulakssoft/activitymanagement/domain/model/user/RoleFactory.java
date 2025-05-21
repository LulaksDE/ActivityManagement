package com.lulakssoft.activitymanagement.domain.model.user;

import java.util.Arrays;
import java.util.List;

public class RoleFactory {

    public static Role createRole(RoleType type) {
        return switch (type) {
            case ADMIN -> new AdminRole();
            case WORKER -> new WorkerRole();
            case TECHNICIAN -> new TechnicianRole();
        };
    }

    public static Role createFromString(String roleName) {
        try {
            RoleType type = RoleType.valueOf(roleName.toUpperCase());
            return createRole(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unbekannte Rolle: " + roleName);
        }
    }

    public static List<RoleType> getAllRoleTypes() {
        return Arrays.asList(RoleType.values());
    }
}