package com.lulakssoft.activitymanagement.User;

import com.lulakssoft.activitymanagement.Project;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserManager {
    private static UserManager instance;
    private final List<User> userList = new ArrayList<>();
    private User currentUser;

    private UserManager() {
        // Standard-Benutzer erstellen
        userList.add(new Admin("admin01", "admin"));
        userList.add(new Worker("employee01"));
        userList.add(new Worker("employee02"));
        userList.add(new Supporter("support01"));
        userList.add(new Supporter("support02"));
        userList.add(new Technician("technician01"));
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public List<User> getAllUsers() {
        return userList;
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public void removeUser(User user) {
        userList.remove(user);
    }

    public Optional<User> findUserByUsername(String username) {
        return userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }
}