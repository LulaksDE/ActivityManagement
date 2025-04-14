package com.lulakssoft.activitymanagement.user.role;

import com.lulakssoft.activitymanagement.user.Privilages;
import com.lulakssoft.activitymanagement.user.UserRole;

import java.util.EnumMap;
import java.util.Map;

public class RoleFactory {
    private RoleFactory() {} // Prevent creation of instance for utility class
    private static final Map<Privilages, UserRole> ROLE_REGISTRY = new EnumMap<>(Privilages.class);

    static {
        registerRole(Privilages.ADMIN, new AdminRole());
        registerRole(Privilages.TECHNICIAN, new TechnicianRole());
        registerRole(Privilages.SUPPORTER, new SupporterRole());
        registerRole(Privilages.WORKER, new WorkerRole());
    }

    public static void registerRole(Privilages privilage, UserRole role) {
        ROLE_REGISTRY.put(privilage, role);
    }

    public static UserRole getRole(Privilages privilage) {
        UserRole role = ROLE_REGISTRY.get(privilage);
        if (role == null) {
            throw new IllegalArgumentException("Unknown privilege: " + privilage);
        }
        return role;
    }
}