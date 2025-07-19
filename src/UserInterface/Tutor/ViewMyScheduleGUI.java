/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Tutor;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import DataModel.Subject;
import Service.TutorService;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

// Tutor GUI class, can display tutor's schedule.

public class ViewMyScheduleGUI extends JFrame {
    public ViewMyScheduleGUI(String tutorId) {
        
        // ------------------------------------------------------------------------------------ Frame setting
        setTitle("My Weekly Schedule");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ------------------------------------------------------------------------------------- Title(Top Panel)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 30, 30));
        
        JLabel titleLabel = new JLabel("My Weekly Schedule", SwingConstants.CENTER);
        titleLabel.setFont(new Font("MV Boli", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 53, 97));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);

        // ----------------------------------------------------------------------------------- Scrollable Weekly Panel 
        JPanel weekPanel = new JPanel();
        weekPanel.setBackground(new Color(30, 30, 30));
        weekPanel.setLayout(new BoxLayout(weekPanel, BoxLayout.Y_AXIS));
        
        // Get tutor's week schedule
        Map<String, Map<Subject, List<String>>> scheduleMap = TutorService.getMyWeekScheduleMap(tutorId);
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for (String day : days) {
            ComponentFactory.RoundedPanel dayPanel = new ComponentFactory.RoundedPanel(20);
            dayPanel.setLayout(new BorderLayout());
            dayPanel.setBackground(new Color(45, 45, 45));
            dayPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            dayPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            JLabel dayLabel = new JLabel(day);
            dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            dayLabel.setForeground(new Color(102, 102, 102));
            dayPanel.add(dayLabel, BorderLayout.NORTH);

            JPanel subjectPanel = new JPanel();
            subjectPanel.setBackground(new Color(45, 45, 45));
            subjectPanel.setLayout(new BoxLayout(subjectPanel, BoxLayout.Y_AXIS));

            Map<Subject, List<String>> subjects = scheduleMap.get(day);
            if (subjects == null || subjects.isEmpty()) {
                JLabel noneLabel = new JLabel("No classes scheduled.");
                noneLabel.setForeground(Color.LIGHT_GRAY);
                noneLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                subjectPanel.add(noneLabel);
            } else {
                // Flatten Subject + Time list into a new list of (Subject, Time) pair
                List<Map.Entry<Subject, String>> flattenedList = new ArrayList<>();
                for (Map.Entry<Subject, List<String>> entry : subjects.entrySet()) {
                    Subject subject = entry.getKey();
                    for (String time : entry.getValue()) {
                        flattenedList.add(Map.entry(subject, time));
                    }
                }

                // Sort by time
                flattenedList.sort(Comparator.comparingInt(entry -> parseStartTime(entry.getValue())));

                for (Map.Entry<Subject, String> entry : flattenedList) {
                    Subject subject = entry.getKey();
                    String time = entry.getValue();

                    JLabel subLabel = new JLabel(subject.getSubjectName() + " (" + time + ") - Level " + subject.getLevel());
                    subLabel.setForeground(Color.WHITE);
                    subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    subjectPanel.add(subLabel);
                }
            }

            dayPanel.add(subjectPanel, BorderLayout.CENTER);
            weekPanel.add(Box.createVerticalStrut(10));
            weekPanel.add(dayPanel);
        }

        JScrollPane scrollPane = new JScrollPane(weekPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);


        // ---------- Bottom Back Button ----------
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(30, 30, 30));
   

        JButton backBtn = ComponentFactory.createRoundedButton("Back", new Color(40, 160, 100));

        backBtn.addActionListener((ActionEvent e) -> {
            new TutorMainGUI(tutorId).setVisible(true);
            dispose();
        });

        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    // ------------------------------------- Private Method------------------------------------------------------
    
    // To parse the time like("0900-1100" → 90)
    private int parseStartTime(String timeRange) {
        try {
            String start = timeRange.split("-")[0];
            return Integer.parseInt(start);
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE; 
        }
    }

}

