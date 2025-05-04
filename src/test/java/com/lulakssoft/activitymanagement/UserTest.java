package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.domain.model.user.Role;
import com.lulakssoft.activitymanagement.domain.model.user.RoleFactory;
import com.lulakssoft.activitymanagement.domain.model.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGetUsername() {
        // Given
        String username = "testUser";
        String password = "testPassword";
        Role role = RoleFactory.createFromString("Worker");

        // When
        User user = new User(username, password, role);

        // Then
        assertEquals(username, user.getUsername());
    }
}