package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.domain.model.project.Project;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    @Test
    void shouldCreateProjectWithCreatorAsMember() {
        // Given
        String creatorId = "creator123";
        Set<String> initialMembers = new HashSet<>();

        // When
        Project project = new Project("Test Project", creatorId, initialMembers);

        // Then
        assertTrue(project.getMemberIds().contains(creatorId));
        assertEquals(1, project.getMemberIds().size());
    }

    @Test
    void shouldAddMemberToProject() {
        // Given
        String creatorId = "creator123";
        Set<String> initialMembers = new HashSet<>();
        Project project = new Project("Test Project", creatorId, initialMembers);
        String newMemberId = "member123";

        // When
        project.addMember(newMemberId);

        // Then
        assertTrue(project.getMemberIds().contains(newMemberId));
        assertEquals(2, project.getMemberIds().size());
    }

    @Test
    void shouldNotRemoveCreatorFromMembers() {
        // Given
        String creatorId = "creator123";
        Set<String> initialMembers = new HashSet<>();
        Project project = new Project("Test Project", creatorId, initialMembers);

        // When
        project.removeMember(creatorId);

        // Then
        assertTrue(project.getMemberIds().contains(creatorId));
        assertEquals(1, project.getMemberIds().size());
    }
}