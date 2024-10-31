package com.lulakssoft.activitymanagement;

public class Supporter extends User{
    public Supporter(String username) {
        super(username);
        this.addProject(new Project("Supporter Project", this));
    }
}
