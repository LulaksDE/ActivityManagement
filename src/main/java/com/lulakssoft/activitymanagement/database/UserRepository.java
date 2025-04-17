package com.lulakssoft.activitymanagement.database;

import com.lulakssoft.activitymanagement.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.user.Privileges;
import com.lulakssoft.activitymanagement.user.User;
import com.lulakssoft.activitymanagement.user.role.RoleFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IUserRepository {

    private final LoggerNotifier logger = LoggerFactory.getLogger();

    @Override
    public Optional<User> findById(String id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            logger.logError("Error finding user by ID: " + id, e);
        }
        return Optional.empty();
    }

    public Optional<User> findByUsername(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            logger.logError("Error finding user by username: " + username, e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            logger.logError("Error loading all users: " + e.getMessage(), e);
        }

        return users;
    }

    @Override
    public User save(User user) {
        String sql;
        boolean isUpdate = existsById(user.getId());

        if (isUpdate) {
            sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
        } else {
            sql = "INSERT INTO users (username, password, role, id) VALUES (?, ?, ?, ?)";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getPrivilage().name());
            stmt.setString(4, user.getId());

            stmt.executeUpdate();
            logger.logInfo("User " + user.getId() + " saved");
            return user;
        } catch (SQLException e) {
            logger.logError("Error saving user: " + e.getMessage(), e);
            return user;
        }
    }

    @Override
    public void delete(User user) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {

            stmt.setString(1, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.logError("Error deleting user: " + e.getMessage(), e);
        }
    }

    public boolean existsById(String id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM users WHERE id = ?")) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            logger.logError("Error checking if user exists by ID: " + id, e);
            return false;
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        Privileges privilage = Privileges.valueOf(rs.getString("role"));

        User user = new User(username, password, RoleFactory.getRole(privilage));
        user.setId(id);

        return user;
    }
}