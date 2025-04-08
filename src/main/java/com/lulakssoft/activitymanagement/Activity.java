package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.User.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Activity {
    private final String id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;
    private String priority;
    private List<User> userList = new ArrayList<>();

    public Activity(User creator, String title, String description, LocalDate dueDate, boolean completed) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
        this.userList.add(creator);
    }

    // Getter and Setter
    public String getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getPriority() { return priority; } // Getter für Priorität
    public void setPriority(String priority) { this.priority = priority; } // Setter für Priorität

    public List<User> getUserList() { return userList; }
    public void setUserList(List<User> userList) { this.userList = userList; }

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", completed=" + completed +
                ", priority='" + priority + '\'' + // Added priority to toString
                '}';
    }
}
