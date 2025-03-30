package com.lulakssoft.activitymanagement.User;

import com.lulakssoft.activitymanagement.Project;

public class Technician extends User{
    public Technician(String username) {
        super(username, "admin", Privilages.TECHNICIAN);
        this.addProject(new Project("Technician Project", this));
    }
}
