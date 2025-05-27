package com.lulakssoft.activitymanagement.infrastructure.persistence;

import com.lulakssoft.activitymanagement.database.DatabaseConnection;
import com.lulakssoft.activitymanagement.domain.model.project.Project;
import com.lulakssoft.activitymanagement.domain.repository.IProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcProjectRepository implements IProjectRepository {
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(JdbcProjectRepository.class);

    public JdbcProjectRepository() {
        this.dataSource = DatabaseConnection.getDataSource();
    }

    @Override
    public void save(Project project) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                insertProject(conn, project);
            } catch (SQLException e) {
                conn.rollback();
                logger.error("Error saving project", e);
                throw new RuntimeException("Database error", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.error("Error getting connection", e);
            throw new RuntimeException("Database error", e);
        }
    }

    private void insertProject(Connection conn, Project project) throws SQLException {
        String sql = "INSERT INTO projects (id, name, creator_id) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, project.getId());
            stmt.setString(2, project.getName());
            stmt.setString(3, project.getCreatorId());
            stmt.executeUpdate();
        }

        // Insert project members
        insertProjectMembers(conn, project.getId(), project.getMemberIds());

        conn.commit();
    }

    private void insertProjectMembers(Connection conn, String projectId, Set<String> memberIds) throws SQLException {
        String sql = "INSERT INTO project_members (project_id, user_id) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (String memberId : memberIds) {
                logger.info("Inserting member to project_members table {}", memberId);
                stmt.setString(1, projectId);
                stmt.setString(2, memberId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void deleteProjectMembers(Connection conn, String projectId) throws SQLException {
        String sql = "DELETE FROM project_members WHERE project_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, projectId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String projectId) {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, projectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting project", e);
            throw new RuntimeException("Database error", e);
        }
    }

    @Override
    public Project findById(String id) {
        String sql = "SELECT p.*, pm.user_id FROM projects p " +
                "LEFT JOIN project_members pm ON p.id = pm.project_id " +
                "WHERE p.id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            Project project = null;
            Set<String> memberIds = new HashSet<>();

            while (rs.next()) {
                if (project == null) {
                    String projectId = rs.getString("id");
                    String name = rs.getString("name");
                    String creatorId = rs.getString("creator_id");
                    project = new Project(projectId, name, creatorId, new HashSet<>());
                }

                String memberId = rs.getString("user_id");
                if (memberId != null) {
                    memberIds.add(memberId);
                }
            }

            if (project != null) {
                project.setMemberIds(memberIds);
                return project;
            }
        } catch (SQLException e) {
            logger.error("Error finding project by ID", e);
        }
        return null;
    }

    @Override
    public List<Project> findAll() {
        String sql = "SELECT p.*, pm.user_id FROM projects p " +
                "LEFT JOIN project_members pm ON p.id = pm.project_id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            Map<String, Project> projectsMap = new HashMap<>();

            while (rs.next()) {
                String projectId = rs.getString("id");

                Project project = projectsMap.get(projectId);
                if (project == null) {
                    String name = rs.getString("name");
                    String creatorId = rs.getString("creator_id");
                    project = new Project(projectId, name, creatorId, new HashSet<>());
                    projectsMap.put(projectId, project);
                }

                String memberId = rs.getString("user_id");
                if (memberId != null) {
                    project.getMemberIds().add(memberId);
                }
            }

            return new ArrayList<>(projectsMap.values());
        } catch (SQLException e) {
            logger.error("Error finding all projects", e);
        }
        return List.of();
    }

    @Override
    public List<Project> findByMember(String userId) {
        String sql = "SELECT p.*, pm.user_id as member_id FROM projects p " +
                "JOIN project_members pm ON p.id = pm.project_id " +
                "WHERE pm.user_id = ? OR p.creator_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, userId);
            ResultSet rs = stmt.executeQuery();

            Map<String, Project> projectsMap = new HashMap<>();

            while (rs.next()) {
                String projectId = rs.getString("id");

                Project project = projectsMap.get(projectId);
                if (project == null) {
                    String name = rs.getString("name");
                    String creatorId = rs.getString("creator_id");
                    project = new Project(projectId, name, creatorId, new HashSet<>());
                    projectsMap.put(projectId, project);
                }

                // Sammle alle Mitglieder für dieses Projekt
                loadProjectMembers(conn, project);
            }

            return new ArrayList<>(projectsMap.values());
        } catch (SQLException e) {
            logger.error("Error finding projects by member", e);
        }
        return List.of();
    }

    private void loadProjectMembers(Connection conn, Project project) throws SQLException {
        String sql = "SELECT user_id FROM project_members WHERE project_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, project.getId());
            ResultSet rs = stmt.executeQuery();

            Set<String> memberIds = new HashSet<>();
            while (rs.next()) {
                memberIds.add(rs.getString("user_id"));
            }

            project.setMemberIds(memberIds);
        }
    }
}