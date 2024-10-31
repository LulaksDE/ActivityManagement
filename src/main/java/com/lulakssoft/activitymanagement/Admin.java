package com.lulakssoft.activitymanagement;

public class Admin extends User{
    public Admin(String username) {
        super(username);
        this.addProject(new Project("Admin Project", this));
    }
}
