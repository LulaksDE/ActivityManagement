package com.lulakssoft.activitymanagement.User;

import com.lulakssoft.activitymanagement.Project;

public class Admin extends User{
    public Admin(String username, String password) {
        super(username, password, Privilages.ADMIN);
    }
}
