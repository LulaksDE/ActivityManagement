package com.lulakssoft.activitymanagement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Activity {
    private final String id;  // ID der Aktivität
    private String title;  // Titel der Aktivität
    private String description;  // Beschreibung der Aktivität
    private LocalDate dueDate;  // Fälligkeitsdatum der Aktivität
    private boolean completed;  // Status der Aktivität (erledigt oder nicht)
    private List<User> userList = new ArrayList<>();  // Liste der Benutzer, die an der Aktivität beteiligt sind

    public Activity(User creator, String title, String description, LocalDate dueDate, boolean completed) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = false;
        this.userList.add(creator);
    }

    // Getter und Setter
    public String getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

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
                '}';
    }
}
