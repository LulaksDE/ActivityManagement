package com.lulakssoft.activitymanagement.infrastructure.persistence;

import com.lulakssoft.activitymanagement.database.DatabaseConnection;
import com.lulakssoft.activitymanagement.domain.model.user.Role;
import com.lulakssoft.activitymanagement.domain.model.user.RoleFactory;
import com.lulakssoft.activitymanagement.domain.model.user.User;
import com.lulakssoft.activitymanagement.domain.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserRepository implements IUserRepository {
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(JdbcUserRepository.class);

    public JdbcUserRepository() {
        this.dataSource = DatabaseConnection.getDataSource();
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (id, username, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

                logger.info("Saving new user: {}", user.getUsername());
                // Insert new user
                stmt.setString(1, user.getId());
                stmt.setString(2, user.getUsername());
                stmt.setString(3, user.getPasswordHash());
                stmt.setString(4, user.getRole().getName());
                stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error saving user", e);
            throw new RuntimeException("Database error", e);
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET username=?, password=?, role=? WHERE id=?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getRole().getName());
            stmt.setString(4, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating user", e);
            throw new RuntimeException("Database error", e);
        }
    }

    @Override
    public void delete(String userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting user", e);
            throw new RuntimeException("Database error", e);
        }
    }

    @Override
    public User findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding user by ID", e);
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding user by username", e);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding all users", e);
        }
        return users;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String roleName = rs.getString("role");
        Role role = RoleFactory.createFromString(roleName);

        return new User(id, username, password, role);
    }
}