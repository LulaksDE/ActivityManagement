package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.domain.model.activity.Activity;
import com.lulakssoft.activitymanagement.domain.model.activity.Priority;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    @Test
    void testActivityCreation() {
        // Given
        String projectId = "ProjectId1";
        String creatorId = "CreatorId1";
        String title = "Activity 1";
        String description = "Description 2";
        Priority priority = Priority.HIGH;
        LocalDate dueDate = LocalDate.now().plusDays(5);

        // When
        Activity activity = new Activity(projectId, creatorId, title, description, priority, dueDate);

        // Then
        assertEquals(projectId, activity.getProjectId());
        assertEquals(creatorId, activity.getCreatorId());
        assertEquals(title, activity.getTitle());
        assertEquals(description, activity.getDescription());
        assertEquals(priority, activity.getPriority());
        assertEquals(dueDate, activity.getDueDate());
    }

    @Test
    void testActivityMarkAsCompleted() {
        // Given
        Activity activity = new Activity("ProjectId1", "CreatorId1", "Activity 1", "Description 2", Priority.HIGH, LocalDate.now().plusDays(5));

        // When
        activity.markAsComplete();

        // Then
        assertTrue(activity.isCompleted());
    }

    @Test
    void testActivityMarkAsIncomplete() {
        // Given
        Activity activity = new Activity("ProjectId1", "CreatorId1", "Activity 1", "Description 2", Priority.HIGH, LocalDate.now().plusDays(5));
        activity.markAsComplete(); // Mark as complete first

        // When
        activity.markAsIncomplete();

        // Then
        assertFalse(activity.isCompleted());
    }

    @Test
    void testActivityPrioritySetting() {
        // Given
        Activity activity = new Activity("ProjectId1", "CreatorId1", "Activity 1", "Description 2", Priority.HIGH, LocalDate.now().plusDays(5));

        // When
        activity.setPriority(Priority.MEDIUM);

        // Then
        assertEquals(Priority.MEDIUM, activity.getPriority());
    }
}
