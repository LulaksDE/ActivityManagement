package com.lulakssoft.activitymanagement;

public class Worker extends User{
    public Worker(String username) {
        super(username, "admin");
        this.addProject(new Project("Worker Project", this));
    }
}
