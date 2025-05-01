package com.lulakssoft.activitymanagement.config;

import com.lulakssoft.activitymanagement.domain.repository.*;
import com.lulakssoft.activitymanagement.application.service.*;
import com.lulakssoft.activitymanagement.infrastructure.persistence.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class ApplicationContext {
    private static ApplicationContext instance;

    // Repositories
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ActivityRepository activityRepository;

    // Services
    private final UserService userService;
    private final ProjectService projectService;
    private final ActivityService activityService;

    private ApplicationContext() {
        // Repository-Instanzen erzeugen
        userRepository = new JdbcUserRepository();
        projectRepository = new JdbcProjectRepository();
        activityRepository = new JdbcActivityRepository();

        // Service-Instanzen mit Repositories injizieren
        userService = new UserService(userRepository);
        projectService = new ProjectService(projectRepository);
        activityService = new ActivityService(activityRepository);
    }

    public static synchronized ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    // Getter für Services
    public UserService getUserService() { return userService; }
    public ProjectService getProjectService() { return projectService; }
    public ActivityService getActivityService() { return activityService; }
}