package edu.univ.erp.api.maintenance;

import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.api.ApiException;

public class MaintenanceAPI {

    private final MaintenanceService maintenanceService;

    public MaintenanceAPI() {
        this.maintenanceService = new MaintenanceService();
        System.out.println("[DEBUG] MaintenanceAPI initialized");
    }

    public boolean isMaintenanceModeOn() {
        System.out.println("[DEBUG] isMaintenanceModeOn called");
        try {
            boolean status = maintenanceService.isMaintenanceModeOn();
            System.out.println("[DEBUG] isMaintenanceModeOn success -> " + status);
            return status;
        } catch (Exception e) {
            System.out.println("[DEBUG] isMaintenanceModeOn FAILED -> " + e.getMessage());
            throw new ApiException("Failed to check maintenance status: " + e.getMessage(), e);
        }
    }

    public String setMaintenanceMode(boolean isEnabled) {
        System.out.println("[DEBUG] setMaintenanceMode called -> enable=" + isEnabled);
        try {
            maintenanceService.setMaintenanceMode(isEnabled);
            String msg = "Maintenance mode " + (isEnabled ? "ENABLED" : "DISABLED");
            System.out.println("[DEBUG] setMaintenanceMode success -> " + msg);
            return msg;
        } catch (Exception e) {
            System.out.println("[DEBUG] setMaintenanceMode FAILED -> " + e.getMessage());
            throw new ApiException("Failed to update maintenance mode: " + e.getMessage(), e);
        }
    }

    public boolean isReadOnlyNow() {
        System.out.println("[DEBUG] isReadOnlyNow called");
        boolean status = isMaintenanceModeOn();
        System.out.println("[DEBUG] isReadOnlyNow -> " + status);
        return status;
    }

    public String getMaintenanceStatus() {
        System.out.println("[DEBUG] getMaintenanceStatus called");
        boolean isOn = isMaintenanceModeOn();
        String status = isOn ? "MAINTENANCE MODE ACTIVE - System is read-only" : "System is operational";
        System.out.println("[DEBUG] getMaintenanceStatus -> " + status);
        return status;
    }
}
