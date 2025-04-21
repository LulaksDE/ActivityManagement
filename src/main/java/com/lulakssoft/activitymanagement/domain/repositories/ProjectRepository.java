package com.lulakssoft.activitymanagement.domain.repositories;

import com.lulakssoft.activitymanagement.database.DatabaseConnection;
import com.lulakssoft.activitymanagement.domain.entities.activity.Activity;
import com.lulakssoft.activitymanagement.domain.entities.proejct.Project;
import com.lulakssoft.activitymanagement.ServiceLocator;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.domain.entities.user.User;

import java.sql.*;
import java.util.*;

public class ProjectRepository implements IProjectRepository {

    private final LoggerNotifier logger = LoggerFactory.getLogger();

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

        // Use of DISTINCT to avoid duplicates
        String sql = "SELECT DISTINCT p.* FROM projects p " +
                "LEFT JOIN project_members pm ON p.id = pm.project_id " +
                "WHERE pm.user_id = ? OR p.creator_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, userId);
            ResultSet rs = stmt.executeQuery();

            // Set for already added project IDs
            Set<String> addedProjectIds = new HashSet<>();

            while (rs.next()) {
                String projectId = rs.getString("id");
                if (!addedProjectIds.contains(projectId)) {
                    addedProjectIds.add(projectId);
                    projects.add(mapResultSetToProject(rs));
                }
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
            stmt.setString(3, project.getCreator());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.logError("Error inserting project: " + e.getMessage(), e);
        }
    }

    private void updateProject(Connection conn, Project project) throws SQLException {
        String sql = "UPDATE projects SET name = ?, creator_id = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, project.getName());
            stmt.setString(2, project.getCreator());
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
            for (String memberId : project.getMembers()) {
                insertStmt.setString(1, project.getId());
                insertStmt.setString(2, memberId);
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

        List<String> members = getProjectMembers(id);

        Project project = new Project(name, creatorId, members, ServiceLocator.getInstance().getService(IActivityRepository.class));
        project.setId(id);

        ActivityRepository activityRepository = new ActivityRepository();
        for (Activity activity : activityRepository.findByProjectId(id)) {
            project.addActivity(activity);
        }

        return project;
    }

    private List<String> getProjectMembers(String projectId) {
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

        // Convert User objects to their IDs
        List<String> memberIds = new ArrayList<>();
        for (User member : members) {
            memberIds.add(member.getId());
        }

        return memberIds;
    }
}