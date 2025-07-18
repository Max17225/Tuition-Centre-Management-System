/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Tutor;

/**
 *
 * @author nengz
 */

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import DataModel.Subject;
import DataModel.Student;
import Service.TutorService;

public class ViewMyStudentGUI extends JFrame {
    
    // Use this to launch GUI if tutor was empty status, it will display empty status page
    public static void launch(String tutorId) {
        Map<Subject, List<Student>> subjectStudentMap = TutorService.getMySubjectStudentMap(tutorId);
        
        if (subjectStudentMap == null || subjectStudentMap.isEmpty()) {
            ComponentFactory.showEmptyStatusFrame("My Subject Student", tutorId, "No subject registered. Please contact admin");
            
        } else {
            new ViewMyStudentGUI(tutorId).setVisible(true);
        }
    }
    
    public ViewMyStudentGUI(String tutorId) {
        setTitle("My Subject Students");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --------------------------------------------------------------------------- Top Title Panel
        JPanel topPanel = ComponentFactory.createTopTitlePanel("My Subject Student");
        add(topPanel, BorderLayout.NORTH);

        // --------------------------------------------------------------------------- Scrollable Subject Panel
        JPanel subjectPanel = new JPanel();
        subjectPanel.setBackground(new Color(30, 30, 30));
        subjectPanel.setLayout(new BoxLayout(subjectPanel, BoxLayout.Y_AXIS));

        Map<Subject, List<Student>> subjectStudentMap = TutorService.getMySubjectStudentMap(tutorId);
        
        // ------------------------------------------------------------------------------ Subject student display
        for (Map.Entry<Subject, List<Student>> entry : subjectStudentMap.entrySet()) {
            Subject subject = entry.getKey();
            List<Student> students = entry.getValue();

            ComponentFactory.RoundedPanel subjectCard = new ComponentFactory.RoundedPanel(20);
            subjectCard.setLayout(new BorderLayout());
            subjectCard.setBackground(new Color(45, 45, 45));
            subjectCard.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            subjectCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

            JLabel subjectLabel = new JLabel(subject.getSubjectName() + " - Level " + subject.getLevel());
            subjectLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            subjectLabel.setForeground(new Color(102, 102, 102));
            subjectCard.add(subjectLabel, BorderLayout.NORTH);

            JPanel studentListPanel = new JPanel();
            studentListPanel.setBackground(new Color(45, 45, 45));
            studentListPanel.setLayout(new BoxLayout(studentListPanel, BoxLayout.Y_AXIS));

            if (students.isEmpty()) {
                JLabel noStudentLabel = new JLabel("No student registered.");
                noStudentLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                noStudentLabel.setForeground(Color.LIGHT_GRAY);
                studentListPanel.add(noStudentLabel);
            } else {
                for (Student student : students) {
                    JLabel studentLabel = new JLabel("- " + student.getUsername() + " (ID: " + student.getId() + ")");
                    studentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    studentLabel.setForeground(Color.WHITE);
                    studentListPanel.add(studentLabel);
                }
            }

            subjectCard.add(studentListPanel, BorderLayout.CENTER);
            subjectPanel.add(Box.createVerticalStrut(10));
            subjectPanel.add(subjectCard);
        }
        
        // Make it scrollable
        JScrollPane scrollPane = new JScrollPane(subjectPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);

        // ---------------------------- Bottom Panel with Back Button
        JPanel bottomPanel = ComponentFactory.createBottomButtonPanel(this, tutorId);
        add(bottomPanel, BorderLayout.SOUTH);
    } 
}


