package com.lulakssoft.activitymanagement.infrastructure.persistence;

import com.lulakssoft.activitymanagement.database.DatabaseConnection;
import com.lulakssoft.activitymanagement.domain.model.activity.Activity;
import com.lulakssoft.activitymanagement.domain.model.activity.Priority;
import com.lulakssoft.activitymanagement.domain.repository.ActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcActivityRepository implements ActivityRepository {
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(JdbcActivityRepository.class);

    public JdbcActivityRepository() {
        this.dataSource = DatabaseConnection.getDataSource();
    }

    @Override
    public Activity save(Activity activity) {
        if (activity.getId() == null) {
            logger.info("Saving activity: {}", activity);
            return insertActivity(activity);
        } else {
            logger.info("Updating activity: {}", activity);
            return updateActivity(activity);
        }
    }

    private Activity insertActivity(Activity activity) {
        String sql = "INSERT INTO activities (id, project_id, creator_id, title, description, priority, due_date, completed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, activity.getId());
            stmt.setString(2, activity.getProjectId());
            stmt.setString(3, activity.getCreatorId());
            stmt.setString(4, activity.getTitle());
            stmt.setString(5, activity.getDescription());
            stmt.setString(6, activity.getPriority().name());
            stmt.setDate(7, activity.getDueDate() != null ? Date.valueOf(activity.getDueDate()) : null);
            stmt.setBoolean(8, activity.isCompleted());

            stmt.executeUpdate();

            return new Activity(
                    activity.getId(),
                    activity.getProjectId(),
                    activity.getCreatorId(),
                    activity.getTitle(),
                    activity.getDescription(),
                    activity.getPriority(),
                    activity.getDueDate(),
                    activity.isCompleted()
            );
        } catch (SQLException e) {
            logger.error("Error inserting activity", e);
            throw new RuntimeException("Database error", e);
        }
    }

    private Activity updateActivity(Activity activity) {
        String sql = "UPDATE activities SET project_id=?, creator_id=?, title=?, description=?, " +
                "priority=?, due_date=?, completed=? WHERE id=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, activity.getProjectId());
            stmt.setString(2, activity.getCreatorId());
            stmt.setString(3, activity.getTitle());
            stmt.setString(4, activity.getDescription());
            stmt.setString(5, activity.getPriority().name());
            stmt.setDate(6, activity.getDueDate() != null ? Date.valueOf(activity.getDueDate()) : null);
            stmt.setBoolean(7, activity.isCompleted());
            stmt.setString(8, activity.getId());

            stmt.executeUpdate();

            return activity;
        } catch (SQLException e) {
            logger.error("Error updating activity", e);
            throw new RuntimeException("Database error", e);
        }
    }

    @Override
    public void delete(String activityId) {
        String sql = "DELETE FROM activities WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, activityId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting activity", e);
            throw new RuntimeException("Database error", e);
        }
    }

    @Override
    public Optional<Activity> findById(String id) {
        String sql = "SELECT * FROM activities WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding activity by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Activity> findByProjectId(String projectId) {
        String sql = "SELECT * FROM activities WHERE project_id = ?";
        List<Activity> activities = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, projectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
            return activities;
        } catch (SQLException e) {
            logger.error("Error finding activities by project ID", e);
        }
        return List.of();
    }

    @Override
    public List<Activity> findByCreator(String creatorId) {
        String sql = "SELECT * FROM activities WHERE creator_id = ?";
        List<Activity> activities = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, creatorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
            return activities;
        } catch (SQLException e) {
            logger.error("Error finding activities by creator ID", e);
        }
        return List.of();
    }

    private Activity mapResultSetToActivity(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String projectId = rs.getString("project_id");
        String creatorId = rs.getString("creator_id");
        String title = rs.getString("title");
        String description = rs.getString("description");
        Priority priority = Priority.fromString(rs.getString("priority"));
        Date dueDate = rs.getDate("due_date");
        LocalDate localDueDate = dueDate != null ? dueDate.toLocalDate() : null;
        boolean completed = rs.getBoolean("completed");

        return new Activity(
                id,
                projectId,
                creatorId,
                title,
                description,
                priority,
                localDueDate,
                completed
        );
    }
}