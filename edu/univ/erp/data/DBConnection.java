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
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("resources/config.properties")) {
                props.load(fis);
            }

            host = props.getProperty("db.host", "localhost");
            port = props.getProperty("db.port", "3306");
            user = props.getProperty("db.user", "root");
            password = props.getProperty("db.password");
            dbNameOverride = props.getProperty("db.name");

            System.out.println("[DEBUG] Loaded DB config -> host=" + host + ", port=" + port + ", user=" + user + ", dbNameOverride=" + dbNameOverride);

            if (password == null || password.isEmpty()) {
                System.out.println("[DEBUG] WARNING -> DB password missing in config.properties");
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[DEBUG] MySQL JDBC driver loaded");

        } catch (IOException e) {
            System.out.println("[DEBUG] FATAL -> config.properties not found or unreadable");
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
