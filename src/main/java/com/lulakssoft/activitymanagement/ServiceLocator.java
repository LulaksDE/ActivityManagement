package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.domain.entities.proejct.Project;
import com.lulakssoft.activitymanagement.domain.repositories.ActivityRepository;
import com.lulakssoft.activitymanagement.domain.repositories.IActivityRepository;
import com.lulakssoft.activitymanagement.domain.repositories.IProjectRepository;
import com.lulakssoft.activitymanagement.domain.repositories.IUserRepository;
import com.lulakssoft.activitymanagement.domain.repositories.ProjectRepository;
import com.lulakssoft.activitymanagement.domain.repositories.UserRepository;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;

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

    public static Project createProject(String name, String creatorId, List<String> userIdList) {
        return new Project(
                name,
                creatorId,
                userIdList,
                ServiceLocator.getInstance().getService(IActivityRepository.class)
        );
    }
}