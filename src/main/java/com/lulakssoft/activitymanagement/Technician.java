package com.lulakssoft.activitymanagement;

public class Technician extends User{
    public Technician(String username) {
        super(username, "admin");
        this.addProject(new Project("Technician Project", this));
    }
}
