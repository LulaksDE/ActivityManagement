package com.lulakssoft.activitymanagement.domain.entities.user;

import java.util.UUID;

public class User {
    private String id;
    private String username;
    private String password;
    private UserRole role;

    public User(String username, String password, UserRole role) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Privileges getPrivilage() {
        return role.getPrivilege();
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                "username='" + username + '\'' +
                ", role=" + role.getRoleName() +
                '}';
    }
}