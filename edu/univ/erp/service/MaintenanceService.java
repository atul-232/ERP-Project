package edu.univ.erp.service;

import edu.univ.erp.data.SettingsDAO;

public class MaintenanceService {

    private final SettingsDAO settingsDAO = new SettingsDAO();
    private static final String MAINTENANCE_KEY = "maintenance_on";

    public boolean isMaintenanceModeOn() {
        System.out.println("[MaintenanceService] isMaintenanceModeOn called");
        boolean enabled = settingsDAO.findSettingValueByKey(MAINTENANCE_KEY)
                .map(Boolean::parseBoolean)
                .orElse(false);

        if (enabled) {
            System.out.println("[MaintenanceService] Maintenance Mode = ON");
        } else {
            System.out.println("[MaintenanceService] Maintenance Mode = OFF");
        }
        return enabled;
    }

    public void setMaintenanceMode(boolean isEnabled) {
        System.out.println("[MaintenanceService] setMaintenanceMode called -> " + isEnabled);
        if (isEnabled) {
            settingsDAO.updateSetting(MAINTENANCE_KEY, "true");
            System.out.println("[MaintenanceService] Maintenance Mode ENABLED");
        } else {
            settingsDAO.updateSetting(MAINTENANCE_KEY, "false");
            System.out.println("[MaintenanceService] Maintenance Mode DISABLED");
        }
    }
}
