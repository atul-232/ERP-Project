package edu.univ.erp.ui.admin;

import edu.univ.erp.api.ApiException;
import edu.univ.erp.api.admin.AdminAPI;
import edu.univ.erp.domain.Instructor;
import edu.univ.erp.domain.Student;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class UserManagementPanel extends JPanel {

    private final AdminAPI adminAPI;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private List<Student> allStudents = new ArrayList<>();
    private List<Instructor> allInstructors = new ArrayList<>();

    public UserManagementPanel() {
        this.adminAPI = new AdminAPI();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 35));
        searchField.putClientProperty("JComponent.roundRect", true);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable(searchField.getText());
            }
        });

        JButton addUserBtn = new JButton("+ Add User");
        addUserBtn.setBackground(new Color(37, 99, 235));
        addUserBtn.setForeground(Color.WHITE);
        addUserBtn.setFocusPainted(false);
        addUserBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addUserBtn.addActionListener(e -> showAddUserDialog());

        headerPanel.add(searchField, BorderLayout.WEST);
        headerPanel.add(addUserBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 0, 0, 0),
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1)
        ));

        JLabel tableTitle = new JLabel(" User Management");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(new Color(30, 41, 59));
        tableTitle.setBorder(new EmptyBorder(15, 20, 15, 20));
        tableContainer.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {"User ID", "Name", "Email/Username", "Role", "Status", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setRowHeight(50);
        userTable.setShowVerticalLines(false);
        userTable.setIntercellSpacing(new Dimension(0, 0));
        userTable.setSelectionBackground(new Color(241, 245, 249));
        userTable.setSelectionForeground(Color.BLACK);
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        userTable.getTableHeader().setBackground(new Color(241, 245, 249));
        userTable.getTableHeader().setPreferredSize(new Dimension(0, 40));

        userTable.getColumnModel().getColumn(3).setCellRenderer(new RoleRenderer());
        userTable.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
        userTable.getColumnModel().getColumn(5).setCellRenderer(new ActionButtonRenderer());
        userTable.getColumnModel().getColumn(5).setCellEditor(new ActionButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        add(tableContainer, BorderLayout.CENTER);

        statusLabel = new JLabel(" ");
        statusLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void loadData() {
        try {
            allInstructors = adminAPI.getAllInstructors();
            allStudents = adminAPI.getAllStudents();
            filterTable("");
        } catch (ApiException e) {
            statusLabel.setText("Error: " + e.getMessage());
            statusLabel.setForeground(Color.RED);
        }
    }

    private void filterTable(String query) {
        tableModel.setRowCount(0);
        String lowerQuery = query.toLowerCase();

        for (Instructor i : allInstructors) {
            String userIdStr = String.valueOf(i.getUserId());
            String formattedId = "I" + userIdStr;
            if (userIdStr.contains(lowerQuery) || formattedId.toLowerCase().contains(lowerQuery)) {
                tableModel.addRow(new Object[]{formattedId, i.getFullName(), "ID: " + i.getUserId(), "Instructor", "Active", "ACTIONS"});
            }
        }

        for (Student s : allStudents) {
            String userIdStr = String.valueOf(s.getUserId());
            String formattedId = "S" + userIdStr;
            if (userIdStr.contains(lowerQuery) || formattedId.toLowerCase().contains(lowerQuery)) {
                tableModel.addRow(new Object[]{formattedId, s.getFullName(), s.getRollNo(), "Student", "Active", "ACTIONS"});
            }
        }
    }

    private void handleEdit(int row) {
        String userIdStr = (String) tableModel.getValueAt(row, 0);
        String role = (String) tableModel.getValueAt(row, 3);
        int userId = Integer.parseInt(userIdStr.substring(1));

        if ("Student".equalsIgnoreCase(role)) {
            Student student = allStudents.stream().filter(s -> s.getUserId() == userId).findFirst().orElse(null);
            if (student != null) showEditStudentDialog(student);
        } else if ("Instructor".equalsIgnoreCase(role)) {
            Instructor instructor = allInstructors.stream().filter(i -> i.getUserId() == userId).findFirst().orElse(null);
            if (instructor != null) showEditInstructorDialog(instructor);
        }
    }

    private void handleDelete(int row) {
        String userIdStr = (String) tableModel.getValueAt(row, 0);
        int userId = Integer.parseInt(userIdStr.substring(1));

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to PERMANENTLY delete user " + userIdStr + "?\nThis will remove all related data.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                adminAPI.deleteUser(userId);
                JOptionPane.showMessageDialog(this, "User deleted successfully.");
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage());
            }
        }
    }

    private void showEditStudentDialog(Student student) {
        JTextField nameField = new JTextField(student.getFullName());
        JTextField rollField = new JTextField(student.getRollNo());
        JTextField progField = new JTextField(student.getProgram());
        JTextField yearField = new JTextField(String.valueOf(student.getYear()));

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Full Name:")); panel.add(nameField);
        panel.add(new JLabel("Roll No:")); panel.add(rollField);
        panel.add(new JLabel("Program:")); panel.add(progField);
        panel.add(new JLabel("Year:")); panel.add(yearField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Student Details",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                student.setFullName(nameField.getText());
                student.setRollNo(rollField.getText());
                student.setProgram(progField.getText());
                student.setYear(Integer.parseInt(yearField.getText()));
                adminAPI.updateStudent(student);
                JOptionPane.showMessageDialog(this, "Student updated successfully.");
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error updating: " + e.getMessage());
            }
        }
    }

    private void showEditInstructorDialog(Instructor instructor) {
        JTextField nameField = new JTextField(instructor.getFullName());
        JTextField deptField = new JTextField(instructor.getDepartment());

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Full Name:")); panel.add(nameField);
        panel.add(new JLabel("Department:")); panel.add(deptField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Instructor Details",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                instructor.setFullName(nameField.getText());
                instructor.setDepartment(deptField.getText());
                adminAPI.updateInstructor(instructor);
                JOptionPane.showMessageDialog(this, "Instructor updated successfully.");
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error updating: " + e.getMessage());
            }
        }
    }

    private void showAddUserDialog() {
        String[] options = {"Student", "Instructor"};
        int choice = JOptionPane.showOptionDialog(this, "Select User Type to Add", "Add User",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 0) showAddStudentForm();
        else if (choice == 1) showAddInstructorForm();
    }

    private void showAddStudentForm() {
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JTextField nameField = new JTextField();
        JTextField rollField = new JTextField();
        JTextField progField = new JTextField();
        JTextField yearField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Username:")); panel.add(userField);
        panel.add(new JLabel("Password:")); panel.add(passField);
        panel.add(new JLabel("Full Name:")); panel.add(nameField);
        panel.add(new JLabel("Roll No:")); panel.add(rollField);
        panel.add(new JLabel("Program:")); panel.add(progField);
        panel.add(new JLabel("Year:")); panel.add(yearField);

        int result = JOptionPane.showConfirmDialog(this, panel, "New Student", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Student s = new Student();
                s.setFullName(nameField.getText());
                s.setRollNo(rollField.getText());
                s.setProgram(progField.getText());
                s.setYear(Integer.parseInt(yearField.getText()));
                adminAPI.addStudentUser(userField.getText(), new String(passField.getPassword()), s);
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void showAddInstructorForm() {
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JTextField nameField = new JTextField();
        JTextField deptField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Username:")); panel.add(userField);
        panel.add(new JLabel("Password:")); panel.add(passField);
        panel.add(new JLabel("Full Name:")); panel.add(nameField);
        panel.add(new JLabel("Department:")); panel.add(deptField);

        int result = JOptionPane.showConfirmDialog(this, panel, "New Instructor", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Instructor i = new Instructor();
                i.setFullName(nameField.getText());
                i.setDepartment(deptField.getText());
                adminAPI.addInstructorUser(userField.getText(), new String(passField.getPassword()), i);
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    class RoleRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            l.setHorizontalAlignment(CENTER);
            String role = (String) value;
            if ("Instructor".equals(role)) l.setForeground(new Color(22, 163, 74));
            else l.setForeground(Color.GRAY);
            l.setFont(new Font("Segoe UI", Font.BOLD, 12));
            return l;
        }
    }

    class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            l.setHorizontalAlignment(CENTER);
            l.setForeground(new Color(22, 163, 74));
            l.setFont(new Font("Segoe UI", Font.BOLD, 12));
            return l;
        }
    }

    class ActionButtonRenderer implements TableCellRenderer {
        private final ActionPanel panel = new ActionPanel();
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return panel;
        }
    }

    class ActionButtonEditor extends DefaultCellEditor {
        private final ActionPanel panel = new ActionPanel();
        private int currentRow;

        public ActionButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel.editBtn.addActionListener(e -> {
                fireEditingStopped();
                handleEdit(currentRow);
            });
            panel.delBtn.addActionListener(e -> {
                fireEditingStopped();
                handleDelete(currentRow);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    class ActionPanel extends JPanel {
        public final JButton editBtn = new JButton("✎");
        public final JButton delBtn = new JButton("🗑");

        public ActionPanel() {
            setOpaque(true);
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editBtn.setBorderPainted(false);
            editBtn.setContentAreaFilled(false);
            editBtn.setForeground(new Color(37, 99, 235));
            delBtn.setBorderPainted(false);
            delBtn.setContentAreaFilled(false);
            delBtn.setForeground(new Color(220, 38, 38));
            add(editBtn);
            add(delBtn);
        }
    }
}
