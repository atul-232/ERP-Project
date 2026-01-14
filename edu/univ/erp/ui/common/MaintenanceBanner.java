package edu.univ.erp.ui.common;

import edu.univ.erp.api.maintenance.MaintenanceAPI;
import java.awt.*;
import javax.swing.*;

public class MaintenanceBanner extends JPanel {

    private final MaintenanceAPI maintenanceAPI;
    private JLabel bannerLabel;
    private JButton closeButton;

    public MaintenanceBanner() {
        System.out.println("[MaintenanceBanner] Constructor called");
        this.maintenanceAPI = new MaintenanceAPI();
        initComponents();
        updateBanner();
    }

    private void initComponents() {
        System.out.println("[MaintenanceBanner] Initializing components");
        setLayout(new BorderLayout());
        setBackground(new Color(255, 243, 205));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 222, 173)));

        bannerLabel = new JLabel("⚠️ Maintenance Scheduled: System will be read-only during this time.", SwingConstants.LEFT);
        bannerLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        bannerLabel.setForeground(new Color(133, 100, 4));
        bannerLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        closeButton = new JButton("✕");
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setForeground(new Color(133, 100, 4));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> {
            System.out.println("[MaintenanceBanner] Close button clicked → Banner hidden");
            setVisible(false);
        });

        add(bannerLabel, BorderLayout.CENTER);
        add(closeButton, BorderLayout.EAST);

        Timer timer = new Timer(5000, e -> {
            System.out.println("[MaintenanceBanner] Timer triggered → Checking maintenance status");
            updateBanner();
        });
        timer.start();
    }

    private void updateBanner() {
        try {
            boolean isMaintenanceMode = maintenanceAPI.isMaintenanceModeOn();
            if (isMaintenanceMode) {
                System.out.println("[MaintenanceBanner] MAINTENANCE MODE ENABLED");
                bannerLabel.setText("⚠️ MAINTENANCE MODE ACTIVE: You can view data, but cannot save changes.");
                setVisible(true);
            } else {
                System.out.println("[MaintenanceBanner] Maintenance OFF → Hiding banner");
                setVisible(false);
            }
        } catch (Exception e) {
            System.err.println("[MaintenanceBanner] Failed to check maintenance status: " + e.getMessage());
        }
    }
}
