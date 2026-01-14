package edu.univ.erp.ui.admin;

import edu.univ.erp.api.admin.AdminAPI;
import edu.univ.erp.api.catalog.CatalogAPI;
import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Instructor;
import edu.univ.erp.domain.Section;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class CourseManagementPanel extends JPanel {

    private final AdminAPI adminAPI;
    private final CatalogAPI catalogAPI;
    private JTextField codeField;
    private JTextField titleField;
    private JSpinner creditsSpinner;
    private JComboBox<String> departmentCombo;
    private JLabel formStatusLabel;
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JComboBox<Course> courseComboBox;
    private JComboBox<Instructor> instructorComboBox;
    private JLabel sectionStatusLabel;

    public CourseManagementPanel() {
        this.adminAPI = new AdminAPI();
        this.catalogAPI = new CatalogAPI();
        initComponents();
        loadTableData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(new EmptyBorder(20, 30, 20, 30));
        tabbedPane.addTab("Course Management", createCourseManagerLayout());
        tabbedPane.addTab("Create Section", createSectionPanel());
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) refreshSectionPanelData();
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createCourseManagerLayout() {
        JPanel panel = new JPanel(new BorderLayout(30, 0));
        panel.setOpaque(false);
        panel.add(createAddCourseCard(), BorderLayout.WEST);
        panel.add(createCourseTableCard(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAddCourseCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(350, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel title = new JLabel("Add Course");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(37, 99, 235));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        codeField = createStyledTextField();
        titleField = createStyledTextField();
        creditsSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
        departmentCombo = new JComboBox<>(new String[]{"Computer Science", "Design", "Bio", "Social Science", "Electronics"});

        JButton addBtn = new JButton("Add Course");
        addBtn.setBackground(new Color(37, 99, 235));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setFocusPainted(false);
        addBtn.setMaximumSize(new Dimension(400, 40));
        addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addBtn.addActionListener(e -> handleAddCourse());

        formStatusLabel = new JLabel(" ");
        formStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        addInput(card, "Course Code", codeField);
        addInput(card, "Title", titleField);

        card.add(createFieldLabel("Credits"));
        JPanel spinPanel = new JPanel(new BorderLayout());
        spinPanel.setMaximumSize(new Dimension(400, 35));
        spinPanel.add(creditsSpinner);
        spinPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(spinPanel);

        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(createFieldLabel("Department"));

        JPanel comboPanel = new JPanel(new BorderLayout());
        comboPanel.setMaximumSize(new Dimension(400, 35));
        departmentCombo.setBackground(Color.WHITE);
        comboPanel.add(departmentCombo);
        comboPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(comboPanel);

        JButton btn = new JButton("Add Course");
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(addBtn);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(formStatusLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private JPanel createCourseTableCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel title = new JLabel("Existing Courses");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(37, 99, 235));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));

        String[] cols = {"ID", "Code", "Title", "Credits", "Department", "Actions"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setRowHeight(50);
        courseTable.setShowVerticalLines(false);
        courseTable.setIntercellSpacing(new Dimension(0, 0));
        courseTable.setSelectionBackground(new Color(241, 245, 249));
        courseTable.setSelectionForeground(Color.BLACK);

        JTableHeader header = courseTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(0, 40));

        courseTable.getColumnModel().getColumn(0).setMinWidth(0);
        courseTable.getColumnModel().getColumn(0).setMaxWidth(0);
        courseTable.getColumnModel().getColumn(0).setWidth(0);

        courseTable.getColumnModel().getColumn(5).setCellRenderer(new ActionRenderer());
        courseTable.getColumnModel().getColumn(5).setCellEditor(new ActionEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(courseTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        card.add(title, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private void loadTableData() {
        try {
            System.out.println("[LOG] Loading course table data");
            tableModel.setRowCount(0);
            List<Course> courses = catalogAPI.getAllCourses();
            for (Course c : courses) {
                String dept = (c.getDepartment() != null) ? c.getDepartment() : "N/A";
                tableModel.addRow(new Object[]{c.getCourseId(), c.getCourseCode(), c.getTitle(), c.getCredits(), dept, "Edit / Delete"});
            }
        } catch (Exception e) {
            System.out.println("[ERROR] loadTableData -> " + e.getMessage());
        }
    }

    private void handleAddCourse() {
        try {
            System.out.println("[LOG] handleAddCourse() called");
            Course c = new Course();
            c.setCourseCode(codeField.getText());
            c.setTitle(titleField.getText());
            c.setCredits((Integer) creditsSpinner.getValue());
            c.setDepartment((String) departmentCombo.getSelectedItem());

            System.out.println("[LOG] New Course => " + c.getCourseCode() + ", " + c.getTitle());
            String msg = adminAPI.createCourse(c);

            formStatusLabel.setText("Success! " + msg);
            formStatusLabel.setForeground(new Color(22, 163, 74));
            loadTableData();

            codeField.setText("");
            titleField.setText("");

        } catch (Exception e) {
            System.out.println("[ERROR] handleAddCourse -> " + e.getMessage());
            formStatusLabel.setText("Error: " + e.getMessage());
            formStatusLabel.setForeground(Color.RED);
        }
    }

    private void handleDeleteCourse(int row) {
        System.out.println("[LOG] handleDeleteCourse() row=" + row);
        int courseId = (int) tableModel.getValueAt(row, 0);
        String courseName = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete " + courseName + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                System.out.println("[LOG] Deleting course => ID=" + courseId);
                adminAPI.deleteCourse(courseId);
                loadTableData();
                JOptionPane.showMessageDialog(this, "Course deleted successfully.");
            } catch (Exception e) {
                System.out.println("[ERROR] handleDeleteCourse -> " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Error deleting course: " + e.getMessage());
            }
        } else {
            System.out.println("[LOG] Delete cancelled");
        }
    }

    private void handleEditCourse(int row) {
        System.out.println("[LOG] handleEditCourse() row=" + row);
        int courseId = (int) tableModel.getValueAt(row, 0);
        String currentCode = (String) tableModel.getValueAt(row, 1);
        String currentTitle = (String) tableModel.getValueAt(row, 2);
        int currentCredits = (int) tableModel.getValueAt(row, 3);
        String currentDept = (String) tableModel.getValueAt(row, 4);

        JTextField editCode = new JTextField(currentCode);
        JTextField editTitle = new JTextField(currentTitle);
        JSpinner editCredits = new JSpinner(new SpinnerNumberModel(currentCredits, 1, 10, 1));
        JComboBox<String> editDept = new JComboBox<>(new String[]{"Computer Science", "Design", "Bio", "Social Science", "Electronics"});
        editDept.setSelectedItem(currentDept);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Code:")); panel.add(editCode);
        panel.add(new JLabel("Title:")); panel.add(editTitle);
        panel.add(new JLabel("Credits:")); panel.add(editCredits);
        panel.add(new JLabel("Department:")); panel.add(editDept);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Course", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Course updated = new Course(courseId, editCode.getText(), editTitle.getText(),
                        (int) editCredits.getValue(), (String) editDept.getSelectedItem());

                System.out.println("[LOG] Updating Course => " + updated);
                adminAPI.updateCourse(updated);
                loadTableData();
                JOptionPane.showMessageDialog(this, "Course updated successfully.");
            } catch (Exception e) {
                System.out.println("[ERROR] handleEditCourse -> " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Error updating course: " + e.getMessage());
            }
        } else {
            System.out.println("[LOG] Edit cancelled");
        }
    }

    private JPanel createSectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setPreferredSize(new Dimension(500, 550));

        JLabel title = new JLabel("Schedule New Section");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        courseComboBox = new JComboBox<>();
        instructorComboBox = new JComboBox<>();
        JTextField dayTimeField = createStyledTextField();
        JTextField roomField = createStyledTextField();
        JTextField capacityField = createStyledTextField();
        JTextField semesterField = createStyledTextField();
        JTextField yearField = createStyledTextField();
        JButton createBtn = new JButton("Create Section");
        sectionStatusLabel = new JLabel(" ");

        addInput(card, "Select Course", courseComboBox);
        addInput(card, "Select Instructor", instructorComboBox);
        addInput(card, "Day/Time (e.g., MWF 10-11)", dayTimeField);
        addInput(card, "Room", roomField);
        addInput(card, "Capacity", capacityField);
        addInput(card, "Semester", semesterField);
        addInput(card, "Year", yearField);

        createBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        createBtn.setBackground(new Color(37, 99, 235));
        createBtn.setForeground(Color.WHITE);

        createBtn.addActionListener(e -> {
            System.out.println("[LOG] Section creation triggered");
            try {
                Course selCourse = (Course) courseComboBox.getSelectedItem();
                Instructor selInst = (Instructor) instructorComboBox.getSelectedItem();

                Section s = new Section();
                s.setCourseId(selCourse.getCourseId());
                s.setInstructorId(selInst.getInstructorId());
                s.setDayTime(dayTimeField.getText());
                s.setRoom(roomField.getText());
                s.setCapacity(Integer.parseInt(capacityField.getText()));
                s.setSemester(semesterField.getText());
                s.setYear(Integer.parseInt(yearField.getText()));

                System.out.println("[LOG] New Section => " + s);
                String msg = adminAPI.createSection(s);

                sectionStatusLabel.setText(msg);
                sectionStatusLabel.setForeground(new Color(22, 163, 74));
            } catch (Exception ex) {
                System.out.println("[ERROR] Section Create -> " + ex.getMessage());
                sectionStatusLabel.setText("Error: " + ex.getMessage());
                sectionStatusLabel.setForeground(Color.RED);
            }
        });

        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(createBtn);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(sectionStatusLabel);

        panel.add(card);
        return panel;
    }

    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setMaximumSize(new Dimension(400, 35));
        tf.setPreferredSize(new Dimension(400, 35));
        tf.putClientProperty("JComponent.roundRect", true);
        return tf;
    }

    private JLabel createFieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(100, 116, 139));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void addInput(JPanel p, String label, JComponent c) {
        p.add(createFieldLabel(label));
        p.add(Box.createRigidArea(new Dimension(0, 5)));
        c.setMaximumSize(new Dimension(400, 35));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (c instanceof JComboBox) c.setBackground(Color.WHITE);
        p.add(c);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void refreshSectionPanelData() {
        try {
            System.out.println("[LOG] Refresh section panel data");
            courseComboBox.removeAllItems();
            instructorComboBox.removeAllItems();
            catalogAPI.getAllCourses().forEach(courseComboBox::addItem);
            adminAPI.getAllInstructors().forEach(instructorComboBox::addItem);
        } catch (Exception e) {
            System.out.println("[ERROR] refreshSectionPanelData -> " + e.getMessage());
        }
    }

    class ActionRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            panel.setOpaque(true);
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);

            JButton editBtn = new JButton("Edit");
            editBtn.setBackground(new Color(37, 99, 235));
            editBtn.setForeground(Color.WHITE);
            JButton delBtn = new JButton("Delete");
            delBtn.setBackground(new Color(220, 38, 38));
            delBtn.setForeground(Color.WHITE);

            panel.add(editBtn);
            panel.add(delBtn);
            return panel;
        }
    }

    class ActionEditor extends DefaultCellEditor {
        private JButton editBtn, delBtn;
        private int currentRow;

        public ActionEditor(JCheckBox checkBox) {
            super(checkBox);
            editBtn = new JButton("Edit");
            delBtn = new JButton("Delete");

            editBtn.addActionListener(e -> {
                fireEditingStopped();
                handleEditCourse(currentRow);
            });
            delBtn.addActionListener(e -> {
                fireEditingStopped();
                handleDeleteCourse(currentRow);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            panel.setBackground(table.getSelectionBackground());
            editBtn.setBackground(new Color(37, 99, 235));
            editBtn.setForeground(Color.WHITE);
            delBtn.setBackground(new Color(220, 38, 38));
            delBtn.setForeground(Color.WHITE);
            panel.add(editBtn);
            panel.add(delBtn);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}