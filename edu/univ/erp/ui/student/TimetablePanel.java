package edu.univ.erp.ui.student;

import edu.univ.erp.api.student.StudentAPI;
import edu.univ.erp.auth.Session;
import edu.univ.erp.service.TimetableEntryDTO;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class TimetablePanel extends JPanel {

    private final Session userSession;
    private final StudentAPI studentAPI;
    private JPanel gridPanel;

    private final String[] TIMES = {"8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM"};
    private final String[] DAYS = {"Mon", "Tue", "Wed", "Thu", "Fri"};

    private final Map<String, Color> courseColors = new HashMap<>();
    private final Color[] PALETTE = {
            new Color(37, 99, 235),
            new Color(16, 185, 129),
            new Color(139, 92, 246),
            new Color(245, 158, 11),
            new Color(236, 72, 153)
    };
    private int colorIndex = 0;

    public TimetablePanel(Session session) {
        System.out.println("[TimetablePanel] Constructor called");
        this.userSession = session;
        this.studentAPI = new StudentAPI();
        initComponents();
        loadTimetable();
    }

    private void initComponents() {
        System.out.println("[TimetablePanel] Initializing UI components");
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Student Timetable");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 41, 59));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        JComboBox<String> semesterCombo = new JComboBox<>(new String[]{"Semester: Fall 2025", "Semester: Spring 2026"});
        semesterCombo.setBackground(Color.WHITE);

        JButton printBtn = new JButton("Print / Download");
        printBtn.setBackground(new Color(37, 99, 235));
        printBtn.setForeground(Color.WHITE);
        printBtn.setFocusPainted(false);
        printBtn.addActionListener(e -> {
            System.out.println("[TimetablePanel] Download button clicked");
            handleDownload();
        });

        actionPanel.add(semesterCombo);
        actionPanel.add(printBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(actionPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(new LineBorder(new Color(226, 232, 240), 1));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadTimetable() {
        System.out.println("[TimetablePanel] Loading timetable from API");
        gridPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.ipady = 20;

        gbc.gridx = 0;
        gbc.weightx = 0.2;
        gridPanel.add(createHeaderCell(""), gbc);

        for (int i = 0; i < DAYS.length; i++) {
            gbc.gridx = i + 1;
            gbc.weightx = 1.0;
            gridPanel.add(createHeaderCell(DAYS[i]), gbc);
        }

        for (int i = 0; i < TIMES.length; i++) {
            gbc.gridy = i + 1;
            gbc.ipady = 60;

            gbc.gridx = 0;
            gbc.weightx = 0.2;
            gridPanel.add(createTimeCell(TIMES[i]), gbc);

            for (int j = 0; j < DAYS.length; j++) {
                gbc.gridx = j + 1;
                gbc.weightx = 1.0;

                JPanel cell = new JPanel(new BorderLayout());
                cell.setBackground(Color.WHITE);
                cell.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, new Color(241, 245, 249)));
                cell.setName(DAYS[j] + "_" + TIMES[i]);
                gridPanel.add(cell, gbc);
            }
        }

        try {
            List<TimetableEntryDTO> entries = studentAPI.getMyTimetable(userSession.getProfileId());
            System.out.println("[TimetablePanel] Timetable entries found: " + entries.size());
            for (TimetableEntryDTO entry : entries) addCourseCard(entry);
        } catch (Exception e) {
            System.err.println("[TimetablePanel] Error loading timetable: " + e.getMessage());
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void addCourseCard(TimetableEntryDTO entry) {
        System.out.println("[TimetablePanel] Placing card for course: " + entry.getCourseCode() + " at " + entry.getDayTime());
        String raw = entry.getDayTime();
        // Remove AM/PM to prevent false positive matching of M in AM/PM for Monday
        String cleanRaw = raw.replaceAll("(?i)\\b[AP]M\\b", "");

        for (String day : DAYS) {
            if (cleanRaw.contains(day) || 
                (day.equals("Mon") && cleanRaw.contains("M")) || 
                (day.equals("Wed") && cleanRaw.contains("W")) || 
                (day.equals("Fri") && cleanRaw.contains("F"))) {
                for (String time : TIMES) {
                    String hour = time.split(":")[0];
                    if (matchesHour(raw, hour)) placeCardInCell(day + "_" + time, entry);
                }
            }
        }
    }

    private boolean matchesHour(String rawTime, String hour) {
        // Match the exact hour (e.g. "1" matches "1" or "1:00" but not "11" or "10")
        return rawTime.matches(".*(?<!\\d)" + hour + "(?!\\d).*");
    }

    private void placeCardInCell(String cellName, TimetableEntryDTO entry) {
        for (Component comp : gridPanel.getComponents()) {
            if (cellName.equals(comp.getName()) && comp instanceof JPanel) {
                JPanel cell = (JPanel) comp;
                cell.removeAll();

                if (!courseColors.containsKey(entry.getCourseCode())) {
                    courseColors.put(entry.getCourseCode(), PALETTE[colorIndex % PALETTE.length]);
                    colorIndex++;
                }
                Color cardColor = courseColors.get(entry.getCourseCode());

                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBackground(cardColor);
                card.setBorder(new EmptyBorder(5, 8, 5, 8));

                JLabel code = new JLabel(entry.getCourseCode());
                code.setFont(new Font("Segoe UI", Font.BOLD, 12));
                code.setForeground(Color.WHITE);

                JLabel title = new JLabel(entry.getCourseTitle());
                title.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                title.setForeground(Color.WHITE);

                JLabel room = new JLabel(entry.getRoom());
                room.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                room.setForeground(new Color(255, 255, 255, 200));

                card.add(code);
                card.add(title);
                card.add(room);

                JPanel marginWrapper = new JPanel(new BorderLayout());
                marginWrapper.setOpaque(false);
                marginWrapper.setBorder(new EmptyBorder(2, 2, 2, 2));
                marginWrapper.add(card, BorderLayout.CENTER);

                cell.add(marginWrapper, BorderLayout.CENTER);
                cell.revalidate();

                System.out.println("[TimetablePanel] Card placed in cell: " + cellName);
            }
        }
    }

    private JPanel createHeaderCell(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(226, 232, 240)));
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(71, 85, 105));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTimeCell(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(241, 245, 249)));
        JLabel label = new JLabel(text + "  ", SwingConstants.RIGHT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(Color.GRAY);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void handleDownload() {
        System.out.println("[TimetablePanel] Export timetable initiated");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Timetable");
        fileChooser.setSelectedFile(new File("timetable_" + userSession.getUsername() + ".csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            System.out.println("[TimetablePanel] Export cancelled by user");
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileToSave))) {
            writer.println("Course Code,Course Title,Time,Room,Instructor");

            List<TimetableEntryDTO> entries = studentAPI.getMyTimetable(userSession.getProfileId());
            System.out.println("[TimetablePanel] Writing " + entries.size() + " entries to file");

            for (TimetableEntryDTO entry : entries) {
                String line = String.format("%s,\"%s\",\"%s\",\"%s\",\"%s\"",
                        entry.getCourseCode(),
                        entry.getCourseTitle(),
                        entry.getDayTime(),
                        entry.getRoom(),
                        entry.getInstructorName()
                );
                writer.println(line);
            }

            JOptionPane.showMessageDialog(this, "Timetable saved to " + fileToSave.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("[TimetablePanel] Export successful → " + fileToSave.getAbsolutePath());

        } catch (IOException ex) {
            System.err.println("[TimetablePanel] Error saving timetable file: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            System.err.println("[TimetablePanel] Error fetching data: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error retrieving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
