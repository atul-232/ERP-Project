package edu.univ.erp.ui.admin;

import edu.univ.erp.service.AdminService;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class SystemSettingsPanel extends JPanel {

    private final AdminService adminService;
    private Map<String, String> currentSettings;
    private boolean isLoading = true; 

    // Components
    private JToggleButton maintenanceToggle;
    private JLabel maintenanceStatusLabel;
    
    private JToggleButton notifToggle;
    private JToggleButton updateToggle;
    private JToggleButton apiToggle;
    
    private JToggleButton themeToggle;
    private JToggleButton motionToggle;
    
    private JSlider timeoutSlider;
    private JLabel lastBackupLabel;

    public SystemSettingsPanel() {
        this.adminService = new AdminService();
        initComponents();
        loadSettingsFromService();
        applyLoadedSettings();
        isLoading = false; 
    }
    
    private void loadSettingsFromService() {
        currentSettings = adminService.getAllSettings();
        if(currentSettings == null || currentSettings.isEmpty()) {
            System.out.println("DEBUG: No settings found in DB.");
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252)); 
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // 1. Header
        JLabel titleLabel = new JLabel("System Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 41, 59));
        add(titleLabel, BorderLayout.NORTH);

        // 2. Scrollable Content Area
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0; 

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(createGeneralOptionsCard(), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        contentPanel.add(createMaintenanceCard(), gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        contentPanel.add(createAppearanceCard(), gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(createDataManagementCard(), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 2;
        contentPanel.add(createLogsCard(), gbc);
        
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        contentPanel.add(Box.createGlue(), gbc);
    }

    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setForeground(new Color(30, 41, 59));
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(titleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        return card;
    }

    private JPanel createGeneralOptionsCard() {
        JPanel card = createCard("General Options");
        
        notifToggle = createSwitch();
        updateToggle = createSwitch();
        apiToggle = createSwitch();
        
        setupGenericListener(notifToggle, "notifications");
        setupGenericListener(updateToggle, "auto_updates");
        setupGenericListener(apiToggle, "api_access");

        card.add(createToggleRow("System Notifications", notifToggle));
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(createToggleRow("Auto-Updates", updateToggle));
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(createToggleRow("Enable API Access", apiToggle));
        
        return card;
    }

    // --- MAINTENANCE CARD ---
    private JPanel createMaintenanceCard() {
        JPanel card = createCard("Maintenance & Security");

        JPanel maintRow = new JPanel(new BorderLayout());
        maintRow.setOpaque(false);
        maintRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        maintRow.setMaximumSize(new Dimension(400, 40));

        maintenanceStatusLabel = new JLabel("Maintenance Mode");
        maintenanceStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        maintenanceToggle = createSwitch(); 
        maintenanceToggle.addItemListener(e -> {
            if (isLoading) return; 
            boolean isSelected = (e.getStateChange() == ItemEvent.SELECTED);
            handleMaintenanceToggle(isSelected);
        });

        maintRow.add(maintenanceStatusLabel, BorderLayout.WEST);
        maintRow.add(maintenanceToggle, BorderLayout.EAST);
        card.add(maintRow);

        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Slider
        JPanel sliderRow = new JPanel(new BorderLayout());
        sliderRow.setOpaque(false);
        sliderRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderRow.setMaximumSize(new Dimension(400, 50));
        
        sliderRow.add(new JLabel("Session Timeout (min)"), BorderLayout.WEST);
        card.add(sliderRow);
        
        timeoutSlider = new JSlider(5, 60, 15);
        timeoutSlider.setBackground(Color.WHITE);
        timeoutSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        timeoutSlider.addChangeListener(e -> {
            if (!timeoutSlider.getValueIsAdjusting() && !isLoading) {
                adminService.updateSystemSetting("session_timeout", String.valueOf(timeoutSlider.getValue()));
            }
        });
        
        card.add(timeoutSlider);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton scanBtn = new JButton("🛡️ Run Security Scan");
        scanBtn.setBackground(new Color(37, 99, 235));
        scanBtn.setForeground(Color.WHITE);
        scanBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        scanBtn.setFocusPainted(false);
        scanBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        scanBtn.setMaximumSize(new Dimension(400, 35));
        scanBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Security Scan initiated.", "Scan", JOptionPane.INFORMATION_MESSAGE);
        });
        card.add(scanBtn);

        return card;
    }

    private JPanel createAppearanceCard() {
        JPanel card = createCard("Appearance & Theme");
        
        JLabel lbl = new JLabel("System Theme");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lbl);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        themePanel.setOpaque(false);
        themePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        themePanel.add(new JLabel("☀️ Light"));
        themeToggle = createSwitch();
        themeToggle.setText("Light"); 
        
        themeToggle.addItemListener(e -> {
            if(isLoading) return;
            if(themeToggle.isSelected()) {
                adminService.updateSystemSetting("theme", "dark");
                themeToggle.setText("Dark");
                setBackground(Color.DARK_GRAY);
            } else {
                adminService.updateSystemSetting("theme", "light");
                themeToggle.setText("Light");
                setBackground(new Color(248, 250, 252));
            }
            revalidate();
            repaint();
        });
        
        themePanel.add(themeToggle);
        themePanel.add(new JLabel("Dark 🌙"));
        
        card.add(themePanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        motionToggle = createSwitch();
        setupGenericListener(motionToggle, "reduced_motion");
        card.add(createToggleRow("Reduced Motion", motionToggle));
        
        return card;
    }

    private JPanel createDataManagementCard() {
        JPanel card = createCard("Data Management");

        JButton backupBtn = new JButton("🗄️ Backup Database Now");
        backupBtn.setBackground(new Color(37, 99, 235));
        backupBtn.setForeground(Color.WHITE);
        backupBtn.setFocusPainted(false);
        backupBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backupBtn.setMaximumSize(new Dimension(400, 35));
        backupBtn.addActionListener(e -> {
            adminService.triggerBackup();
            lastBackupLabel.setText("Last backup: Just now");
            JOptionPane.showMessageDialog(this, "Backup completed successfully.");
        });
        card.add(backupBtn);

        lastBackupLabel = new JLabel("Last backup: Checking...");
        lastBackupLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lastBackupLabel.setForeground(Color.GRAY);
        lastBackupLabel.setBorder(new EmptyBorder(5, 0, 15, 0));
        lastBackupLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lastBackupLabel);

        JButton clearBtn = new JButton("🗑️ Clear System Cache");
        clearBtn.setBackground(Color.WHITE);
        clearBtn.setForeground(new Color(220, 38, 38)); 
        clearBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 38, 38)));
        clearBtn.setFocusPainted(false);
        clearBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        clearBtn.setMaximumSize(new Dimension(400, 35));
        clearBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Clear system cache?", "Confirm", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                adminService.clearCache();
                JOptionPane.showMessageDialog(this, "System Cache Cleared.");
            }
        });
        card.add(clearBtn);

        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(65);
        progressBar.setForeground(new Color(37, 99, 235));
        progressBar.setPreferredSize(new Dimension(100, 8));
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(new JLabel("Storage Used: 65%"));
        card.add(progressBar);

        return card;
    }

    private JPanel createLogsCard() {
        JPanel card = createCard("Logs & Diagnostics");
        
        JButton viewLogsBtn = new JButton("📄 View System Logs");
        viewLogsBtn.setBackground(Color.WHITE);
        viewLogsBtn.setFocusPainted(false);
        viewLogsBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewLogsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logs refreshed.");
        });
        card.add(viewLogsBtn);
        
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JLabel recentLbl = new JLabel("Recent Logs");
        recentLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        recentLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(recentLbl);

        String[] cols = {"Time", "Level", "Message"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        java.util.List<String[]> logs = adminService.getSystemLogs();
        if(logs != null) {
            for(String[] row : logs) model.addRow(row);
        }
        
        JTable table = new JTable(model);
        table.setShowGrid(false);
        table.setRowHeight(25);
        table.setEnabled(false); 
        
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(400, 100));
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        tableScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(tableScroll);
        return card;
    }

    private void applyLoadedSettings() {
        if(currentSettings == null) return;
        
        // 1. Maintenance
        if(currentSettings.containsKey("maintenance_on")) {
            boolean val = Boolean.parseBoolean(currentSettings.get("maintenance_on"));
            maintenanceToggle.setSelected(val);
            updateMaintenanceVisuals(val); 
        }
        
        // 2. Toggles
        if(currentSettings.containsKey("notifications")) notifToggle.setSelected(Boolean.parseBoolean(currentSettings.get("notifications")));
        if(currentSettings.containsKey("auto_updates")) updateToggle.setSelected(Boolean.parseBoolean(currentSettings.get("auto_updates")));
        if(currentSettings.containsKey("api_access")) apiToggle.setSelected(Boolean.parseBoolean(currentSettings.get("api_access")));
        if(currentSettings.containsKey("reduced_motion")) motionToggle.setSelected(Boolean.parseBoolean(currentSettings.get("reduced_motion")));

        updateNormalToggleVisuals(notifToggle);
        updateNormalToggleVisuals(updateToggle);
        updateNormalToggleVisuals(apiToggle);
        updateNormalToggleVisuals(motionToggle);

        // 3. Theme
        if(currentSettings.containsKey("theme")) {
            String theme = currentSettings.get("theme");
            if("dark".equalsIgnoreCase(theme)) {
                themeToggle.setSelected(true);
                themeToggle.setText("Dark");
                setBackground(Color.DARK_GRAY);
            } else {
                themeToggle.setSelected(false);
                themeToggle.setText("Light");
                setBackground(new Color(248, 250, 252));
            }
        }
        
        // 4. Slider
        if(currentSettings.containsKey("session_timeout")) {
            try {
                timeoutSlider.setValue(Integer.parseInt(currentSettings.get("session_timeout")));
            } catch(Exception e) {}
        }
        
        if(currentSettings.containsKey("last_backup_time")) {
            lastBackupLabel.setText("Last backup: " + currentSettings.get("last_backup_time"));
        } else {
            lastBackupLabel.setText("Last backup: Never");
        }
    }

    // --- FIX: UPDATED MAINTENANCE HANDLER ---
    private void handleMaintenanceToggle(boolean turnOn) {
        try {
            System.out.println("DEBUG: Toggling Maintenance to: " + turnOn);
            
            // 1. Immediately update UI (Visual feedback first)
            updateMaintenanceVisuals(turnOn);
            
            // 2. Save to DB
            adminService.setMaintenanceMode(turnOn);
            
            // 3. Show dialog AFTER visual update so button doesn't get stuck
            SwingUtilities.invokeLater(() -> {
                String msg = turnOn ? "Maintenance Mode ENABLED.\nStudents cannot make changes." : "Maintenance Mode DISABLED.\nSystem is live.";
                JOptionPane.showMessageDialog(this, msg, "Settings Updated", JOptionPane.INFORMATION_MESSAGE);
            });
            
        } catch (Exception e) {
            System.out.println("DEBUG: Error in maintenance toggle: " + e.getMessage());
            // Revert on error
            maintenanceToggle.setSelected(!turnOn);
            updateMaintenanceVisuals(!turnOn);
        }
    }
    
    // Updates Text/Color based on state
    private void updateMaintenanceVisuals(boolean isOn) {
        if (isOn) {
            maintenanceToggle.setText("ON");
            maintenanceToggle.setBackground(new Color(37, 99, 235)); // Blue
            maintenanceToggle.setForeground(Color.WHITE);
            maintenanceStatusLabel.setText("Maintenance Mode ⚠️ (Active)");
            maintenanceStatusLabel.setForeground(new Color(234, 88, 12)); 
        } else {
            maintenanceToggle.setText("OFF");
            maintenanceToggle.setBackground(new Color(226, 232, 240)); // Gray
            maintenanceToggle.setForeground(Color.DARK_GRAY);
            maintenanceStatusLabel.setText("Maintenance Mode");
            maintenanceStatusLabel.setForeground(Color.BLACK);
        }
    }

    private void updateNormalToggleVisuals(JToggleButton toggle) {
        if (toggle.isSelected()) {
            toggle.setText("ON");
            toggle.setBackground(new Color(37, 99, 235)); 
            toggle.setForeground(Color.WHITE);
        } else {
            toggle.setText("OFF");
            toggle.setBackground(new Color(226, 232, 240)); 
            toggle.setForeground(Color.DARK_GRAY);
        }
    }

    private JPanel createToggleRow(String labelText, JToggleButton toggle) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(400, 40));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        row.add(label, BorderLayout.WEST);
        row.add(toggle, BorderLayout.EAST);
        return row;
    }

    private JToggleButton createSwitch() {
        JToggleButton toggle = new JToggleButton("OFF");
        toggle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        toggle.setFocusPainted(false);
        
        toggle.addItemListener(e -> {
            if(!isLoading && toggle != themeToggle && toggle != maintenanceToggle) {
                updateNormalToggleVisuals(toggle);
            }
        });
        return toggle;
    }
    
    private void setupGenericListener(JToggleButton toggle, String key) {
        toggle.addItemListener(e -> {
            if(isLoading) return;
            boolean val = (e.getStateChange() == ItemEvent.SELECTED);
            adminService.updateSystemSetting(key, String.valueOf(val));
        });
    }
    
}