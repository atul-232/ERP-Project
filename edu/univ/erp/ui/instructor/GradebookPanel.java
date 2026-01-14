package edu.univ.erp.ui.instructor;

import edu.univ.erp.api.ApiException;
import edu.univ.erp.api.instructor.InstructorAPI;
import edu.univ.erp.auth.Session;
import edu.univ.erp.service.GradebookDTO;
import edu.univ.erp.service.SectionInfoDTO;
import edu.univ.erp.service.StudentGradeDTO;
import java.awt.*;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class GradebookPanel extends JPanel {

    private final Session userSession;
    private final InstructorAPI instructorAPI;

    private JComboBox<SectionInfoDTO> sectionComboBox;
    private JTable gradebookTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    private GradebookDTO currentGradebook;

    public GradebookPanel(Session session) {
        System.out.println("[GradebookPanel] Constructor called");
        this.userSession = session;
        this.instructorAPI = new InstructorAPI();
        initComponents();
        loadSectionsDropdown();
    }

    private void initComponents() {
        System.out.println("[GradebookPanel] Initializing UI components");
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftHeader.setOpaque(false);

        JLabel courseLbl = new JLabel("Course Selection:");
        courseLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        courseLbl.setForeground(new Color(30, 41, 59));

        sectionComboBox = new JComboBox<>();
        sectionComboBox.setPreferredSize(new Dimension(350, 35));
        sectionComboBox.setBackground(Color.WHITE);
        sectionComboBox.addActionListener(e -> {
            System.out.println("[GradebookPanel] Section changed → Loading gradebook");
            loadGradebookTable();
        });

        leftHeader.add(courseLbl);
        leftHeader.add(sectionComboBox);

        JButton saveBtn = new JButton("Save Grades");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.setBackground(new Color(37, 99, 235));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> {
            System.out.println("[GradebookPanel] Save button clicked");
            handleSaveGrades();
        });

        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(saveBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 0, 0, 0),
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1)
        ));

        JLabel tableTitle = new JLabel(" Gradebook View");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(new Color(30, 41, 59));
        tableTitle.setBorder(new EmptyBorder(15, 20, 15, 20));
        tableContainer.add(tableTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 1 || column == getColumnCount() - 1) return false;
                return true;
            }
        };

        gradebookTable = new JTable(tableModel);
        gradebookTable.setRowHeight(45);
        gradebookTable.setShowVerticalLines(false);
        gradebookTable.setSelectionBackground(new Color(241, 245, 249));
        gradebookTable.setSelectionForeground(Color.BLACK);
        gradebookTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        gradebookTable.getTableHeader().setBackground(new Color(241, 245, 249));
        gradebookTable.getTableHeader().setForeground(new Color(71, 85, 105));
        gradebookTable.getTableHeader().setPreferredSize(new Dimension(0, 40));

        gradebookTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (tableModel.isCellEditable(row, column)) {
                    setBackground(Color.WHITE);
                    setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createEmptyBorder(5, 5, 5, 5),
                            BorderFactory.createLineBorder(new Color(203, 213, 225))
                    ));
                    setHorizontalAlignment(CENTER);
                } else {
                    setBackground(new Color(248, 250, 252));
                    setBorder(new EmptyBorder(0, 10, 0, 0));
                    setHorizontalAlignment(LEFT);
                }

                if (table.getColumnName(column).equals("Final Grade")) {
                    setFont(new Font("Segoe UI", Font.BOLD, 14));
                    if (value != null && !value.toString().isEmpty()) {
                        if (value.toString().startsWith("A") || value.toString().startsWith("B")) setForeground(new Color(22, 163, 74));
                        else if (value.toString().startsWith("F")) setForeground(Color.RED);
                        else setForeground(Color.BLACK);
                    }
                } else {
                    setForeground(Color.BLACK);
                    setFont(new Font("Segoe UI", Font.PLAIN, 13));
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(gradebookTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableContainer.add(scrollPane, BorderLayout.CENTER);
        add(tableContainer, BorderLayout.CENTER);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void loadSectionsDropdown() {
        System.out.println("[GradebookPanel] Loading sections dropdown");
        sectionComboBox.removeAllItems();
        sectionComboBox.addItem(null);
        try {
            List<SectionInfoDTO> sections = instructorAPI.getMySections(userSession.getProfileId());
            System.out.println("[GradebookPanel] Sections fetched: " + sections.size());
            for (SectionInfoDTO sectionInfo : sections) {
                sectionComboBox.addItem(sectionInfo);
            }
        } catch (ApiException e) {
            System.err.println("[GradebookPanel] Error loading sections: " + e.getMessage());
            statusLabel.setText("Error loading sections.");
            statusLabel.setForeground(Color.RED);
        }
    }

    private void loadGradebookTable() {
        SectionInfoDTO selected = (SectionInfoDTO) sectionComboBox.getSelectedItem();

        if (selected == null) {
            System.out.println("[GradebookPanel] No section selected → Clearing table");
            tableModel.setColumnCount(0);
            tableModel.setRowCount(0);
            return;
        }

        System.out.println("[GradebookPanel] Fetching gradebook for section " + selected);
        try {
            this.currentGradebook = instructorAPI.getGradebookData(selected.getSection().getSectionId());
            System.out.println("[GradebookPanel] Gradebook data received");

            Vector<String> columns = new Vector<>();
            columns.add("Student ID");
            columns.add("Name");
            columns.addAll(currentGradebook.getAssessmentComponents());
            columns.add("Final Grade");

            tableModel.setColumnIdentifiers(columns);
            tableModel.setRowCount(0);

            for (StudentGradeDTO studentGrade : currentGradebook.getStudentGrades()) {
                Vector<Object> row = new Vector<>();
                row.add(studentGrade.getStudent().getRollNo());
                row.add(studentGrade.getStudent().getFullName());
                for (String component : currentGradebook.getAssessmentComponents()) {
                    row.add(studentGrade.getScores().getOrDefault(component, null));
                }
                row.add("");
                tableModel.addRow(row);
            }

            System.out.println("[GradebookPanel] Gradebook table updated");
            statusLabel.setText("Loaded gradebook for " + selected);
            statusLabel.setForeground(Color.GRAY);

        } catch (ApiException e) {
            System.err.println("[GradebookPanel] Error loading gradebook: " + e.getMessage());
            statusLabel.setText("Error loading gradebook: " + e.getMessage());
            statusLabel.setForeground(Color.RED);
        }
    }

    private void handleSaveGrades() {
        if (currentGradebook == null) {
            System.err.println("[GradebookPanel] Save failed → no course selected");
            statusLabel.setText("Please select a course first.");
            statusLabel.setForeground(Color.RED);
            return;
        }

        if (gradebookTable.isEditing()) {
            gradebookTable.getCellEditor().stopCellEditing();
        }

        try {
            List<String> components = currentGradebook.getAssessmentComponents();
            List<StudentGradeDTO> studentGrades = currentGradebook.getStudentGrades();
            int savedCount = 0;

            System.out.println("[GradebookPanel] Saving grade entries...");
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int enrollmentId = studentGrades.get(i).getEnrollmentId();

                for (int j = 2; j < tableModel.getColumnCount() - 1; j++) {
                    String component = components.get(j - 2);
                    Object value = tableModel.getValueAt(i, j);

                    if (value != null && !value.toString().isEmpty()) {
                        try {
                            double score = Double.parseDouble(value.toString());
                            if (score < 0 || score > 100) throw new NumberFormatException();

                            instructorAPI.submitGrade(enrollmentId, component, score);
                            savedCount++;
                        } catch (NumberFormatException ex) {
                            System.err.println("[GradebookPanel] Invalid score for: " + tableModel.getValueAt(i, 1));
                            statusLabel.setText("Invalid score for student " + tableModel.getValueAt(i, 1));
                            statusLabel.setForeground(Color.RED);
                            return;
                        }
                    }
                }
            }

            instructorAPI.calculateFinalGradesForSection(((SectionInfoDTO)sectionComboBox.getSelectedItem()).getSection().getSectionId());
            System.out.println("[GradebookPanel] Saved " + savedCount + " grade entries. Final grades recalculated.");

            statusLabel.setText("Successfully saved " + savedCount + " grade entries and updated finals!");
            statusLabel.setForeground(new Color(22, 163, 74));

        } catch (ApiException e) {
            System.err.println("[GradebookPanel] Error saving grades: " + e.getMessage());
            statusLabel.setText("Error saving: " + e.getMessage());
            statusLabel.setForeground(Color.RED);
        }
    }
}
