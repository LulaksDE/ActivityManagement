package com.lulakssoft.activitymanagement.User;

import com.lulakssoft.activitymanagement.Project;

public class Supporter extends User{
    public Supporter(String username) {
        super(username, "admin", Privilages.SUPPORTER);
    }
}
