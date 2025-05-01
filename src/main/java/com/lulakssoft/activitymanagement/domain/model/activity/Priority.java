package com.lulakssoft.activitymanagement.domain.model.activity;

public enum Priority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Priority fromString(String text) {
        for (Priority priority : Priority.values()) {
            if (priority.displayName.equalsIgnoreCase(text)) {
                return priority;
            }
        }
        return MEDIUM; // Default
    }

    @Override
    public String toString() {
        return displayName;
    }
}