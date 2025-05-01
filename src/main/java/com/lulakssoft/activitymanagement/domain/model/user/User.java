package com.lulakssoft.activitymanagement.domain.model.user;

import java.util.UUID;

public class User {
    private final String id;
    private String username;
    private String passwordHash;
    private Role role;

    public User(String username, String password, Role role) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.role = role;
    }

    // Konstruktor für Repository
    public User(String id, String username, String passwordHash, Role role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public boolean verifyPassword(String password) {
        return this.passwordHash.equals(hashPassword(password));
    }

    private String hashPassword(String password) {
        // In einer realen Anwendung würde hier ein sicherer Hash-Algorithmus verwendet werden
        return password;
    }

    // Getter
    public String getId() { return id; }
    public String getUsername() { return username; }
    public Role getRole() { return role; }
    public String getPasswordHash() { return passwordHash; }

    // Setter mit eingeschränktem Zugriff
    public void setUsername(String username) { this.username = username; }
    public void setRole(Role role) { this.role = role; }
    public void changePassword(String newPassword) { this.passwordHash = hashPassword(newPassword); }
}