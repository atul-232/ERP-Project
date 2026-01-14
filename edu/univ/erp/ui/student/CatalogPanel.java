package edu.univ.erp.ui.student;

import edu.univ.erp.api.catalog.CatalogAPI;
import edu.univ.erp.api.student.StudentAPI;
import edu.univ.erp.auth.Session;
import edu.univ.erp.domain.Course;
import edu.univ.erp.service.SectionDetailDTO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class CatalogPanel extends JPanel {

    private final CatalogAPI catalogAPI;
    private final StudentAPI studentAPI;
    private final Session userSession;

    private JTable courseTable;
    private DefaultTableModel tableModel;

    private JTextField searchField;
    private JComboBox<String> deptCombo;
    private JComboBox<String> creditsCombo;

    private JLabel totalValLbl;
    private JLabel newValLbl;
    private JLabel popValLbl;

    public CatalogPanel(Session session) {
        System.out.println("[CatalogPanel] Constructor called");
        this.userSession = session;
        this.catalogAPI = new CatalogAPI();
        this.studentAPI = new StudentAPI();
        initComponents();
        loadData();
    }

    private void initComponents() {
        System.out.println("[CatalogPanel] Initializing UI components");
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBackground(new Color(248, 250, 252));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("Course Catalog");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(30, 41, 59));
        titlePanel.add(title);
        topSection.add(titlePanel);
        topSection.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filterPanel.setOpaque(false);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 35));
        searchField.putClientProperty("JTextField.placeholderText", "Search Courses...");
        filterPanel.add(searchField);

        deptCombo = new JComboBox<>(new String[]{"Department", "Computer Science", "Biology", "English", "Math"});
        creditsCombo = new JComboBox<>(new String[]{"Credits", "1-2", "3-4", "5+"});
        styleCombo(deptCombo);
        styleCombo(creditsCombo);

        filterPanel.add(new JLabel("Filters:"));
        filterPanel.add(deptCombo);
        filterPanel.add(creditsCombo);

        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> {
            System.out.println("[CatalogPanel] Search clicked → Reloading data");
            loadData();
        });
        filterPanel.add(searchBtn);

        topSection.add(filterPanel);
        topSection.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setOpaque(false);

        totalValLbl = new JLabel("0");
        newValLbl = new JLabel("0");
        popValLbl = new JLabel("0");

        cardsPanel.add(createCard("Total Courses", totalValLbl, "📖"));
        cardsPanel.add(createCard("Active Sections", newValLbl, "⭐"));
        cardsPanel.add(createCard("Popular Courses", popValLbl, "🔥"));
        topSection.add(cardsPanel);
        topSection.add(Box.createRigidArea(new Dimension(0, 20)));

        add(topSection, BorderLayout.NORTH);

        String[] columns = {"ID", "Course Code", "Title", "Credits", "Instructor", "Action"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setRowHeight(55);

        courseTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        courseTable.getTableHeader().setBackground(new Color(30, 41, 59));
        courseTable.getTableHeader().setForeground(Color.WHITE);
        courseTable.getColumnModel().getColumn(0).setMinWidth(0);
        courseTable.getColumnModel().getColumn(0).setMaxWidth(0);
        courseTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        courseTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        add(tableContainer, BorderLayout.CENTER);
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setPreferredSize(new Dimension(150, 35));
        combo.setBackground(Color.WHITE);
    }

    private JPanel createCard(String title, JLabel valueLabel, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setForeground(Color.GRAY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(new Color(37, 99, 235));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLbl);
        textPanel.add(valueLabel);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(iconLbl, BorderLayout.EAST);
        return card;
    }

    private void loadData() {
        System.out.println("[CatalogPanel] Loading course data");
        tableModel.setRowCount(0);
        try {
            List<SectionDetailDTO> sections = catalogAPI.getSectionDetailsForCatalog();
            List<Course> allCourses = catalogAPI.getAllCourses();
            System.out.println("[CatalogPanel] Sections fetched: " + sections.size() + ", Courses fetched: " + allCourses.size());

            int totalUniqueCourses = allCourses.size();
            int activeSections = sections.size();
            long popularCount = sections.stream().filter(s -> s.getEnrolledCount() > 0).count();

            totalValLbl.setText(String.valueOf(totalUniqueCourses));
            newValLbl.setText(String.valueOf(activeSections));
            popValLbl.setText(String.valueOf(popularCount));

            String searchText = searchField.getText().toLowerCase();
            for (SectionDetailDTO section : sections) {
                if (!searchText.isEmpty() &&
                        !section.getCourseTitle().toLowerCase().contains(searchText) &&
                        !section.getCourseCode().toLowerCase().contains(searchText)) {
                    continue;
                }

                tableModel.addRow(new Object[]{
                        section.getSectionId(),
                        section.getCourseCode(),
                        section.getCourseTitle(),
                        section.getCredits() + " Credits",
                        section.getInstructorName(),
                        "Enroll"
                });
            }
            System.out.println("[CatalogPanel] Table updated → Rows: " + tableModel.getRowCount());
        } catch (Exception e) {
            System.err.println("[CatalogPanel] Error loading course data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Enroll" : value.toString());
            setForeground(Color.WHITE);
            setBackground(new Color(37, 99, 235));
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int currentSectionId;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setBackground(new Color(37, 99, 235));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "Enroll" : value.toString();
            currentSectionId = Integer.parseInt(table.getValueAt(row, 0).toString());
            isPushed = true;
            System.out.println("[CatalogPanel] Enroll clicked on Section ID: " + currentSectionId);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) handleEnrollment(currentSectionId);
            isPushed = false;
            return label;
        }
    }

    private void handleEnrollment(int sectionId) {
        System.out.println("[CatalogPanel] Processing enrollment for Section ID: " + sectionId);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to enroll in this course?", 
            "Confirm Enrollment", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int studentId = userSession.getProfileId();
                System.out.println("[CatalogPanel] Student ID: " + studentId + " → Submitting enrollment");
                String result = studentAPI.registerForSection(studentId, sectionId);
                System.out.println("[CatalogPanel] Enrollment Response: " + result);
                JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } catch (Exception e) {
                System.err.println("[CatalogPanel] Enrollment failed: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Enrollment Failed", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("[CatalogPanel] Enrollment cancelled by user");
        }
    }
}
