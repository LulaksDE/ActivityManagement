package com.lulakssoft.activitymanagement;

import java.time.LocalDate;

public class Activity {
    private String title;
    private String description;
    private LocalDate finalDate;
    private boolean done;

    public Activity(String title, String description, LocalDate finalDate) {
        this.title = title;
        this.description = description;
        this.finalDate = finalDate;
        this.done = false;
    }

    // Getter und Setter
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getFinalDate() { return finalDate; }
    public void setFinalDate(LocalDate finalDate) { this.finalDate = finalDate; }

    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }

    public String getTitel() {
        return title;
    }
}
