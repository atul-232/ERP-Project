package edu.univ.erp.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DBConnection {
    private static String host, port, user, password, dbNameOverride;

    static {
        System.out.println("[DEBUG] DBConnection static initializer called");
        try {
            // Try to load from environment variables first (production/cloud mode)
            host = System.getenv("DB_HOST");
            port = System.getenv("DB_PORT");
            user = System.getenv("DB_USER");
            password = System.getenv("DB_PASSWORD");
            dbNameOverride = System.getenv("DB_NAME");

            // If environment variables are missing, fallback to config.properties (local mode)
            if (host == null || port == null || user == null || password == null) {
                System.out.println("[DEBUG] Environment variables missing. Falling back to config.properties...");
                Properties props = new Properties();
                try (FileInputStream fis = new FileInputStream("resources/config.properties")) {
                    props.load(fis);
                }
                if (host == null) host = props.getProperty("db.host", "localhost");
                if (port == null) port = props.getProperty("db.port", "3306");
                if (user == null) user = props.getProperty("db.user", "root");
                if (password == null) password = props.getProperty("db.password");
                if (dbNameOverride == null) dbNameOverride = props.getProperty("db.name");
            }

            System.out.println("[DEBUG] Loaded DB config -> host=" + host + ", port=" + port + ", user=" + user + ", dbNameOverride=" + dbNameOverride);

            if (password == null || password.isEmpty()) {
                System.out.println("[DEBUG] WARNING -> DB password missing");
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[DEBUG] MySQL JDBC driver loaded");

        } catch (IOException e) {
            System.out.println("[DEBUG] FATAL -> config.properties not found or unreadable, and env variables not set");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("[DEBUG] FATAL -> MySQL JDBC driver not found");
            e.printStackTrace();
        }
    }

    public static Connection getConnection(String dbName) throws SQLException {
        System.out.println("[DEBUG] getConnection called -> dbName=" + dbName);

        if (password == null || password.isEmpty()) {
            System.out.println("[DEBUG] getConnection FAILED -> missing password");
            throw new SQLException("Database connection failed: Password is not set in config.properties.");
        }

        String targetDb = (dbNameOverride != null && !dbNameOverride.isEmpty()) ? dbNameOverride : dbName;
        String url = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true",
                host, port, targetDb);

        System.out.println("[DEBUG] Attempting DB connection -> url=" + url + ", user=" + user);

        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("[DEBUG] DB connection SUCCESS -> targetDb=" + targetDb);

        return conn;
    }

    public static String getDbNameOverride() {
        return dbNameOverride;
    }
}
