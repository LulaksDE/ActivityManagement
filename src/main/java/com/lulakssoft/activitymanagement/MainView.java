package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.adapter.ui.LoginViewController;
import com.lulakssoft.activitymanagement.application.observer.ActivityObserverManager;
import com.lulakssoft.activitymanagement.application.service.*;
import com.lulakssoft.activitymanagement.database.DatabaseConnection;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.domain.repository.ActivityRepository;
import com.lulakssoft.activitymanagement.domain.repository.ProjectRepository;
import com.lulakssoft.activitymanagement.domain.repository.UserRepository;
import com.lulakssoft.activitymanagement.infrastructure.di.DependencyInjector;
import com.lulakssoft.activitymanagement.infrastructure.observer.EmailActivityObserver;
import com.lulakssoft.activitymanagement.infrastructure.persistence.JdbcActivityRepository;
import com.lulakssoft.activitymanagement.infrastructure.persistence.JdbcProjectRepository;
import com.lulakssoft.activitymanagement.infrastructure.persistence.JdbcUserRepository;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class MainView extends Application {

    private final LoggerNotifier logger = LoggerFactory.getLogger();


    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.logInfo("Starting application");

        initializeDependencies();

        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setMainScene(primaryStage, SceneManager.LOGIN_VIEW, "Activity Management System");

        primaryStage.show();

        setupObservers();

        primaryStage.setOnCloseRequest(event -> {
            logger.logInfo("Application is closing");
            DatabaseConnection.closeDataSource();
        });
    }

    private void initializeDependencies() {
        DependencyInjector injector = DependencyInjector.getInstance();

        UserRepository userRepository = new JdbcUserRepository();
        ProjectRepository projectRepository = new JdbcProjectRepository();
        ActivityRepository activityRepository = new JdbcActivityRepository();

        injector.register(UserRepository.class, userRepository);
        injector.register(ProjectRepository.class, projectRepository);
        injector.register(ActivityRepository.class, activityRepository);

        UserService userService = new UserService(userRepository);
        ProjectService projectService = new ProjectService(projectRepository);
        ActivityService activityService = new ActivityService(activityRepository);
        LoggingService loggingService = new LoggingService();
        EmailNotificationService emailService = new EmailNotificationService();

        injector.register(UserService.class, userService);
        injector.register(ProjectService.class, projectService);
        injector.register(ActivityService.class, activityService);
        injector.register(LoggingService.class, loggingService);
        injector.register(EmailNotificationService.class, emailService);
    }

    @Override
    public void stop() {
        logger.logInfo("Closing database connection to prevent memory leaks");
        DatabaseConnection.closeDataSource();
        Platform.exit();
    }

    private void setupObservers() {
        ActivityObserverManager manager = ActivityObserverManager.INSTANCE;
        manager.registerObserver(new EmailActivityObserver());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
