/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Tutor;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import DataModel.*;
import Service.TutorService;
import java.util.Map;
import Util.*;
        
public class AddSubjectScheduleGUI extends JFrame {

    private final JComboBox<Subject> subjectComboBox;
    private final JComboBox<String> dayComboBox;
    private final JTextField timeField;
    private final JTextArea conflictArea;
    final private String tutorId;
    
    // Use this to launch GUI if tutor was empty status, it will display empty status page
    public static void launch(String tutorId) {
        List<Subject> subjectList = TutorService.getMySubject(tutorId);
        if (subjectList.isEmpty()) {
            ComponentFactory.showEmptyStatusFrame("Add Subject Schedule", tutorId, "No Subject Registered, Please register with Admin");
        } else {
            new AddSubjectScheduleGUI(tutorId).setVisible(true);
        }
    }
    
    // Constructor
    private AddSubjectScheduleGUI(String tutorId) {
        this.tutorId = tutorId;
        
        List<Subject> subjectList = TutorService.getMySubject(tutorId);
        // ----------------------------------------------------------------------------- Frame setting
        setTitle("Add Subject Schedule");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ------------------------------------------------------------------------------ Title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(102, 102, 102));
        
        JLabel titleLabel = new JLabel("Add Subject Schedule", SwingConstants.CENTER);
        titleLabel.setFont(new Font("MV Boli", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 53, 97));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        //--------------------------------------------------------------------------------Center
        Color textColor = Color.WHITE;
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 18);
        
        // -------- Form Panel ----------
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(40, 40, 40));
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // -------- Subject ComboBox ----------
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel subjectLabel = new JLabel("Choose Subject:");
        subjectLabel.setForeground(textColor);
        subjectLabel.setFont(labelFont);
        formPanel.add(subjectLabel, gbc);

        gbc.gridx = 1;
        subjectComboBox = new JComboBox<>(subjectList.toArray(Subject[]::new));
        subjectComboBox.setFont(labelFont);
        subjectComboBox.addActionListener(e -> autoCheckConflict());
        formPanel.add(subjectComboBox, gbc);

        // -------- Day ComboBox --------
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel dayLabel = new JLabel("Choose Day:");
        dayLabel.setForeground(textColor);
        dayLabel.setFont(labelFont);
        formPanel.add(dayLabel, gbc);

        gbc.gridx = 1;
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        dayComboBox = new JComboBox<>(days);
        dayComboBox.setFont(labelFont);
        dayComboBox.addActionListener(e -> autoCheckConflict());
        formPanel.add(dayComboBox, gbc);

        // --------- Time Field ----------
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel timeLabel = new JLabel("Time (HHMM-HHMM):");
        timeLabel.setForeground(textColor);
        timeLabel.setFont(labelFont);
        formPanel.add(timeLabel, gbc);

        gbc.gridx = 1;
        timeField = new JTextField();
        timeField.setFont(labelFont);
        formPanel.add(timeField, gbc);

        // ---------- Conflict Area ----------
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        conflictArea = new JTextArea(7, 30);
        conflictArea.setEditable(false);
        conflictArea.setBackground(new Color(170, 170, 170));
        conflictArea.setForeground(new Color(0, 0, 0));
        conflictArea.setMargin(new Insets(10, 10, 10, 10));
        formPanel.add(conflictArea, gbc);

        add(formPanel, BorderLayout.CENTER);
        

        // -------- Button Panel ----------
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(102, 102, 102));

        JButton checkButton = ComponentFactory.createRoundedButton("Add", new Color(40, 160, 100));
        checkButton.addActionListener(e -> checkConflictAndSave());

        JButton backButton = ComponentFactory.createRoundedButton("Cancel", new Color(230, 100, 100));
        backButton.addActionListener(e -> { new TutorMainGUI(tutorId).setVisible(true); dispose();});

        buttonPanel.add(checkButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    private void checkConflictAndSave() {
        // Collect the data in the combobox and text field and save it into a variable
        Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
        String day = (String) dayComboBox.getSelectedItem();
        String time = timeField.getText().trim();
        
        // if tutor didnt input a time but press the add button this will dispay
        if (time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check if tutor input a valid time format
        if (!Util.InputValidator.isValidTime(time)) {
            JOptionPane.showMessageDialog(this, "Invalid time format. Use HHMM-HHMM like 0900-1100", "Invalid Time", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean hasSameLevelConflict = !TutorService.noTimeConflictInSameLevel(selectedSubject, day, time);
        boolean hasOwnScheduleConflict = !TutorService.noTimeConflictWithOwnSchedule(tutorId, day, time);
        
        // If conflict this will display
        if (hasSameLevelConflict || hasOwnScheduleConflict) {
            JOptionPane.showMessageDialog(
                this,
                "⚠ Conflict found in selected time!",
                "Schedule Conflict",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // If subject have no schedule yet, this will add a new subject
        if (TutorService.subjectHasNoSchedule(selectedSubject)) {
            ClassSchedule newSch = new ClassSchedule(IdGenerator.getNewId(ClassSchedule.class), selectedSubject.getId(), day + ":" + time);
            DataManager<ClassSchedule> schManager = DataManager.of(ClassSchedule.class);
            schManager.appendOne(newSch);
        
        // If subject already got schedule, this will add the new day and time on the record
        } else {
            DataManager<ClassSchedule> schManager = DataManager.of(ClassSchedule.class);
            List<ClassSchedule> allSch = schManager.readFromFile();
            String newDayTimeStr = "|" + day + ":" + time;
            for (ClassSchedule sch : allSch) {
                if (sch.getSubjectId().equals(selectedSubject.getId())) {
                    sch.setScheduleInWeek(sch.getScheduleInWeek() + newDayTimeStr);
                    schManager.updateRecord(sch);
                }
            }
        }

        JOptionPane.showMessageDialog(this, "✔ Schedule added/updated successfully!");
        
        // Reset Page
        subjectComboBox.setSelectedIndex(0);
        dayComboBox.setSelectedIndex(0);
        timeField.setText("");
        conflictArea.setText("");
    }

    
    // This will display some warning in the conflict box
    private void autoCheckConflict() {
        Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
        String selectedDay = (String) dayComboBox.getSelectedItem();

        if (selectedSubject == null || selectedDay == null) {
            conflictArea.setText("Please select a subject and day to see potential conflicts.");
            return;
        }

        StringBuilder result = new StringBuilder();
        result.append("🔍 Potential Conflicts on ").append(selectedDay).append(":\n\n");

        Map<String, Map<Subject, List<String>>> sameLevelScheduleMap = TutorService.getSameLevelDayScheduleMap(selectedSubject);
        Map<Subject, List<String>> sameDaySchedules = sameLevelScheduleMap.get(selectedDay);

        if (sameDaySchedules == null || sameDaySchedules.isEmpty()) {
            result.append("✓ No other classes found on this day for same level.\n");
        } else {
            result.append("⚠ Same Level Classes:\n");
            for (Map.Entry<Subject, List<String>> entry : sameDaySchedules.entrySet()) {
                String subjectName = entry.getKey().getSubjectName();
                List<String> timeSlots = entry.getValue();
                for (String time : timeSlots) {
                    result.append("• ").append(subjectName).append(": ").append(time).append("\n");
                }
            }
        }


        result.append("\n");

        Map<String, Map<Subject, List<String>>> ownScheduleMap = TutorService.getMyWeekScheduleMap(tutorId);
        Map<Subject, List<String>> ownDaySchedules = ownScheduleMap.get(selectedDay);

        if (ownDaySchedules == null || ownDaySchedules.isEmpty()) {
            result.append("✓ No personal classes scheduled on this day.\n");
        } else {
            result.append("⚠ Your Own Classes:\n");
            for (Map.Entry<Subject, List<String>> entry : ownDaySchedules.entrySet()) {
                String subjectName = entry.getKey().getSubjectName();
                List<String> timeSlots = entry.getValue();
                for (String time : timeSlots) {
                    result.append("• ").append(subjectName).append(": ").append(time).append("\n");
                }
            }
        }

        conflictArea.setText(result.toString());
    }
}
