package com.lulakssoft.activitymanagement;

import com.lulakssoft.activitymanagement.database.DatabaseConnection;
import com.lulakssoft.activitymanagement.domain.model.activity.Activity;
import com.lulakssoft.activitymanagement.domain.model.activity.Priority;
import com.lulakssoft.activitymanagement.infrastructure.persistence.JdbcActivityRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class JdbcActivityRepositoryTest {

    @Mock
    private HikariDataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    private JdbcActivityRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Mock for DatabaseConnection.getDataSource()
        try (var mockedDatabaseConnection = mockStatic(DatabaseConnection.class)) {
            mockedDatabaseConnection.when(DatabaseConnection::getDataSource).thenReturn(dataSource);

            // DataSource returns Connection
            when(dataSource.getConnection()).thenReturn(connection);

            // Connection returns PreparedStatement
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

            // PreparedStatement returns ResultSet
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            repository = new JdbcActivityRepository();
        }
    }

    @Test
    void testSaveActivity() throws SQLException {
        // Given
        Activity activity = new Activity(
                "project123",
                "creator123",
                "Test Activity",
                "Description",
                Priority.HIGH,
                LocalDate.now()
        );

        // When
        Activity savedActivity = repository.save(activity);

        // Then
        verify(preparedStatement).setString(1, activity.getId());
        verify(preparedStatement).setString(2, activity.getProjectId());
        verify(preparedStatement).setString(3, activity.getCreatorId());
        verify(preparedStatement).setString(4, activity.getTitle());
        verify(preparedStatement).setString(5, activity.getDescription());
        verify(preparedStatement).setString(6, activity.getPriority().name());
        verify(preparedStatement).setDate(7, Date.valueOf(activity.getDueDate()));
        verify(preparedStatement).setBoolean(8, activity.isCompleted());
        verify(preparedStatement).executeUpdate();

        assertEquals(activity.getId(), savedActivity.getId());
        assertEquals(activity.getTitle(), savedActivity.getTitle());
    }

    @Test
    void testUpdateActivity() throws SQLException {
        // Given
        Activity activity = new Activity(
                "activity123",
                "project123",
                "creator123",
                "Updated Activity",
                "Updated Description",
                Priority.MEDIUM,
                LocalDate.now(),
                true
        );

        // When
        repository.update(activity);

        // Then
        verify(preparedStatement).setString(1, activity.getTitle());
        verify(preparedStatement).setString(2, activity.getDescription());
        verify(preparedStatement).setString(3, activity.getPriority().name());
        verify(preparedStatement).setDate(4, Date.valueOf(activity.getDueDate()));
        verify(preparedStatement).setBoolean(5, activity.isCompleted());
        verify(preparedStatement).setString(6, activity.getId());
        verify(preparedStatement).setString(7, activity.getProjectId());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testFindById() throws SQLException {
        // Given
        String activityId = "d27177a7-dbe0-496a-944c-ef626d17ba76";

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn(activityId);
        when(resultSet.getString("project_id")).thenReturn("project123");
        when(resultSet.getString("creator_id")).thenReturn("creator123");
        when(resultSet.getString("title")).thenReturn("Test Activity");
        when(resultSet.getString("description")).thenReturn("Description");
        when(resultSet.getString("priority")).thenReturn("HIGH");
        when(resultSet.getDate("due_date")).thenReturn(Date.valueOf(LocalDate.now()));
        when(resultSet.getBoolean("completed")).thenReturn(false);

        // When
        Optional<Activity> result = repository.findById(activityId);

        // Then
        verify(preparedStatement).setString(1, activityId);

        assertTrue(result.isPresent());
        assertEquals(activityId, result.get().getId());
        assertEquals("Test Activity", result.get().getTitle());
        assertEquals(Priority.HIGH, result.get().getPriority());
    }

    @Test
    void testFindByProjectId() throws SQLException {
        // Given
        String projectId = "project123";

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("id")).thenReturn("activity1", "activity2");
        when(resultSet.getString("project_id")).thenReturn(projectId, projectId);
        when(resultSet.getString("creator_id")).thenReturn("creator123", "creator123");
        when(resultSet.getString("title")).thenReturn("Activity 1", "Activity 2");
        when(resultSet.getString("description")).thenReturn("Description 1", "Description 2");
        when(resultSet.getString("priority")).thenReturn("HIGH", "MEDIUM");
        when(resultSet.getDate("due_date")).thenReturn(
                Date.valueOf(LocalDate.now()),
                Date.valueOf(LocalDate.now().plusDays(1))
        );
        when(resultSet.getBoolean("completed")).thenReturn(false, true);

        // When
        List<Activity> activities = repository.findByProjectId(projectId);

        // Then
        verify(preparedStatement).setString(1, projectId);

        assertEquals(2, activities.size());
        assertEquals("Activity 1", activities.get(0).getTitle());
        assertEquals("Activity 2", activities.get(1).getTitle());
    }

    @Test
    void testDeleteActivity() throws SQLException {
        // Given
        String activityId = "activity123";

        // When
        repository.delete(activityId);

        // Then
        verify(preparedStatement).setString(1, activityId);
        verify(preparedStatement).executeUpdate();
    }
}