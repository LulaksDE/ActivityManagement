package com.lulakssoft.activitymanagement.User;

import com.lulakssoft.activitymanagement.Project;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public abstract class User {
    private String username;  // Benutzername
    private String password;  // Passwort
    private final Privilages privilage;  // Added privilege field


    public User(String username, String password, Privilages privilage) {
        this.username = username;
        this.password = encodePassword(password);
        this.privilage = privilage;
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
        this.password = encodePassword(password);
    }

    public Privilages getPrivilage() {
        return privilage;
    }


    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", privilage=" + privilage +
                '}';
    }

    private String encodePassword(String password) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(password.getBytes());
    }
}