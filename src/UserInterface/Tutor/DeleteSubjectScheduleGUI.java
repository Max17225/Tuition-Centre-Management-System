/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Tutor;

import DataModel.*;
import Service.TutorService;
import Util.DataManager;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DeleteSubjectScheduleGUI extends JFrame {
    private final String tutorId;
    private JComboBox<Subject> subjectComboBox;
    private JTextArea scheduleDisplay;
    
    // Use this to launch GUI if tutor was empty status, it will display empty status page
    public static void launch(String tutorId) {
        List<ClassSchedule> mySchedule = TutorService.getMyClassSchedule(tutorId);
        
        if (mySchedule.isEmpty()) {
            ComponentFactory.showEmptyStatusFrame("DeleteSubjectSchedule", tutorId, "No Schedule Yet");

        } else {
            new DeleteSubjectScheduleGUI(tutorId).setVisible(true);
        }
    }
    
    // Constructor
    public DeleteSubjectScheduleGUI(String tutorId) {
        this.tutorId = tutorId;
        
        // -------------------------------------------------------------------------------------- Frame setting
        setTitle("Delete Subject Schedule");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(102, 102, 102));
        
        // -------- Component Adding here ------------
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
    }
    
    // ------------------------------------------------------- Private Methods ------------------------------
    
    // ----------- Create header panel -----------
    private JPanel createHeaderPanel() {
        JPanel panel = ComponentFactory.createTopTitlePanel("Delete Subject Schedule");
        return panel;
    }
    
    // ----------- Create main panel ------------
    private JPanel createMainPanel() {
        // ----------- Create center panel ------------
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(40, 40, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        
        // ----------- Select Subject Lebel ------------
        JLabel selectLabel = new JLabel("Select Subject:");
        selectLabel.setForeground(Color.WHITE);
        selectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        selectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // ------------ ComboBox for choose subject -------------
        subjectComboBox = new JComboBox<>();
        subjectComboBox.setPreferredSize(new Dimension(300, 30));
        subjectComboBox.setMaximumSize(new Dimension(300, 40));
        subjectComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subjectComboBox.setBackground(Color.WHITE);
        subjectComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        subjectComboBox.addActionListener(e -> updateScheduleDisplay());
        
        // -------------- TextArea for display Schedule of choosen subject ----------
        scheduleDisplay = new JTextArea();
        scheduleDisplay.setFont(new Font("Monospaced", Font.PLAIN, 14));
        scheduleDisplay.setEditable(false);
        scheduleDisplay.setBackground(new Color(30, 30, 30));
        scheduleDisplay.setForeground(Color.WHITE);
        scheduleDisplay.setMargin(new Insets(20, 20, 20, 20));
        scheduleDisplay.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // -------------- ScrollPanel: If schedule too much, user can scholl ------------
        JScrollPane scrollPane = new JScrollPane(scheduleDisplay);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        scrollPane.setMaximumSize(new Dimension(400, 150));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // ---------------------------------------------------------------------------------------- bottom button
        JButton deleteButton = ComponentFactory.createRoundedButton("Delete", new Color(200, 100, 100));
        JButton backButton = ComponentFactory.createRoundedButton("Back", new Color(40, 160, 100));
        
        // ------------------- ActionListener of bottom button ------------------------
        deleteButton.addActionListener(e -> {
            handleDelete();
        });
        
        backButton.addActionListener(e -> {
            new TutorMainGUI(tutorId).setVisible(true);
            dispose();
        });

        // Put Button at bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10)); 
        buttonPanel.setOpaque(false); // Set panel background invisible
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        add(Box.createVerticalStrut(20)); // Make a distance 

        // Add components to panel
        panel.add(selectLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subjectComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(buttonPanel);

        // Load subject data
        loadSubjects();

        return panel;
    }
    
    // load Subject comboBox subject
    private void loadSubjects() {
        subjectComboBox.removeAllItems(); // Reset comboBox
        List<Subject> subjects = TutorService.getMySubject(tutorId);

        for (Subject s : subjects) {
            subjectComboBox.addItem(s);
        }

        if (!subjects.isEmpty()) {
            subjectComboBox.setSelectedIndex(0);
            updateScheduleDisplay();
        } else {
            scheduleDisplay.setText("No subjects found.");
        }
    }
    
    // Display schedule on text field
    private void updateScheduleDisplay() {
        Subject selected = (Subject) subjectComboBox.getSelectedItem();
        if (selected != null) {
            ClassSchedule target = null;
            for (ClassSchedule sch : DataManager.of(ClassSchedule.class).readFromFile()) {
                if (sch.getSubjectId().equals(selected.getId())) {
                    target = sch;
                    break;
                }
            }

            if (target == null || target.getScheduleInWeek() == null || target.getScheduleInWeek().isEmpty()) {
                scheduleDisplay.setText("No schedule assigned.");
            } else {
                StringBuilder sb = new StringBuilder();
                String[] scheduleItems = target.getScheduleInWeek().split("\\|");
                for (String item : scheduleItems) {
                    sb.append(item.trim()).append("\n");
                }
                scheduleDisplay.setText(sb.toString());
            }
        }
    }
    
    // Handle delete operation 
    private void handleDelete() {
        Subject selected = (Subject) subjectComboBox.getSelectedItem();
        if (selected == null) return;
        
        // Show a window to make confirmation
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the schedule for " + selected.getSubjectName() + "?",
                "Confirm Deletion",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        // if user choose OK this will run
        if (confirm == JOptionPane.OK_OPTION) {
            List<ClassSchedule> allSchedules = DataManager.of(ClassSchedule.class).readFromFile();
            boolean removed = allSchedules.removeIf(sch -> sch.getSubjectId().equals(selected.getId()));

            if (removed) {
                DataManager.of(ClassSchedule.class).overwriteFile(allSchedules);
                JOptionPane.showMessageDialog(this,
                        "Schedule deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                updateScheduleDisplay();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No schedule found to delete.",
                        "Not Found",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}





