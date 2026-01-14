package edu.univ.erp.ui.instructor;

import edu.univ.erp.api.instructor.InstructorAPI;
import edu.univ.erp.auth.Session;
import edu.univ.erp.service.SectionInfoDTO;
import edu.univ.erp.service.SectionStatsDTO;
import edu.univ.erp.ui.auth.LoginWindow;
import edu.univ.erp.ui.common.MaintenanceBanner;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class InstructorDashboard extends JFrame {

    private final Session userSession;
    private final InstructorAPI instructorAPI;
    private JPanel contentArea;
    private CardLayout cardLayout;

    public InstructorDashboard(Session session) {
        System.out.println("[InstructorDashboard] Constructor called");
        this.userSession = session;
        this.instructorAPI = new InstructorAPI();
        initComponents();
    }

    private void initComponents() {
        System.out.println("[InstructorDashboard] Initializing components");
        setTitle("University ERP - Instructor Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        addSidebarButton(sidebar, "📊  Dashboard Overview", "DASHBOARD");
        addSidebarButton(sidebar, "📝  Gradebook & Scores", "GRADEBOOK");
        addSidebarButton(sidebar, "📈  Class Statistics", "STATS");

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
            System.out.println("[InstructorDashboard] Logout clicked");
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.out.println("[InstructorDashboard] Logout confirmed");
                dispose();
                new LoginWindow().setVisible(true);
            }
        });
        sidebar.add(logoutBtn);

        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);

        System.out.println("[InstructorDashboard] Loading dashboard views");
        contentArea.add(createDashboardHomePanel(), "DASHBOARD");
        contentArea.add(new GradebookPanel(userSession), "GRADEBOOK");
        contentArea.add(new StatsPanel(userSession), "STATS");

        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel titleLbl = new JLabel("Instructor Portal");
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
    }

    private void addSidebarButton(JPanel sidebar, String text, String cardName) {
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

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setForeground(Color.WHITE);
                btn.setOpaque(true);
                btn.setBackground(new Color(30, 41, 59));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setForeground(new Color(203, 213, 225));
                btn.setOpaque(false);
                btn.setBackground(new Color(15, 23, 42));
            }
        });

        btn.addActionListener(e -> {
            System.out.println("[InstructorDashboard] Sidebar click → " + cardName);
            if ("DASHBOARD".equals(cardName)) {
                System.out.println("[InstructorDashboard] Refreshing dashboard data");
                contentArea.add(createDashboardHomePanel(), "DASHBOARD");
            }
            cardLayout.show(contentArea, cardName);
        });
        sidebar.add(btn);
    }

    private JPanel createDashboardHomePanel() {
        System.out.println("[InstructorDashboard] Building dashboard overview panel");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        int totalEnrollment = 0;
        int activeSections = 0;
        double avgGradeSum = 0;
        int gradedSections = 0;

        try {
            List<SectionInfoDTO> sections = instructorAPI.getMySections(userSession.getProfileId());
            System.out.println("[InstructorDashboard] Sections fetched: " + sections.size());
            activeSections = sections.size();

            for (SectionInfoDTO sec : sections) {
                SectionStatsDTO stats = instructorAPI.getSectionStatistics(sec.getSection().getSectionId());
                totalEnrollment += stats.getTotalStudents();

                Map<String, Double> avgs = stats.getAverageScores();
                if (!avgs.isEmpty()) {
                    double sectionAvg = avgs.values().stream().mapToDouble(d -> d).average().orElse(0.0);
                    avgGradeSum += sectionAvg;
                    gradedSections++;
                }
            }

        } catch (Exception e) {
            System.err.println("[InstructorDashboard] Error fetching instructor stats: " + e.getMessage());
        }

        double overallAvg = (gradedSections > 0) ? (avgGradeSum / gradedSections) : 0.0;

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(2000, 140));

        statsPanel.add(createStatCard("Average Score", String.format("%.1f", overallAvg), "🎓"));
        statsPanel.add(createStatCard("Active Sections", String.valueOf(activeSections), "📚"));
        statsPanel.add(createStatCard("Total Students", String.valueOf(totalEnrollment), "👨‍🎓"));

        panel.add(statsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsPanel.setOpaque(false);
        chartsPanel.setMaximumSize(new Dimension(2000, 300));
        chartsPanel.add(createChartCard("Grade Distribution", new BarChartPanel()));
        chartsPanel.add(createChartCard("Attendance", new PieChartPanel()));
        panel.add(chartsPanel);

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createStatCard(String title, String value, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        JPanel stripe = new JPanel();
        stripe.setBackground(new Color(37, 99, 235));
        stripe.setPreferredSize(new Dimension(10, 5));
        card.add(stripe, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel textPnl = new JPanel(new GridLayout(2, 1));
        textPnl.setOpaque(false);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setForeground(Color.GRAY);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valLbl.setForeground(new Color(30, 41, 59));
        textPnl.add(titleLbl);
        textPnl.add(valLbl);

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));

        body.add(textPnl, BorderLayout.CENTER);
        body.add(iconLbl, BorderLayout.EAST);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel createChartCard(String title, JPanel chartComponent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setBorder(new EmptyBorder(15, 20, 10, 20));
        card.add(titleLbl, BorderLayout.NORTH);
        card.add(chartComponent, BorderLayout.CENTER);
        return card;
    }

    class BarChartPanel extends JPanel {
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            int barW = 40, gap = 30, startX = 40, bottomY = h - 30;
            int[] v = {60, 90, 50, 30, 12};
            String[] l = {"A", "B", "C", "D", "F"};
            for (int i = 0; i < v.length; i++) {
                int bh = (int)((v[i] / 100.0) * (h - 60));
                g2.setColor(new Color(37, 99, 235));
                g2.fillRoundRect(startX + (i * (barW + gap)), bottomY - bh, barW, bh, 5, 5);
                g2.setColor(Color.GRAY);
                g2.drawString(l[i], startX + (i * (barW + gap)) + 15, bottomY + 15);
            }
            g2.setColor(new Color(226, 232, 240));
            g2.drawLine(20, bottomY, w - 20, bottomY);
        }
    }

    class PieChartPanel extends JPanel {
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int s = Math.min(getWidth(), getHeight()) - 60;
            int x = (getWidth() - s) / 2, y = (getHeight() - s) / 2;
            g2.setColor(new Color(37, 99, 235)); g2.fill(new Arc2D.Double(x, y, s, s, 0, 288, Arc2D.PIE));
            g2.setColor(new Color(147, 197, 253)); g2.fill(new Arc2D.Double(x, y, s, s, 288, 54, Arc2D.PIE));
            g2.setColor(new Color(219, 234, 254)); g2.fill(new Arc2D.Double(x, y, s, s, 288 + 54, 18, Arc2D.PIE));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("[InstructorDashboard] Starting standalone main()");
            new InstructorDashboard(new Session(201, "inst1", "INSTRUCTOR", 201)).setVisible(true);
        });
    }
}
