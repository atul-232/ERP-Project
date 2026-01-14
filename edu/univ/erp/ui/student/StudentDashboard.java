package edu.univ.erp.ui.student;

import edu.univ.erp.auth.Session;
import edu.univ.erp.ui.auth.LoginWindow;
import edu.univ.erp.ui.common.MaintenanceBanner;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StudentDashboard extends JFrame {

    private final Session userSession;
    private JPanel contentArea;
    private CardLayout cardLayout;

    public StudentDashboard(Session session) {
        System.out.println("[StudentDashboard] Constructor called");
        this.userSession = session;
        initComponents();
    }

    private void initComponents() {
        System.out.println("[StudentDashboard] Initializing components");
        setTitle("University ERP - Student Portal");
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

        addSidebarButton(sidebar, "📖  Course Catalog", "CATALOG");
        addSidebarButton(sidebar, "📅  My Schedule", "TIMETABLE");
        addSidebarButton(sidebar, "🏆  My Grades", "GRADES");
        addSidebarButton(sidebar, "📄  Transcript", "TRANSCRIPT");

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
            System.out.println("[StudentDashboard] Logout clicked");
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.out.println("[StudentDashboard] Logout confirmed");
                dispose();
                new LoginWindow().setVisible(true);
            } else {
                System.out.println("[StudentDashboard] Logout cancelled");
            }
        });
        sidebar.add(logoutBtn);

        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        System.out.println("[StudentDashboard] Loading main UI panels");
        contentArea.add(new CatalogPanel(userSession), "CATALOG");
        contentArea.add(new TimetablePanel(userSession), "TIMETABLE");
        contentArea.add(new GradesPanel(userSession), "GRADES");
        contentArea.add(new TranscriptPanel(userSession), "TRANSCRIPT");

        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel titleLbl = new JLabel("Student Portal");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLbl.setForeground(new Color(30, 41, 59));

        JLabel userLbl = new JLabel("🎓 " + userSession.getUsername());
        userLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        header.add(titleLbl, BorderLayout.WEST);
        header.add(userLbl, BorderLayout.EAST);

        JPanel topContainer = new JPanel(new BorderLayout());
        System.out.println("[StudentDashboard] MaintenanceBanner added");
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
            System.out.println("[StudentDashboard] Sidebar clicked → " + cardName);
            cardLayout.show(contentArea, cardName);
        });
        sidebar.add(btn);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("[StudentDashboard] main() launched");
            new StudentDashboard(new Session(101, "student", "STUDENT", 101)).setVisible(true);
        });
    }
}
