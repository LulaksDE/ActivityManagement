package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.database.ActivityRepository;
import com.lulakssoft.activitymanagement.database.IActivityRepository;
import com.lulakssoft.activitymanagement.database.IProjectRepository;
import com.lulakssoft.activitymanagement.database.IUserRepository;
import com.lulakssoft.activitymanagement.database.ProjectRepository;
import com.lulakssoft.activitymanagement.database.UserRepository;
import com.lulakssoft.activitymanagement.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceLocator {
    private static final ServiceLocator INSTANCE = new ServiceLocator();
    private final Map<Class<?>, Object> services = new HashMap<>();
    private final LoggerNotifier logger = LoggerFactory.getLogger();

    private ServiceLocator() {
        registerService(IActivityRepository.class, new ActivityRepository());
        registerService(IProjectRepository.class, new ProjectRepository());
        registerService(IUserRepository.class, new UserRepository());
        logger.logInfo("ServiceLocator initialized with default services");
    }

    public static ServiceLocator getInstance() {
        return INSTANCE;
    }

    public <T> void registerService(Class<T> serviceType, T implementation) {
        services.put(serviceType, implementation);
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceType) {
        T service = (T) services.get(serviceType);
        if (service == null) {
            logger.logWarning("Service not found for type: " + serviceType.getName());
        }
        return service;
    }

    public static Project createProject(String name, User creator, List<User> userList) {
        return new Project(
                name,
                creator,
                userList,
                ServiceLocator.getInstance().getService(IActivityRepository.class)
        );
    }
}