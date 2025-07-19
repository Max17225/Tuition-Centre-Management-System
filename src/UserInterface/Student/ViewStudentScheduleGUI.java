/**
 * ViewStudentScheduleGUI.java
 * 
 * This JFrame displays the weekly class schedule for a logged-in student.
 * 
 * Features:
 * - Fetches and organizes the student’s enrolled subjects by weekday
 * - Displays class times and subject names grouped under each day
 * - Supports vertical scrolling for usability
 * - Provides a "Back" button to return to the StudentGUI
 * 
 * Visuals:
 * - Colored headers for each day (filled with [48,188,237], white font)
 * - Subject entries shown below with a neutral gray color
 * 
 * Data Source:
 * - Uses StudentService#getStudentWeeklySchedule to retrieve schedule mappings
 * 
 * Used In:
 * - Launched from StudentGUI → scheduleButtonActionPerformed
 * 
 * Dependencies:
 * - DataModel: Student
 * - Service: StudentService
 * - Util: WindowUtils
 */

package UserInterface.Student;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.awt.event.ActionEvent;
import DataModel.Student;
import Service.StudentService;
import Util.WindowUtils;

public class ViewStudentScheduleGUI extends JFrame {
    
     /**
     * Constructs the GUI for displaying the student's weekly schedule.
     *
     * @param student The student whose schedule will be displayed.
     */
    public ViewStudentScheduleGUI(Student student) {
        setTitle("My Weekly Schedule");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        WindowUtils.centerWindow(this); // Center window on screen using the WindowUtils package
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(235, 245, 238)); // Light green background

        // ---------------------------- Top Panel ----------------------------
        // Header section with title text
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(45, 118, 232));

        JLabel titleLabel = new JLabel("My Weekly Schedule", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setIcon(new ImageIcon(getClass().getResource("/images/schedulelogo.png")));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // ---------------------------- Weekly Schedule ----------------------------
        // Main content panel for days and their respective classes
        JPanel weekPanel = new JPanel();
        weekPanel.setLayout(new BoxLayout(weekPanel, BoxLayout.Y_AXIS));
        weekPanel.setBackground(new Color(235, 245, 238));

        // Retrieve the schedule mapped by day
        Map<String, List<String>> scheduleMap = StudentService.getStudentWeeklySchedule(student);
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        // Loop through each day of the week and display classes
        for (String day : days) {
            // Outer container for each day (includes both header and entries)
            JPanel dayPanel = new JPanel(new BorderLayout());
            dayPanel.setBackground(Color.WHITE);
            dayPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            dayPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

            // --- Day Header ---
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            headerPanel.setBackground(new Color(48, 188, 237));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            JLabel dayLabel = new JLabel(day);
            dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            dayLabel.setForeground(Color.WHITE);
            headerPanel.add(dayLabel);

            // --- Subject Entries Panel ---
            JPanel subjectPanel = new JPanel();
            subjectPanel.setLayout(new BoxLayout(subjectPanel, BoxLayout.Y_AXIS));
            subjectPanel.setBackground(Color.WHITE);

            List<String> entries = scheduleMap.get(day);
            if (entries == null || entries.isEmpty()) {
                // Display message if no classes are scheduled
                JLabel noneLabel = new JLabel("No classes scheduled.");
                noneLabel.setForeground(new Color(120, 120, 120));
                noneLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                subjectPanel.add(noneLabel);
            } else {
                // Sort classes by starting time for better visual order
                entries.sort(Comparator.comparingInt(this::parseStartTime));
                for (String entry : entries) {
                    JLabel subLabel = new JLabel(entry);
                    subLabel.setForeground(new Color(50, 50, 50));
                    subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    subjectPanel.add(subLabel);
                }
            }

            // --- Combine panels ---
            dayPanel.add(headerPanel, BorderLayout.NORTH);
            dayPanel.add(subjectPanel, BorderLayout.CENTER);
            
            // Add spacing and day panel to the weekly container
            weekPanel.add(Box.createVerticalStrut(10));
            weekPanel.add(dayPanel);
        }

        // Wrap the entire week panel in a scroll pane for usability
        JScrollPane scrollPane = new JScrollPane(weekPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(235, 245, 238));
        add(scrollPane, BorderLayout.CENTER);

        // ---------------------------- Bottom Panel (Back Button) ----------------------------
        // Navigation panel with a back button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(235, 245, 238));

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setBackground(new Color(255,255,255));
        backBtn.setForeground(new Color(0,0,0));
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Return to the main Student GUI on click
        backBtn.addActionListener((ActionEvent e) -> {
            new StudentGUI(student).setVisible(true);
            dispose();
        });

        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Parses the start time from a time range string like "0900-1100".
     * 
     * @param timeRange The string representing the class time range.
     * @return The starting time as an integer (e.g., "0900" -> 900), or Integer.MAX_VALUE if invalid.
     */
    private int parseStartTime(String timeRange) {
        try {
            String start = timeRange.split("-")[0];
            return Integer.parseInt(start);
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }
}
