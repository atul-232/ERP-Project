package edu.univ.erp.data;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SettingsDAO {

    public Optional<String> findSettingValueByKey(String key) {
        System.out.println("[DEBUG] findSettingValueByKey called -> key=" + key);
        String sql = "SELECT value FROM settings WHERE `key` = ?";
        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, key);
            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String value = rs.getString("value");
                System.out.println("[DEBUG] findSettingValueByKey SUCCESS -> value=" + value);
                return Optional.ofNullable(value);
            } else {
                System.out.println("[DEBUG] findSettingValueByKey -> NO RESULT");
            }

        } catch (SQLException e) {
            System.out.println("[DEBUG] findSettingValueByKey FAILED -> " + e.getMessage());
            throw new DataAccessException("Error finding setting with key: " + key, e);
        }
        return Optional.empty();
    }

    public void updateSetting(String key, String value) {
        System.out.println("[DEBUG] updateSetting called -> key=" + key + ", value=" + value);
        String sql = "INSERT INTO settings(`key`, `value`) VALUES (?, ?) ON DUPLICATE KEY UPDATE value = ?";
        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, key);
            pst.setString(2, value);
            pst.setString(3, value);

            System.out.println("[DEBUG] Executing INSERT/UPDATE -> " + sql);
            pst.executeUpdate();
            System.out.println("[DEBUG] updateSetting SUCCESS");

        } catch (SQLException e) {
            System.out.println("[DEBUG] updateSetting FAILED -> " + e.getMessage());
            throw new DataAccessException("Error updating setting for key: " + key, e);
        }
    }

    public Map<String, String> findAll() {
        System.out.println("[DEBUG] findAll called");
        Map<String, String> settings = new HashMap<>();
        String sql = "SELECT `key`, `value` FROM settings";
        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            while (rs.next()) {
                settings.put(rs.getString("key"), rs.getString("value"));
            }
            System.out.println("[DEBUG] findAll SUCCESS -> count=" + settings.size());
            return settings;

        } catch (SQLException e) {
            System.out.println("[DEBUG] findAll FAILED -> " + e.getMessage());
            throw new DataAccessException("Error retrieving all settings", e);
        }
    }
}
