package com.lulakssoft.activitymanagement.User;

import com.lulakssoft.activitymanagement.Project;

public class Worker extends User{
    public Worker(String username) {
        super(username, "admin", Privilages.WORKER);
    }
}
