package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Project {
    private String id;
    private String name;
    private final List<Activity> activityList;
    private User creator;
    private List<User> userList = new ArrayList<>();

    public Project(String name, User creator, List<User> userList) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.activityList = new ArrayList<>();
        this.creator = creator;
        this.userList = userList;
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public List<User> getMembers() {
        return userList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", creator=" + creator.getUsername() +
                " userList=" + userList.toString() +
                ", activityList=" + this.activityList.toString() +
                '}';
    }
}
