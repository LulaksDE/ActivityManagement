package com.lulakssoft.activitymanagement.database;

import com.lulakssoft.activitymanagement.Activity;
import com.lulakssoft.activitymanagement.Project;
import com.lulakssoft.activitymanagement.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.user.User;
import com.lulakssoft.activitymanagement.user.UserManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectRepository implements Repository<Project, String> {

    private final LoggerNotifier logger = LoggerFactory.getLogger();

    @Override
    public Optional<Project> findById(String id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM projects WHERE id = ?")) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToProject(rs));
            }
        } catch (SQLException e) {
            logger.logError("Error finding project by ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM projects")) {

            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
        } catch (SQLException e) {
            logger.logError("Error finding all projects", e);
        }

        return projects;
    }

    public List<Project> findProjectsByUser(String userId) {
        List<Project> projects = new ArrayList<>();

        String sql = "SELECT p.* FROM projects p " +
                "JOIN project_members pm ON p.id = pm.project_id " +
                "WHERE pm.user_id = ? OR p.creator_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
        } catch (SQLException e) {
            logger.logError("Error finding projects by user ID: " + userId, e);
        }

        return projects;
    }

    @Override
    public Project save(Project project) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            if (existsById(project.getId())) {
                updateProject(conn, project);
            } else {
                insertProject(conn, project);
            }

            updateProjectMembers(conn, project);

            conn.commit();
            return project;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                logger.logError("Error rolling back transaction", rollbackEx);
            }
            logger.logError("Error saving project: " + e.getMessage(), e);
            return project;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                logger.logError("Error closing connection", closeEx);
            }
        }
    }

    private void insertProject(Connection conn, Project project) throws SQLException {
        String sql = "INSERT INTO projects (id, name, creator_id) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, project.getId());
            stmt.setString(2, project.getName());
            stmt.setString(3, project.getCreator().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.logError("Error inserting project: " + e.getMessage(), e);
        }
    }

    private void updateProject(Connection conn, Project project) throws SQLException {
        String sql = "UPDATE projects SET name = ?, creator_id = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, project.getName());
            stmt.setString(2, project.getCreator().getId());
            stmt.setString(3, project.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.logError("Error updating project: " + e.getMessage(), e);
        }
    }

    private void updateProjectMembers(Connection conn, Project project) throws SQLException {
        // Lösche alle bestehenden Mitglieder
        String deleteSql = "DELETE FROM project_members WHERE project_id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setString(1, project.getId());
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            logger.logError("Error deleting project members: " + e.getMessage(), e);
        }

        String insertSql = "INSERT INTO project_members (project_id, user_id) VALUES (?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            for (User member : project.getMembers()) {
                insertStmt.setString(1, project.getId());
                insertStmt.setString(2, member.getId());
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            logger.logError("Error inserting project members: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Project project) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM projects WHERE id = ?")) {

            stmt.setString(1, project.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.logError("Error deleting project: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsById(String id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM projects WHERE id = ?")) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            logger.logError("Error checking if project exists by ID: " + id, e);
            return false;
        }
    }

    private Project mapResultSetToProject(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String name = rs.getString("name");
        String creatorId = rs.getString("creator_id");

        User creator = UserManager.INSTANCE.findUserById(creatorId)
                .orElseThrow(() -> new SQLException("Creator with ID " + creatorId + " not found."));

        List<User> members = getProjectMembers(id);

        Project project = new Project(name, creator, members);
        project.setId(id);

        ActivityRepository activityRepository = new ActivityRepository();
        for (Activity activity : activityRepository.findByProjectId(id)) {
            project.addActivity(activity);
        }

        return project;
    }

    private List<User> getProjectMembers(String projectId) {
        List<User> members = new ArrayList<>();

        String sql = "SELECT u.* FROM users u " +
                "JOIN project_members pm ON u.id = pm.user_id " +
                "WHERE pm.project_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UserRepository userRepo = new UserRepository();
                String userId = rs.getString("id");
                userRepo.findById(userId).ifPresent(members::add);
            }
        } catch (SQLException e) {
            logger.logError("Error finding project members: " + e.getMessage(), e);
        }

        return members;
    }
}