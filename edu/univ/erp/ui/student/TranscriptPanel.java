package edu.univ.erp.ui.student;

import edu.univ.erp.api.ApiException;
import edu.univ.erp.api.student.StudentAPI;
import edu.univ.erp.auth.Session;
import edu.univ.erp.domain.Course;
import edu.univ.erp.util.CSVExporter;
import java.awt.*;
import java.io.File;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TranscriptPanel extends JPanel {
    private final Session userSession;
    private final StudentAPI studentAPI;
    private JLabel nameLabel;
    private JLabel rollLabel;
    private JLabel programLabel;

    public TranscriptPanel(Session session) {
        System.out.println("[TranscriptPanel] Constructor called");
        this.userSession = session;
        this.studentAPI = new StudentAPI();
        initComponents();
        loadStudentProfile();
    }

    private void initComponents() {
        System.out.println("[TranscriptPanel] Initializing UI components");
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);
        mainContent.setMaximumSize(new Dimension(900, 800));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Transcript Download");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 41, 59));

        JButton printBtn = new JButton("Print / Download");
        printBtn.setBackground(new Color(37, 99, 235));
        printBtn.setForeground(Color.WHITE);
        printBtn.setFocusPainted(false);
        printBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printBtn.addActionListener(e -> {
            System.out.println("[TranscriptPanel] Header download button clicked");
            handleDownload();
        });

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(printBtn, BorderLayout.EAST);
        headerPanel.setMaximumSize(new Dimension(2000, 50));

        mainContent.add(headerPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel infoCard = new JPanel(new BorderLayout());
        infoCard.setBackground(Color.WHITE);
        infoCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 41, 59), 2),
                new EmptyBorder(20, 25, 20, 25)
        ));
        infoCard.setMaximumSize(new Dimension(2000, 150));

        JLabel cardTitle = new JLabel("Student Information");
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cardTitle.setForeground(new Color(30, 41, 59));

        JPanel detailsPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        detailsPanel.setOpaque(false);
        nameLabel = createDetailLabel("Name: Loading...");
        rollLabel = createDetailLabel("Roll No: Loading...");
        programLabel = createDetailLabel("Program: Loading...");
        detailsPanel.add(nameLabel);
        detailsPanel.add(rollLabel);
        detailsPanel.add(programLabel);

        infoCard.add(cardTitle, BorderLayout.NORTH);
        infoCard.add(Box.createRigidArea(new Dimension(0, 15)), BorderLayout.CENTER);
        infoCard.add(detailsPanel, BorderLayout.SOUTH);

        mainContent.add(infoCard);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton downloadBtn = new JButton("Download Transcript PDF / CSV");
        downloadBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        downloadBtn.setBackground(new Color(37, 99, 235));
        downloadBtn.setForeground(Color.WHITE);
        downloadBtn.setFocusPainted(false);
        downloadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        downloadBtn.setMaximumSize(new Dimension(2000, 55));
        downloadBtn.addActionListener(e -> {
            System.out.println("[TranscriptPanel] Main download button clicked");
            handleDownload();
        });

        mainContent.add(downloadBtn);
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel historyLabel = new JLabel("Download History / Request Status");
        historyLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        historyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(historyLabel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 10)));

        String[] columns = {"Date", "Status", "Action"};
        Object[][] data = {
                {"Nov 15, 2025", "Completed", "Downloaded (CSV)"},
                {"Oct 20, 2025", "Completed", "Downloaded (CSV)"},
                {"Sept 1, 2025", "Request Pending", "View Request"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable historyTable = new JTable(model);
        historyTable.setRowHeight(45);
        historyTable.setShowVerticalLines(false);
        historyTable.setIntercellSpacing(new Dimension(0, 0));

        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        historyTable.getTableHeader().setBackground(new Color(15, 23, 42));
        historyTable.getTableHeader().setForeground(Color.WHITE);
        historyTable.getTableHeader().setPreferredSize(new Dimension(0, 40));

        historyTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 15, 0, 0));
                if (column == 2 && "View Request".equals(value)) {
                    setForeground(new Color(37, 99, 235));
                } else {
                    setForeground(Color.BLACK);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setMaximumSize(new Dimension(2000, 200));

        mainContent.add(scrollPane);
        add(mainContent, BorderLayout.NORTH);
    }

    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(71, 85, 105));
        return label;
    }

    private void handleDownload() {
        System.out.println("[TranscriptPanel] Transcript export initiated");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Transcript");
        fileChooser.setSelectedFile(new File("transcript_" + userSession.getUsername() + ".csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            System.out.println("[TranscriptPanel] Export cancelled by user");
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        try {
            System.out.println("[TranscriptPanel] Fetching transcript data from API");
            Map<Course, String> transcriptData = studentAPI.getTranscriptData(userSession.getProfileId());
            System.out.println("[TranscriptPanel] Transcript entries returned: " + transcriptData.size());

            CSVExporter.exportTranscript(transcriptData, fileToSave.getAbsolutePath());
            System.out.println("[TranscriptPanel] Export successful → " + fileToSave.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Transcript saved successfully to " + fileToSave.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (ApiException ex) {
            System.err.println("[TranscriptPanel] API error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            System.err.println("[TranscriptPanel] Unexpected error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadStudentProfile() {
        System.out.println("[TranscriptPanel] Loading student profile in background...");
        SwingWorker<edu.univ.erp.domain.Student, Void> worker = new SwingWorker<>() {
            @Override
            protected edu.univ.erp.domain.Student doInBackground() {
                try {
                    return studentAPI.getStudentProfile(userSession.getProfileId());
                } catch (Exception e) {
                    System.err.println("[TranscriptPanel] Error loading profile: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    edu.univ.erp.domain.Student student = get();
                    if (student != null) {
                        nameLabel.setText("Name: " + student.getFullName());
                        rollLabel.setText("Roll No: " + student.getRollNo());
                        programLabel.setText("Program: " + student.getProgram() + " (Year " + student.getYear() + ")");
                        System.out.println("[TranscriptPanel] Profile loaded successfully for " + student.getFullName());
                    } else {
                        nameLabel.setText("Name: " + userSession.getUsername());
                        rollLabel.setText("Roll No: N/A");
                        programLabel.setText("Program: Computer Science");
                    }
                } catch (Exception e) {
                    System.err.println("[TranscriptPanel] Worker exception: " + e.getMessage());
                    nameLabel.setText("Name: " + userSession.getUsername());
                }
            }
        };
        worker.execute();
    }
}
