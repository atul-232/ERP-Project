package edu.univ.erp.ui.student;

import edu.univ.erp.api.ApiException;
import edu.univ.erp.api.student.StudentAPI;
import edu.univ.erp.auth.Session;
import edu.univ.erp.domain.Enrollment;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class GradesPanel extends JPanel {

    private final Session userSession;
    private final StudentAPI studentAPI;

    private JTable gradesTable;
    private DefaultTableModel tableModel;

    private JLabel gpaLabel;
    private JLabel attemptedLabel;
    private JLabel passedLabel;

    public GradesPanel(Session session) {
        System.out.println("[GradesPanel] Constructor called");
        this.userSession = session;
        this.studentAPI = new StudentAPI();
        initComponents();
        loadGradesData();
    }

    private void initComponents() {
        System.out.println("[GradesPanel] Initializing components");
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Student Grades");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 41, 59));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        JComboBox<String> termCombo = new JComboBox<>(new String[]{"Semester: Fall 2025", "All Semesters"});
        termCombo.setBackground(Color.WHITE);

        JButton printBtn = new JButton("Print / Download");
        printBtn.setBackground(new Color(37, 99, 235));
        printBtn.setForeground(Color.WHITE);
        printBtn.setFocusPainted(false);
        printBtn.addActionListener(e -> {
            System.out.println("[GradesPanel] Print button clicked");
            try {
                if (!gradesTable.print()) System.out.println("[GradesPanel] Printing cancelled");
            } catch (java.awt.print.PrinterException ex) {
                System.err.println("[GradesPanel] Print error: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Print Error: " + ex.getMessage());
            }
        });

        actionPanel.add(termCombo);
        actionPanel.add(printBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(actionPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel summaryCard = new JPanel(new BorderLayout());
        summaryCard.setBackground(Color.WHITE);
        summaryCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(20, 25, 20, 25)
        ));
        summaryCard.setMaximumSize(new Dimension(2000, 120));

        JPanel gpaSection = new JPanel(new GridLayout(2, 1));
        gpaSection.setOpaque(false);
        JLabel gpaTitle = new JLabel("GPA Summary Card");
        gpaTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gpaTitle.setForeground(Color.GRAY);

        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        valuePanel.setOpaque(false);
        JLabel gpaText = new JLabel("Overall GPA:");
        gpaText.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gpaText.setForeground(new Color(37, 99, 235));

        gpaLabel = new JLabel("0.00");
        gpaLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        gpaLabel.setForeground(new Color(37, 99, 235));

        JLabel trendLabel = new JLabel(" (+0.10) Trend ");
        trendLabel.setOpaque(true);
        trendLabel.setBackground(new Color(220, 252, 231));
        trendLabel.setForeground(new Color(22, 163, 74));
        trendLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        valuePanel.add(gpaText);
        valuePanel.add(gpaLabel);
        valuePanel.add(trendLabel);

        gpaSection.add(gpaTitle);
        gpaSection.add(valuePanel);

        JPanel creditsSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        creditsSection.setOpaque(false);

        attemptedLabel = new JLabel("Attempted Credits: 0");
        passedLabel = new JLabel("Passed Credits: 0");
        attemptedLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passedLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        creditsSection.add(attemptedLabel);
        creditsSection.add(passedLabel);

        JPanel leftSide = new JPanel(new BorderLayout());
        leftSide.setOpaque(false);
        leftSide.add(gpaSection, BorderLayout.NORTH);
        leftSide.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        leftSide.add(creditsSection, BorderLayout.SOUTH);

        summaryCard.add(leftSide, BorderLayout.CENTER);
        contentPanel.add(summaryCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        String[] columns = {"Course", "Title", "Grade", "Credits"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        gradesTable = new JTable(tableModel);
        gradesTable.setRowHeight(50);
        gradesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        gradesTable.getTableHeader().setBackground(new Color(30, 41, 59));
        gradesTable.getTableHeader().setForeground(Color.WHITE);
        gradesTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        gradesTable.getColumnModel().getColumn(2).setCellRenderer(new GradeRenderer());

        JScrollPane tableScroll = new JScrollPane(gradesTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        contentPanel.add(tableScroll);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void loadGradesData() {
        System.out.println("[GradesPanel] Loading grades data for student profile ID " + userSession.getProfileId());
        try {
            List<Enrollment> enrollments = studentAPI.getMyGrades(userSession.getProfileId());
            System.out.println("[GradesPanel] Grades fetched: " + enrollments.size());
            tableModel.setRowCount(0);

            double totalPoints = 0;
            int totalCredits = 0;
            int passedCredits = 0;

            for (Enrollment e : enrollments) {
                String courseCode = "CS" + e.getSectionId();
                String title = "Course Title for Section " + e.getSectionId();
                int credits = 4;
                String grade = e.getFinalGrade() != null ? e.getFinalGrade() : "Pending";

                if (!grade.equals("Pending")) {
                    totalCredits += credits;
                    double points = gradeToPoints(grade);
                    totalPoints += (points * credits);
                    if (points > 0) passedCredits += credits;
                }

                tableModel.addRow(new Object[]{courseCode, title, grade, credits});
            }

            if (totalCredits > 0) {
                double gpa = totalPoints / totalCredits;
                gpaLabel.setText(String.format("%.2f", gpa));
                System.out.println("[GradesPanel] GPA calculated: " + gpa);
            } else {
                gpaLabel.setText("N/A");
                System.out.println("[GradesPanel] No graded credits yet → GPA unavailable");
            }

            attemptedLabel.setText("Attempted Credits: " + totalCredits);
            passedLabel.setText("Passed Credits: " + passedCredits);

            System.out.println("[GradesPanel] Table rows updated: " + tableModel.getRowCount());
        } catch (ApiException e) {
            System.err.println("[GradesPanel] Error loading grades: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading grades.");
        }
    }

    private double gradeToPoints(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            default: return 0.0;
        }
    }

    class GradeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String grade = (String) value;
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            if ("A".equals(grade)) setForeground(new Color(22, 163, 74));
            else if ("B".equals(grade)) setForeground(new Color(37, 99, 235));
            else if ("C".equals(grade)) setForeground(new Color(202, 138, 4));
            else if ("F".equals(grade)) setForeground(new Color(220, 38, 38));
            else setForeground(Color.GRAY);
            return c;
        }
    }
}
