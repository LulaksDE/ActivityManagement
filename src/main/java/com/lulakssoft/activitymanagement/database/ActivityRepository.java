package com.lulakssoft.activitymanagement.database;

import com.lulakssoft.activitymanagement.Activity;
import com.lulakssoft.activitymanagement.ProjectManager;
import com.lulakssoft.activitymanagement.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.user.User;
import com.lulakssoft.activitymanagement.user.UserManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActivityRepository implements Repository<Activity, String> {

    private final LoggerNotifier logger = LoggerFactory.getLogger();

    @Override
    public Optional<Activity> findById(String id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM activities WHERE id = ?")) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            logger.logError("Error finding activity by ID: " + id, e);
        }
        return Optional.empty();
    }

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
    public List<Activity> findAll() {
        List<Activity> activities = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM activities")) {

            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            logger.logError("Error loading all activities: " + e.getMessage(), e);
        }

        return activities;
    }

    @Override
    public Activity save(Activity activity) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            if (existsById(activity.getId())) {
                updateActivity(conn, activity);
            } else {
                insertActivity(conn, activity);
            }

            updateActivityMembers(conn, activity);

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
            return activity;
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

    private void insertActivity(Connection conn, Activity activity) throws SQLException {
        String sql = "INSERT INTO activities (id, title, description, due_date, completed, priority, " +
                "project_id, creator_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
            stmt.setString(7, ProjectManager.getInstance().getCurrentProject().getId());
            stmt.setString(8, activity.getCreator().getId());

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

    private void updateActivityMembers(Connection conn, Activity activity) throws SQLException {
        String deleteSql = "DELETE FROM activity_members WHERE activity_id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setString(1, activity.getId());
            deleteStmt.executeUpdate();
        }

        String insertSql = "INSERT INTO activity_members (activity_id, user_id) VALUES (?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            for (User member : activity.getUserList()) {
                insertStmt.setString(1, activity.getId());
                insertStmt.setString(2, member.getId());
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            logger.logError("Error inserting activity members: " + e.getMessage(), e);
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

    @Override
    public boolean existsById(String id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM activities WHERE id = ?")) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            logger.logError("Error checking if activity exists by ID: " + id, e);
            return false;
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

        User creator = UserManager.INSTANCE.findUserById(creatorId)
                .orElseThrow(() -> new SQLException("Creator with ID " + creatorId + " not found."));

        List<User> members = getActivityMembers(id);

        Activity activity = new Activity(creator, members, title, description, dueDate, completed);
        activity.setPriority(priority);

        return activity;
    }

    private List<User> getActivityMembers(String activityId) {
        List<User> members = new ArrayList<>();

        String sql = "SELECT u.* FROM users u " +
                "JOIN activity_members am ON u.id = am.user_id " +
                "WHERE am.activity_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, activityId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UserRepository userRepo = new UserRepository();
                String userId = rs.getString("id");
                userRepo.findById(userId).ifPresent(members::add);
            }
        } catch (SQLException e) {
            logger.logError("Error finding activity members: " + e.getMessage(), e);
        }

        return members;
    }
}