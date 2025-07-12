package UserInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class AdminUI {
    private final ArrayList<Tutor> tutors = new ArrayList<>();
    private final ArrayList<Receptionist> receptionists = new ArrayList<>();

    // GUI components as instance variables
    private JTextField tName, tEmail, tLevel, tSubject;
    private DefaultListModel<String> tutorListModel;

    private JTextField rName, rEmail;
    private DefaultListModel<String> receptionistListModel;

    private JTextField uName, uEmail;
    private JLabel updatedLabel;

    public JPanel getAdminPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Tutor Panel
        JPanel tutorPanel = new JPanel(new GridLayout(6, 2));
        tName = new JTextField();
        tEmail = new JTextField();
        tLevel = new JTextField();
        tSubject = new JTextField();
        JButton registerTutor = new JButton("Register Tutor");
        JButton deleteTutor = new JButton("Delete Tutor");
        tutorListModel = new DefaultListModel<>();
        JList<String> tutorList = new JList<>(tutorListModel);

        registerTutor.addActionListener((ActionEvent e) -> {
            Tutor tutor = new Tutor(
                    tName.getText(),
                    tEmail.getText(),
                    tLevel.getText(),
                    tSubject.getText(),
                    3000.00  // or use a JTextField if you want it input by user
            );
            
            tutors.add(tutor);
            tutorListModel.addElement(tutor.toString());
            tName.setText(""); tEmail.setText(""); tLevel.setText(""); tSubject.setText("");
        });

        deleteTutor.addActionListener(e -> {
            int index = tutorList.getSelectedIndex();
            if (index >= 0) {
                tutors.remove(index);
                tutorListModel.remove(index);
            }
        });

        tutorPanel.add(new JLabel("Tutor ID:")); tutorPanel.add(tName);
        tutorPanel.add(new JLabel("Email:")); tutorPanel.add(tEmail);
        tutorPanel.add(new JLabel("Level:")); tutorPanel.add(tLevel);
        tutorPanel.add(new JLabel("Subject:")); tutorPanel.add(tSubject);
        tutorPanel.add(registerTutor); tutorPanel.add(deleteTutor);
        tutorPanel.add(new JScrollPane(tutorList));

        // Receptionist Panel
        JPanel receptionistPanel = new JPanel(new GridLayout(5, 2));
        rName = new JTextField();
        rEmail = new JTextField();
        JButton registerReceptionist = new JButton("Register Receptionist");
        JButton deleteReceptionist = new JButton("Delete Receptionist");
        receptionistListModel = new DefaultListModel<>();
        JList<String> receptionistList = new JList<>(receptionistListModel);

        registerReceptionist.addActionListener((ActionEvent e) -> {
            Receptionist r = new Receptionist(
                    rName.getText(),
                    rEmail.getText(),
                    2500.00  // fixed income, or get from input
            );
            
            receptionists.add(r);
            receptionistListModel.addElement(r.toString());
            rName.setText(""); rEmail.setText("");
        });

        deleteReceptionist.addActionListener(e -> {
            int index = receptionistList.getSelectedIndex();
            if (index >= 0) {
                receptionists.remove(index);
                receptionistListModel.remove(index);
            }
        });

        receptionistPanel.add(new JLabel("Receptionist ID:")); receptionistPanel.add(rName);
        receptionistPanel.add(new JLabel("Email:")); receptionistPanel.add(rEmail);
        receptionistPanel.add(registerReceptionist); receptionistPanel.add(deleteReceptionist);
        receptionistPanel.add(new JScrollPane(receptionistList));

        // Income Report Panel
        JPanel incomePanel = new JPanel(new BorderLayout());
        JButton viewIncome = new JButton("View Income Report");

        viewIncome.addActionListener(e -> {
            Map<String, Integer> incomeMap = new HashMap<>();
            for (Tutor t : tutors) {
                String key = t.level + " - " + t.subject;
                incomeMap.put(key, incomeMap.getOrDefault(key, 0) + 500);
            }

            System.out.println("=== Income Report ===");
            incomeMap.forEach((k, v) -> System.out.println(k + ": RM" + v));
        });

        incomePanel.add(viewIncome, BorderLayout.NORTH);

        // Profile Panel
        JPanel profilePanel = new JPanel(new GridLayout(4, 2));
        uName = new JTextField("John Doe");
        uEmail = new JTextField("john@example.com");
        JButton updateProfile = new JButton("Update Profile");
        updatedLabel = new JLabel("Profile not updated");

        updateProfile.addActionListener(e -> {
            updatedLabel.setText("Updated: " + uName.getText() + " | " + uEmail.getText());
        });

        profilePanel.add(new JLabel("Your Name:")); profilePanel.add(uName);
        profilePanel.add(new JLabel("Your Email:")); profilePanel.add(uEmail);
        profilePanel.add(updateProfile); profilePanel.add(updatedLabel);

        tabbedPane.add("Tutors", tutorPanel);
        tabbedPane.add("Receptionists", receptionistPanel);
        tabbedPane.add("Income Report", incomePanel);
        tabbedPane.add("My Profile", profilePanel);

        JPanel container = new JPanel(new BorderLayout());
        container.add(tabbedPane, BorderLayout.CENTER);
        return container;
    }


    // Inner classes for Tutor and Receptionist
    class User {
        protected String name, email;
        protected double monthlyIncome;

        public User(String name, String email, double income) {
            this.name = name;
            this.email = email;
            this.monthlyIncome = monthlyIncome;
        }

        public void updateProfile(String name, String email) {
            this.name = name;
            this.email = email;
        }
        
        public void promote(double increment) {
            this.monthlyIncome += increment;
        }
        
        public String getIncomeInfo() {
            return "RM " + monthlyIncome;
        }
    }

    class Tutor extends User {
        String level, subject;

        public Tutor(String name, String email, String level, String subject, double income) {
            super(name, email, income);
            this.level = level;
            this.subject = subject;
        }

        public void promote(double increment, String newLevel) {
            super.promote(increment);
            this.level = newLevel;
        }

        @Override
        public String toString() {
            return name + " - " + level + " - " + subject + " - " + getIncomeInfo();
        }
    }

    class Receptionist extends User {
        public Receptionist(String name, String email, double income) {
            super(name, email, income);
        }

        @Override
        public String toString() {
            return name + " - " + email + " - " + getIncomeInfo();
        }
    }

    // Add main method here to run AdminUI directly
    public static void main(String[] args) {
        JFrame frame;
        frame = new JFrame("Admin UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        AdminUI adminUI = new AdminUI();
        JPanel panel = adminUI.getAdminPanel(); // ✅ use the updated method
        frame.add(panel);
        frame.setVisible(true);
    }
}
