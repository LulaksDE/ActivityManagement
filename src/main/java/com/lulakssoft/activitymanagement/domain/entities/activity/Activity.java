package com.lulakssoft.activitymanagement.domain.entities.activity;

import com.lulakssoft.activitymanagement.domain.entities.user.User;

import java.time.LocalDate;
import java.util.UUID;

public class Activity {
    private String id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;
    private String priority;
    private final User creator;

    public Activity(User creator, String title, String description, String priority,LocalDate dueDate, boolean completed) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = completed;
        this.creator = creator;
    }

    // Getter and Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public User getCreator(){
        return creator;
    }

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
