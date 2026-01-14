package edu.univ.erp.ui.admin;

import edu.univ.erp.api.admin.AdminAPI;
import edu.univ.erp.api.catalog.CatalogAPI;
import edu.univ.erp.auth.Session;
import edu.univ.erp.ui.auth.LoginWindow;
import edu.univ.erp.ui.common.MaintenanceBanner;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard extends JFrame {

    private final Session userSession;
    private final AdminAPI adminAPI;
    private final CatalogAPI catalogAPI;

    private JPanel contentArea;
    private CardLayout cardLayout;

    public AdminDashboard(Session session) {
        System.out.println("[AdminDashboard] Constructor called");

        if (session == null) {
            System.out.println("[AdminDashboard] Session is NULL, redirecting to login");
            new LoginWindow().setVisible(true);
            dispose();
            throw new IllegalStateException("Session missing");
        }

        this.userSession = session;
        this.adminAPI = new AdminAPI();
        this.catalogAPI = new CatalogAPI();

        initComponents();
    }

    private void initComponents() {
        System.out.println("[AdminDashboard] initComponents called");
        setTitle("ERP PROJECT - Admin");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1280, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(15, 23, 42));
        sidebar.setPreferredSize(new Dimension(240, 850));
        sidebar.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel logoLabel = new JLabel(" ERP PROJECT");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(new EmptyBorder(0, 25, 30, 0));
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logoLabel);

        addSidebarItem(sidebar, "📊  Dashboard Overview", "DASHBOARD");
        addSidebarItem(sidebar, "👥  User Management", "USERS");
        addSidebarItem(sidebar, "📚  Course Management", "COURSES");
        addSidebarItem(sidebar, "⚙️  System Settings", "SETTINGS");

        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = new JButton("⬅  Logout");
        logoutBtn.setForeground(new Color(148, 163, 184));
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setBorder(new EmptyBorder(0, 25, 20, 0));

        logoutBtn.addActionListener(e -> {
            System.out.println("[AdminDashboard] Logout clicked");
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.out.println("[AdminDashboard] Logging out");
                dispose();
                new LoginWindow().setVisible(true);
            } else {
                System.out.println("[AdminDashboard] Logout cancelled");
            }
        });

        sidebar.add(logoutBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.add(createDashboardHomePanel(), "DASHBOARD");
        contentArea.add(new UserManagementPanel(), "USERS");
        contentArea.add(new CourseManagementPanel(), "COURSES");
        contentArea.add(new SystemSettingsPanel(), "SETTINGS");

        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel titleLbl = new JLabel("Admin Portal");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLbl.setForeground(new Color(30, 41, 59));

        JLabel userLbl = new JLabel("👤 " + userSession.getUsername());
        userLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        header.add(titleLbl, BorderLayout.WEST);
        header.add(userLbl, BorderLayout.EAST);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(new MaintenanceBanner(), BorderLayout.NORTH);
        topContainer.add(header, BorderLayout.CENTER);

        rightPanel.add(topContainer, BorderLayout.NORTH);
        rightPanel.add(contentArea, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        System.out.println("[AdminDashboard] UI initialized successfully");
    }

    private void addSidebarItem(JPanel sidebar, String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(new Color(15, 23, 42));
        btn.setBorder(new EmptyBorder(12, 25, 12, 0));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(240, 50));

        btn.addActionListener(e -> {
            System.out.println("[AdminDashboard] Sidebar -> " + cardName);
            if ("DASHBOARD".equals(cardName)) {
                contentArea.add(createDashboardHomePanel(), "DASHBOARD");
            }
            cardLayout.show(contentArea, cardName);
        });

        sidebar.add(btn);
    }

    private JPanel createDashboardHomePanel() {
        System.out.println("[AdminDashboard] Loading dashboard stats");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        int totalStudents = 0, activeCourses = 0, totalInstructors = 0;

        try {
            totalStudents = adminAPI.getAllStudents() != null ? adminAPI.getAllStudents().size() : 0;
            activeCourses = catalogAPI.getAllCourses() != null ? catalogAPI.getAllCourses().size() : 0;
            totalInstructors = adminAPI.getAllInstructors() != null ? adminAPI.getAllInstructors().size() : 0;
        } catch (Exception e) {
            System.out.println("[AdminDashboard] Dashboard load error -> " + e.getMessage());
        }

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(2000, 120));
        statsPanel.add(createCard("Total Students", String.valueOf(totalStudents), "🎓", new Color(37, 99, 235)));
        statsPanel.add(createCard("Active Courses", String.valueOf(activeCourses), "📚", new Color(16, 185, 129)));
        statsPanel.add(createCard("Total Faculty", String.valueOf(totalInstructors), "👨‍🏫", new Color(245, 158, 11)));
        statsPanel.add(createCard("Est. Revenue", "$" + (totalStudents * 1000), "💲", new Color(99, 102, 241)));

        panel.add(statsPanel);
        panel.add(Box.createVerticalStrut(30));

        JLabel tableTitle = new JLabel("Recent Activities");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(tableTitle);
        panel.add(Box.createVerticalStrut(10));

        JTable table = new JTable(new DefaultTableModel(
                new Object[][]{
                        {"System Startup", "System", "Now", "Completed"},
                        {"Database Init", "DB Admin", "Now", "Verified"}
                },
                new String[]{"Activity", "User", "Date", "Status"}
        ));

        table.setRowHeight(40);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane);

        System.out.println("[AdminDashboard] Dashboard UI rendered");
        return panel;
    }

    private JPanel createCard(String title, String value, String icon, Color iconColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setForeground(Color.GRAY);
        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLbl.setForeground(iconColor);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLbl);
        textPanel.add(valueLbl);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(iconLbl, BorderLayout.EAST);

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> new AdminDashboard(new Session(1, "admin", "ADMIN", 0)).setVisible(true)
        );
    }
}
