package com.lulakssoft.activitymanagement;

import java.util.ArrayList;
import java.util.List;

public class AppContext {
    private static AppContext instance;
    private List<User> userList = new ArrayList<>();
    private User currentUser;

    private AppContext() {}

    public static AppContext getInstance() {
        if (instance == null) instance = new AppContext();
        return instance;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}