package edu.univ.erp.ui.auth;

import edu.univ.erp.api.ApiException;
import edu.univ.erp.api.auth.AuthAPI;
import edu.univ.erp.auth.Session;
import edu.univ.erp.ui.admin.AdminDashboard;
import edu.univ.erp.ui.instructor.InstructorDashboard;
import edu.univ.erp.ui.student.StudentDashboard;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginWindow extends JFrame {

    private final AuthAPI authAPI;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton changePassButton;
    private JLabel statusLabel;

    public LoginWindow() {
        this.authAPI = new AuthAPI();
        initComponents();
    }

    private void initComponents() {
        setTitle(" ERP PROJECT");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 242, 245));
        setLayout(new GridBagLayout());

        JPanel cardPanel = new JPanel(new GridLayout(1, 2));
        cardPanel.setPreferredSize(new Dimension(750, 450));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel titleLabel = new JLabel("University ERP Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel userLbl = new JLabel("Username");
        userLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setMaximumSize(new Dimension(400, 40));
        usernameField.putClientProperty("JComponent.roundRect", true);
        usernameField.putClientProperty("JTextField.placeholderText", "Enter your username");
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(userLbl);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setMaximumSize(new Dimension(400, 40));
        passwordField.putClientProperty("JComponent.roundRect", true);
        passwordField.putClientProperty("JTextField.placeholderText", "Enter your password");
        passwordField.putClientProperty("JPasswordField.showRevealButton", true);
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(passLbl);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(37, 99, 235));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setMaximumSize(new Dimension(400, 45));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { loginButton.setBackground(new Color(29, 78, 216)); }
            public void mouseExited(MouseEvent e) { loginButton.setBackground(new Color(37, 99, 235)); }
        });

        formPanel.add(loginButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        changePassButton = new JButton("Change Password");
        changePassButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        changePassButton.setForeground(new Color(100, 116, 139));
        changePassButton.setBackground(Color.WHITE);
        changePassButton.setBorderPainted(false);
        changePassButton.setContentAreaFilled(false);
        changePassButton.setFocusPainted(false);
        changePassButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changePassButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        changePassButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { changePassButton.setForeground(new Color(37, 99, 235)); }
            public void mouseExited(MouseEvent e) { changePassButton.setForeground(new Color(100, 116, 139)); }
        });

        changePassButton.addActionListener(e -> {
            System.out.println("[LoginWindow] Change Password clicked");
            showChangePasswordDialog();
        });

        formPanel.add(changePassButton);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(220, 38, 38));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(statusLabel);

        JPanel illustrationPanel = new JPanel(new GridBagLayout());
        illustrationPanel.setBackground(new Color(248, 250, 252));
        JLabel iconLabel = new JLabel("🎓");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 120));
        JLabel textLabel = new JLabel("<html><div style='text-align: center;'>Welcome to<br>ERP PROJECT</div></html>");
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        textLabel.setForeground(new Color(71, 85, 105));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        illustrationPanel.add(iconLabel, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        illustrationPanel.add(textLabel, gbc);

        cardPanel.add(formPanel);
        cardPanel.add(illustrationPanel);

        add(cardPanel);

        loginButton.addActionListener(e -> handleLogin());
        getRootPane().setDefaultButton(loginButton);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        System.out.println("[LoginWindow] Login attempt -> Username: " + username);
        statusLabel.setText("Authenticating...");
        statusLabel.setForeground(Color.GRAY);

        SwingWorker<Session, Void> worker = new SwingWorker<>() {
            @Override
            protected Session doInBackground() {
                try {
                    return authAPI.login(username, password);
                } catch (Exception e) {
                    System.out.println("[LoginWindow] Login failed: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    Session session = get();
                    if (session != null) {
                        System.out.println("[LoginWindow] Login success -> Role: " + session.getRole());
                        dispose();
                        openDashboard(session);
                    } else {
                        statusLabel.setText("Invalid username or password");
                        statusLabel.setForeground(new Color(220, 38, 38));
                    }
                } catch (Exception e) {
                    System.out.println("[LoginWindow] Exception in login: " + e.getMessage());
                    statusLabel.setText("System error occurred.");
                }
            }
        };
        worker.execute();
    }

    private void showChangePasswordDialog() {
        JTextField userField = new JTextField();
        JPasswordField oldPassField = new JPasswordField();
        JPasswordField newPassField = new JPasswordField();
        JPasswordField confirmPassField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("Current Password:"));
        panel.add(oldPassField);
        panel.add(new JLabel("New Password:"));
        panel.add(newPassField);
        panel.add(new JLabel("Confirm New Password:"));
        panel.add(confirmPassField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Change Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String user = userField.getText();
            String oldPass = new String(oldPassField.getPassword());
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());

            System.out.println("[LoginWindow] ChangePassword request for user: " + user);

            if (user.isEmpty() || oldPass.isEmpty() || newPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                authAPI.changePassword(user, oldPass, newPass);
                System.out.println("[LoginWindow] Password changed successfully!");
                JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (ApiException e) {
                System.out.println("[LoginWindow] Change password failed: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openDashboard(Session session) {
        System.out.println("[LoginWindow] Opening dashboard for role: " + session.getRole());
        String role = session.getRole();
        if ("STUDENT".equalsIgnoreCase(role)) {
            new StudentDashboard(session).setVisible(true);
        } else if ("INSTRUCTOR".equalsIgnoreCase(role)) {
            new InstructorDashboard(session).setVisible(true);
        } else if ("ADMIN".equalsIgnoreCase(role)) {
            new AdminDashboard(session).setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
    }
}
