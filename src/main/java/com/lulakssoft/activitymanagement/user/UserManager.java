package com.lulakssoft.activitymanagement.user;

import com.lulakssoft.activitymanagement.user.role.RoleFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum UserManager {
    INSTANCE;

    private final List<User> userList = new ArrayList<>();
    private User currentUser;

    UserManager() {
        userList.add(new User("admin01", "admin", RoleFactory.getRole(Privilages.ADMIN)));
        userList.add(new User("employee01", "admin", RoleFactory.getRole(Privilages.WORKER)));
        userList.add(new User("employee02", "admin", RoleFactory.getRole(Privilages.WORKER)));
        userList.add(new User("support01", "admin", RoleFactory.getRole(Privilages.SUPPORTER)));
        userList.add(new User("support02", "admin", RoleFactory.getRole(Privilages.SUPPORTER)));
        userList.add(new User("technician01", "admin", RoleFactory.getRole(Privilages.TECHNICIAN)));
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