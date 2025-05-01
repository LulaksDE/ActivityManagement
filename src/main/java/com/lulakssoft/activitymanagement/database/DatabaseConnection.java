package com.lulakssoft.activitymanagement.database;

import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:h2:./activitymanagement";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private static final LoggerNotifier logger = LoggerFactory.getLogger();
    private static HikariDataSource dataSource;

    static {
        try {
            initializeDataSource();
            initDatabase();
            logger.logInfo("Database connection pool initialized");
        } catch (Exception e) {
            logger.logError("Error when initializing database", e);
        }
    }

    private static void initializeDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(5);
        config.setAutoCommit(true);
        config.setConnectionTimeout(30000);
        config.setLeakDetectionThreshold(60000);

        dataSource = new HikariDataSource(config);
    }

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            logger.logWarning("DataSource was close. New initialization.");
            initializeDataSource();
        }
        return dataSource.getConnection();
    }

    private static void initDatabase() {
        try (Connection connection = getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(
                        "CREATE TABLE IF NOT EXISTS users (" +
                                "id VARCHAR(36) PRIMARY KEY, " +
                                "username VARCHAR(50) NOT NULL UNIQUE, " +
                                "password VARCHAR(100) NOT NULL, " +
                                "role VARCHAR(20) NOT NULL" +
                                ")"
                );

                stmt.execute(
                        "CREATE TABLE IF NOT EXISTS projects (" +
                                "id VARCHAR(36) PRIMARY KEY, " +
                                "name VARCHAR(100) NOT NULL, " +
                                "creator_id VARCHAR(36) NOT NULL, " +
                                "FOREIGN KEY (creator_id) REFERENCES users(id)" +
                                ")"
                );

                stmt.execute(
                        "CREATE TABLE IF NOT EXISTS project_members (" +
                                "project_id VARCHAR(36) NOT NULL, " +
                                "user_id VARCHAR(36) NOT NULL, " +
                                "PRIMARY KEY (project_id, user_id), " +
                                "FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE, " +
                                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                                ")"
                );

                stmt.execute(
                        "CREATE TABLE IF NOT EXISTS activities (" +
                                "id VARCHAR(36) PRIMARY KEY, " +
                                "title VARCHAR(100) NOT NULL, " +
                                "description TEXT, " +
                                "due_date DATE, " +
                                "completed BOOLEAN NOT NULL DEFAULT FALSE, " +
                                "priority VARCHAR(20), " +
                                "project_id VARCHAR(36) NOT NULL, " +
                                "creator_id VARCHAR(36) NOT NULL, " +
                                "FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE, " +
                                "FOREIGN KEY (creator_id) REFERENCES users(id)" +
                                ")"
                );

                stmt.execute(
                        "CREATE TABLE IF NOT EXISTS activity_members (" +
                                "activity_id VARCHAR(36) NOT NULL, " +
                                "user_id VARCHAR(36) NOT NULL, " +
                                "PRIMARY KEY (activity_id, user_id), " +
                                "FOREIGN KEY (activity_id) REFERENCES activities(id) ON DELETE CASCADE, " +
                                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                                ")"
                );
            } catch (SQLException e) {
                logger.logError("Error creating tables: " + e.getMessage(), e);
            }
            logger.logInfo("Database initialized successfully");
        } catch (SQLException e) {
            logger.logError("Error initializing database: " + e.getMessage(), e);
        }
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            logger.logInfo("Closing database connection pool");
            dataSource.close();
            logger.logInfo("Database connection pool closed");
        }
    }
}
