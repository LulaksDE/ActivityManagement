package com.lulakssoft.activitymanagement.user.role;

import com.lulakssoft.activitymanagement.user.Privileges;
import com.lulakssoft.activitymanagement.user.UserRole;

import java.util.EnumMap;
import java.util.Map;

public class RoleFactory {
    private RoleFactory() {} // Prevent creation of instance for utility class
    private static final Map<Privileges, UserRole> ROLE_REGISTRY = new EnumMap<>(Privileges.class);

    static {
        registerRole(Privileges.ADMIN, new AdminRole());
        registerRole(Privileges.TECHNICIAN, new TechnicianRole());
        registerRole(Privileges.SUPPORTER, new SupporterRole());
        registerRole(Privileges.WORKER, new WorkerRole());
    }

    public static void registerRole(Privileges privilage, UserRole role) {
        ROLE_REGISTRY.put(privilage, role);
    }

    public static UserRole getRole(Privileges privilage) {
        UserRole role = ROLE_REGISTRY.get(privilage);
        if (role == null) {
            throw new IllegalArgumentException("Unknown privilege: " + privilage);
        }
        return role;
    }
}