package com.lulakssoft.activitymanagement.user;

import com.lulakssoft.activitymanagement.database.UserRepository;
import com.lulakssoft.activitymanagement.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.user.role.RoleFactory;

import java.util.List;
import java.util.Optional;

public enum UserManager {
    INSTANCE;

    private final UserRepository userRepository;
    private User currentUser;
    private final LoggerNotifier logger = LoggerFactory.getLogger();

    UserManager() {
        userRepository = new UserRepository();
        try {
            initializeDefaultUsers();
            logger.logInfo("UserManager initialized successfully");
        } catch (Exception e) {
            logger.logError("Failed to initialize UserManager", e);
        }
    }

    private void initializeDefaultUsers() {
        try {
            List<User> existingUsers = userRepository.findAll();
            if (existingUsers != null && existingUsers.isEmpty()) {
                logger.logInfo("Creating default users");
                userRepository.save(new User("admin01", "admin", RoleFactory.getRole(Privileges.ADMIN)));
                userRepository.save(new User("employee01", "admin", RoleFactory.getRole(Privileges.WORKER)));
            }
        } catch (Exception e) {
            logger.logError("Error initializing default users", e);
        }
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void removeUser(User user) {
        userRepository.delete(user);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public Optional<User> findUserById(String id) {
        return userRepository.findById(id);
    }

    public void logout() {
        this.currentUser = null;
    }
}