package edu.univ.erp.ui.instructor;

import edu.univ.erp.api.ApiException;
import edu.univ.erp.api.instructor.InstructorAPI;
import edu.univ.erp.auth.Session;
import edu.univ.erp.service.SectionInfoDTO;
import edu.univ.erp.service.SectionStatsDTO;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class StatsPanel extends JPanel {
    private final Session userSession;
    private final InstructorAPI instructorAPI;

    private JComboBox<SectionInfoDTO> sectionComboBox;
    private JPanel statsDisplayPanel;

    public StatsPanel(Session session) {
        System.out.println("[StatsPanel] Constructor called");
        this.userSession = session;
        this.instructorAPI = new InstructorAPI();
        initComponents();
        loadSectionsDropdown();
    }

    private void initComponents() {
        System.out.println("[StatsPanel] Initializing components");
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setBackground(new Color(248, 250, 252));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);

        JLabel label = new JLabel("Select Section for Stats:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));

        sectionComboBox = new JComboBox<>();
        sectionComboBox.setPreferredSize(new Dimension(350, 35));
        sectionComboBox.setBackground(Color.WHITE);

        topPanel.add(label);
        topPanel.add(sectionComboBox);

        statsDisplayPanel = new JPanel();
        statsDisplayPanel.setLayout(new BoxLayout(statsDisplayPanel, BoxLayout.Y_AXIS));
        statsDisplayPanel.setBackground(Color.WHITE);
        statsDisplayPanel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        statsDisplayPanel.add(createStatLabel("Please select a section to view statistics."));

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(statsDisplayPanel), BorderLayout.CENTER);

        sectionComboBox.addActionListener(e -> {
            System.out.println("[StatsPanel] Section changed");
            updateStatsView();
        });
    }

    private void loadSectionsDropdown() {
        System.out.println("[StatsPanel] Loading sections dropdown");
        sectionComboBox.removeAllItems();
        sectionComboBox.addItem(null);
        try {
            List<SectionInfoDTO> sections = instructorAPI.getMySections(userSession.getProfileId());
            System.out.println("[StatsPanel] Sections fetched: " + sections.size());
            for (SectionInfoDTO section : sections) {
                sectionComboBox.addItem(section);
            }
        } catch (ApiException e) {
            System.err.println("[StatsPanel] Error loading sections: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading sections: " + e.getMessage());
        }
    }

    private void updateStatsView() {
        SectionInfoDTO selected = (SectionInfoDTO) sectionComboBox.getSelectedItem();
        System.out.println("[StatsPanel] Updating stats view for selection: " + selected);
        statsDisplayPanel.removeAll();

        if (selected == null) {
            System.out.println("[StatsPanel] No section selected");
            statsDisplayPanel.add(createStatLabel("Please select a section to view statistics."));
            revalidate();
            repaint();
            return;
        }

        try {
            SectionStatsDTO stats = instructorAPI.getSectionStatistics(selected.getSection().getSectionId());
            System.out.println("[StatsPanel] Stats fetched successfully");

            JLabel totalLabel = createStatLabel("Total Students Enrolled: " + stats.getTotalStudents());
            totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            totalLabel.setForeground(new Color(37, 99, 235));
            statsDisplayPanel.add(totalLabel);

            statsDisplayPanel.add(Box.createRigidArea(new Dimension(0, 20)));

            JLabel averagesTitle = createStatLabel("Class Averages by Component:");
            averagesTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
            statsDisplayPanel.add(averagesTitle);
            statsDisplayPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            if (stats.getAverageScores().isEmpty()) {
                System.out.println("[StatsPanel] No grade data available");
                statsDisplayPanel.add(createStatLabel(" - No grades entered yet."));
            } else {
                for (Map.Entry<String, Double> entry : stats.getAverageScores().entrySet()) {
                    String component = entry.getKey();
                    Double average = entry.getValue();

                    JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    row.setOpaque(false);

                    JLabel nameLbl = new JLabel(component + ": ");
                    nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));

                    JLabel valLbl = new JLabel(String.format("%.2f%%", average));
                    valLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

                    if (average >= 80) valLbl.setForeground(new Color(22, 163, 74));
                    else if (average < 60) valLbl.setForeground(Color.RED);

                    row.add(nameLbl);
                    row.add(valLbl);
                    statsDisplayPanel.add(row);
                }
            }
        } catch (Exception e) {
            System.err.println("[StatsPanel] Error calculating stats: " + e.getMessage());
            statsDisplayPanel.add(createStatLabel("Error calculating stats: " + e.getMessage()));
        }

        statsDisplayPanel.revalidate();
        statsDisplayPanel.repaint();
        System.out.println("[StatsPanel] Stats view updated successfully");
    }

    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}
