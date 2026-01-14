package edu.univ.erp.ui.common;

import edu.univ.erp.api.ApiException;
import edu.univ.erp.api.auth.AuthAPI;
import edu.univ.erp.auth.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChangePasswordDialog extends JDialog {

    private final Session session;
    private final AuthAPI authAPI;
    private JPasswordField currentPassField;
    private JPasswordField newPassField;
    private JPasswordField confirmPassField;
    private JLabel statusLabel;

    public ChangePasswordDialog(Frame owner, Session session) {
        super(owner, "Change Password", true);
        this.session = session;
        this.authAPI = new AuthAPI();
        System.out.println("[ChangePasswordDialog] Opened for user: " + session.getUsername());
        initComponents();
    }

    private void initComponents() {
        setSize(400, 350);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Update Password");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(15, 20, 10, 20));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        formPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        formPanel.add(new JLabel("Current Password:"));
        currentPassField = new JPasswordField();
        formPanel.add(currentPassField);

        formPanel.add(new JLabel("New Password:"));
        newPassField = new JPasswordField();
        formPanel.add(newPassField);

        formPanel.add(new JLabel("Confirm New Password:"));
        confirmPassField = new JPasswordField();
        formPanel.add(confirmPassField);

        add(formPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(220, 38, 38));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton saveBtn = new JButton("Change Password");
        saveBtn.setBackground(new Color(37, 99, 235));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(e -> {
            System.out.println("[ChangePasswordDialog] Change button clicked");
            handleChange();
        });

        footerPanel.add(statusLabel, BorderLayout.NORTH);
        footerPanel.add(saveBtn, BorderLayout.SOUTH);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private void handleChange() {
        String oldPass = new String(currentPassField.getPassword());
        String newPass = new String(newPassField.getPassword());
        String confirmPass = new String(confirmPassField.getPassword());

        System.out.println("[ChangePasswordDialog] Request -> user=" + session.getUsername());

        if (oldPass.isEmpty() || newPass.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            System.out.println("[ChangePasswordDialog] Missing input fields");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            statusLabel.setText("New passwords do not match.");
            System.out.println("[ChangePasswordDialog] Password mismatch");
            return;
        }

        try {
            authAPI.changePassword(session.getUsername(), oldPass, newPass);
            System.out.println("[ChangePasswordDialog] Password updated successfully!");
            JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (ApiException e) {
            statusLabel.setText(e.getMessage());
            System.out.println("[ChangePasswordDialog] Failed: " + e.getMessage());
        }
    }
}
