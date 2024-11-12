package com.lulakssoft.activitymanagement;

public class Admin extends User{
    public Admin(String username, String password) {
        super(username, password);
        this.addProject(new Project("Admin Project", this));
    }
}
