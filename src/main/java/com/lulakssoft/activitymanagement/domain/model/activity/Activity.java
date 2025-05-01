package com.lulakssoft.activitymanagement.domain.model.activity;

import java.time.LocalDate;
import java.util.UUID;

public class Activity {
    private final String id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;
    private Priority priority;
    private final String creatorId;
    private final String projectId;

    public Activity(String projectId, String creatorId, String title, String description,
                    Priority priority, LocalDate dueDate) {
        this.id = UUID.randomUUID().toString();
        this.projectId = projectId;
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = false;
    }

    // Konstruktor für Repository
    public Activity(String id, String projectId, String creatorId, String title,
             String description, Priority priority, LocalDate dueDate, boolean completed) {
        this.id = id;
        this.projectId = projectId;
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public void markAsComplete() {
        this.completed = true;
    }

    public void markAsIncomplete() {
        this.completed = false;
    }

    // Getter
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isCompleted() { return completed; }
    public Priority getPriority() { return priority; }
    public String getCreatorId() { return creatorId; }
    public String getProjectId() { return projectId; }

    // Setter
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", completed=" + completed +
                ", priority=" + priority +
                ", creatorId='" + creatorId + '\'' +
                ", projectId='" + projectId + '\'' +
                '}';
    }
}