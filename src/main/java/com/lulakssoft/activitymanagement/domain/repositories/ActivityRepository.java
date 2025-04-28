package com.lulakssoft.activitymanagement.domain.repositories;

import com.lulakssoft.activitymanagement.domain.entities.activity.Activity;
import com.lulakssoft.activitymanagement.domain.entities.proejct.ProjectManager;
import com.lulakssoft.activitymanagement.database.DatabaseConnection;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.operation.ActivityRepositoryException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActivityRepository implements IActivityRepository {

    private final LoggerNotifier logger = LoggerFactory.getLogger();

    @Override
    public List<Activity> findByProjectId(String projectId) {
        List<Activity> activities = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM activities WHERE project_id = ?")) {

            stmt.setString(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            logger.logError("Error finding activities by project ID: " + projectId, e);
        }

        return activities;
    }

    @Override
    public Activity save(Activity activity) throws ActivityRepositoryException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            if (existsById(activity.getId())) {
                updateActivity(conn, activity);
            } else {
                insertActivity(conn, activity);
            }

            conn.commit();
            return activity;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                logger.logError("Error rolling back transaction: " + rollbackEx.getMessage(), rollbackEx);
            }
            logger.logError("Error saving activity: " + activity.getId(), e);
            throw new ActivityRepositoryException("Error saving activity: " + e.getMessage(), e);
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                logger.logError("Error closing connection: " + closeEx.getMessage(), closeEx);
            }
        }
    }

    private boolean existsById(String id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM activities WHERE id = ?")) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.logError("Error checking existence of activity by ID: " + id, e);
        }
        return false;
    }

    private void insertActivity(Connection conn, Activity activity) throws SQLException {
        String sql = "INSERT INTO activities (id, title, description, due_date, completed, priority, " +
                "project_id, creator_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        ProjectManager projectManager = ProjectManager.INSTANCE;
        String projectId = projectManager.getCurrentProject().getId();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, activity.getId());
            stmt.setString(2, activity.getTitle());
            stmt.setString(3, activity.getDescription());

            if (activity.getDueDate() != null) {
                stmt.setDate(4, Date.valueOf(activity.getDueDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setBoolean(5, activity.isCompleted());
            stmt.setString(6, activity.getPriority());
            stmt.setString(7, projectId);
            stmt.setString(8, activity.getCreator());

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.logError("Error inserting activity: " + e.getMessage(), e);
        }
    }

    private void updateActivity(Connection conn, Activity activity) throws SQLException {
        String sql = "UPDATE activities SET title = ?, description = ?, due_date = ?, " +
                "completed = ?, priority = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, activity.getTitle());
            stmt.setString(2, activity.getDescription());

            if (activity.getDueDate() != null) {
                stmt.setDate(3, Date.valueOf(activity.getDueDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            stmt.setBoolean(4, activity.isCompleted());
            stmt.setString(5, activity.getPriority());
            stmt.setString(6, activity.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.logError("Error updating activity: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Activity activity) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM activities WHERE id = ?")) {

            stmt.setString(1, activity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.logError("Error deleting activity: " + e.getMessage(), e);
        }
    }

    private Activity mapResultSetToActivity(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String title = rs.getString("title");
        String description = rs.getString("description");

        LocalDate dueDate = null;
        Date dueDateSql = rs.getDate("due_date");
        if (dueDateSql != null) {
            dueDate = dueDateSql.toLocalDate();
        }

        boolean completed = rs.getBoolean("completed");
        String priority = rs.getString("priority");
        String creatorId = rs.getString("creator_id");

        Activity activity = new Activity(creatorId, title, description, priority,dueDate, completed);
        activity.setId(id);

        return activity;
    }
}