/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Tutor;

import DataModel.*;
import Util.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import Service.TutorService;
import java.util.ArrayList;

/**
 *
 * @author nengz
 */

// Tutor GUI class, can display account info page

public class TutorAccountGUI extends JFrame {
    public TutorAccountGUI(String tutorId) {
        setTitle("My Account");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(102, 102, 102));

        Tutor tutor = DataManager.of(Tutor.class).getRecordById(tutorId);

        // ----------------------------------------------------------------------------------- Top Panel 

        JPanel topPanel = ComponentFactory.createTopTitlePanel("My Account");
        add(topPanel, BorderLayout.NORTH);

        // ------------------------------------------------------------------------ Center Panel with Two Section
        JPanel centerWrapperPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        centerWrapperPanel.setBackground(new Color(40, 40, 40));
        centerWrapperPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // -----------------------------------------------------------------------------------LEFT - Tutor Info 
        JPanel infoPanel = new ComponentFactory.RoundedPanel(20);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(60, 60, 60));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
      
        // Title
        JLabel infoTitle = new JLabel("TUTOR INFO");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        infoTitle.setForeground(Color.WHITE);
        infoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(infoTitle);
        infoPanel.add(Box.createVerticalStrut(15)); // Add a space

        // Info
        Font infoFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color textColor = Color.WHITE;

        JLabel usernameLabel = createInfoLabel("Username: " + tutor.getUsername(), infoFont, textColor);
        JLabel emailLabel = createInfoLabel("Email: " + tutor.getEmail(), infoFont, textColor);
        JLabel phoneLabel = createInfoLabel("Phone: " + tutor.getPhoneNumber(), infoFont, textColor);
        JLabel countryLabel = createInfoLabel("Country: " + tutor.getCountry(), infoFont, textColor);
        JLabel tutorIdLabel = createInfoLabel("Tutor ID: " + tutor.getId(), infoFont, textColor);

        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        phoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        countryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tutorIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(usernameLabel);
        infoPanel.add(emailLabel);
        infoPanel.add(phoneLabel);
        infoPanel.add(countryLabel);
        infoPanel.add(tutorIdLabel);

        // Make a space, Work Like a spring push the top element and bottom element
        infoPanel.add(Box.createVerticalGlue()); 

        // --- Bottom Buttons Panel ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); 
        btnPanel.setOpaque(false); // Remove default background
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT); 

        JButton editBtn = ComponentFactory.createDashboardStyleButton("Edit My Info");
        editBtn.setPreferredSize(new Dimension(140, 40)); 
        editBtn.addActionListener(e -> {
            new EditTutorInfoGUI(tutorId).setVisible(true); // Take to info editor GUI, private GUI below
            dispose();
        });

        JButton changePwdBtn = ComponentFactory.createDashboardStyleButton("Change Password");
        changePwdBtn.setPreferredSize(new Dimension(140, 40));
        changePwdBtn.addActionListener(e -> {
            new ChangePasswordGUI(tutorId).setVisible(true); // Take to password edigor GUI, private GUI below
            dispose();
        });

        btnPanel.add(editBtn);
        btnPanel.add(changePwdBtn);

        // Make sure button at the bottom
        infoPanel.add(Box.createVerticalStrut(40)); 
        infoPanel.add(btnPanel);

        // -----------------------------------------------------------------------------------RIGHT - Subject Info
        JPanel subjectPanel = new ComponentFactory.RoundedPanel(20);
        subjectPanel.setLayout(new BoxLayout(subjectPanel, BoxLayout.Y_AXIS));
        subjectPanel.setBackground(new Color(60, 60, 60));
        subjectPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel subjectTitle = new JLabel("TUTOR SUBJECTS");
        subjectTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        subjectTitle.setForeground(Color.WHITE);
        subjectTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subjectPanel.add(subjectTitle);
        subjectPanel.add(Box.createVerticalStrut(15));

        List<Subject> subjects = TutorService.getMySubject(tutorId);
        List<Subject> sortedList = new ArrayList<>(subjects);
        // Sort the Subject List
        sortedList.sort(
            Comparator.comparing(Subject::getLevel)
                      .thenComparing(Subject::getSubjectName)
        );
        
        // Print all Tutor's subjects
        if (subjects.isEmpty()) {
            JLabel noSub = new JLabel("No subjects assigned.");
            noSub.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            noSub.setForeground(Color.LIGHT_GRAY);
            noSub.setAlignmentX(Component.LEFT_ALIGNMENT);
            subjectPanel.add(noSub);
            
        } else {
            for (Subject sub : sortedList) {
                JLabel subLabel = new JLabel("• " + sub.getSubjectName() + " (Level " + sub.getLevel() + ")");
                subLabel.setFont(infoFont);
                subLabel.setForeground(Color.LIGHT_GRAY);
                subLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                subjectPanel.add(subLabel);
                subjectPanel.add(Box.createVerticalStrut(5));         
            }
        }
        
        centerWrapperPanel.add(infoPanel);
        centerWrapperPanel.add(subjectPanel);
        add(centerWrapperPanel, BorderLayout.CENTER); // Add it in middle of frame

        // --------------------------------------------------------------------------Bottom Panel with Back Button
        JPanel bottomPanel = ComponentFactory.createBottomButtonPanel(this, tutorId);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    // ----------------------------------- Private Class and Method---------------------------------------------
    
    // ----------------------------------------------------------------------------- Info Editor GUI
    private class EditTutorInfoGUI extends JFrame {
        public EditTutorInfoGUI(String tutorId) {
            setTitle("Edit My Info");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            getContentPane().setBackground(new Color(102, 102, 102));
            setLayout(new BorderLayout());

            Tutor tutor = DataManager.of(Tutor.class).getRecordById(tutorId);


            // ---- Title ----
            JPanel title = ComponentFactory.createTopTitlePanel("Profile Editor");
            add(title, BorderLayout.NORTH);

            // ---- Editor ----
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(new Color(40, 40, 40));

            Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
            Color labelColor = Color.WHITE;

            Insets padding = new Insets(10, 20, 10, 20); // Create space

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = padding;
            gbc.fill = GridBagConstraints.HORIZONTAL; // Will make a long text field
            gbc.anchor = GridBagConstraints.WEST; // Start from  left side

            JTextField usernameField = new JTextField(tutor.getUsername());
            JTextField phoneField = new JTextField(tutor.getPhoneNumber());
            JTextField countryField = new JTextField(tutor.getCountry());
            JTextField emailField = new JTextField(tutor.getEmail());

            addFormRow(formPanel, gbc, 0, "Username:", usernameField, labelFont, labelColor);
            addFormRow(formPanel, gbc, 1, "Phone:", phoneField, labelFont, labelColor);
            addFormRow(formPanel, gbc, 2, "Country:", countryField, labelFont, labelColor);
            addFormRow(formPanel, gbc, 3, "Email:", emailField, labelFont, labelColor);

            add(formPanel, BorderLayout.CENTER);

            // ---- Bottom Button -----
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            btnPanel.setBackground(new Color(102, 102, 102));

            JButton saveBtn = ComponentFactory.createDashboardStyleButton("Save");
            JButton backBtn = ComponentFactory.createDashboardStyleButton("Back");
            
            // Go back and not changing anything
            backBtn.addActionListener(e -> {
                new TutorAccountGUI(tutorId).setVisible(true);
                dispose();
            });
            
            // Before save, get all the text from the form
            saveBtn.addActionListener(e -> {
                String username = usernameField.getText().trim();
                String phone = phoneField.getText().trim();
                String country = countryField.getText().trim();
                String email = emailField.getText().trim();

                // Check if email format valid
                if (!InputValidator.emailFormatIsValid(email)) {
                    JOptionPane.showMessageDialog(this,
                        "Invalid email format. Only '@gmail.com' and '@mail.com' are accepted.",
                        "Invalid Email",
                        JOptionPane.ERROR_MESSAGE);
                    return; // Give Warning and stop the process
                }

                // If not problem then reset all data
                tutor.setUsername(username);
                tutor.setPhoneNumber(phone);
                tutor.setCountry(country);
                tutor.setEmail(email);

                List<Tutor> tutors = DataManager.of(Tutor.class).readFromFile();
                for (int i = 0; i < tutors.size(); i++) {
                    if (tutors.get(i).getId().equalsIgnoreCase(tutorId)) {
                        tutors.set(i, tutor);
                        break;
                    }
                }
                DataManager.of(Tutor.class).overwriteFile(tutors);

                JOptionPane.showMessageDialog(this, "Info updated successfully.");
                dispose();
                new TutorAccountGUI(tutorId).setVisible(true);
            });

            btnPanel.add(saveBtn);
            btnPanel.add(backBtn);
            add(btnPanel, BorderLayout.SOUTH);
        }
    }
    
    // ----------------------------------------------------------------------------- Change Password GUI
    private class ChangePasswordGUI extends JFrame {
        public ChangePasswordGUI(String tutorId) {
            setTitle("Change Password");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            getContentPane().setBackground(new Color(102, 102, 102));
            setLayout(new BorderLayout());

            Tutor tutor = DataManager.of(Tutor.class).getRecordById(tutorId);

            // ---------- Title --------------
            JPanel title = ComponentFactory.createTopTitlePanel("Password Editor");
            add(title, BorderLayout.NORTH);

            // --------- Editor(Form) -----------
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(new Color(40, 40, 40));

            Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
            Color labelColor = Color.WHITE;
            Insets padding = new Insets(10, 20, 10, 20);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = padding;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            JPasswordField currentPassField = new JPasswordField();
            JPasswordField newPassField = new JPasswordField();
            JPasswordField confirmPassField = new JPasswordField();

            addFormRow(formPanel, gbc, 0, "Current Password:", currentPassField, labelFont, labelColor);
            addFormRow(formPanel, gbc, 1, "New Password:", newPassField, labelFont, labelColor);
            addFormRow(formPanel, gbc, 2, "Confirm New Password:", confirmPassField, labelFont, labelColor);

            add(formPanel, BorderLayout.CENTER);

            // Bottom Button
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            btnPanel.setBackground(new Color(102, 102, 102));

            JButton changeBtn = ComponentFactory.createDashboardStyleButton("Change");
            JButton backBtn = ComponentFactory.createDashboardStyleButton("Back");

            changeBtn.addActionListener(e -> {
                String current = new String(currentPassField.getPassword()).trim();
                String newPass = new String(newPassField.getPassword()).trim();
                String confirm = new String(confirmPassField.getPassword()).trim();
                
                // A little logic to make sure current password and new password is correct.
                if (!current.equals(tutor.getPassword())) {
                    JOptionPane.showMessageDialog(this, "Current password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (newPass.isEmpty() || confirm.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "New password fields cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!newPass.equals(confirm)) {
                    JOptionPane.showMessageDialog(this, "New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                tutor.setPassword(newPass);
                List<Tutor> tutors = DataManager.of(Tutor.class).readFromFile();
                for (int i = 0; i < tutors.size(); i++) {
                    if (tutors.get(i).getId().equalsIgnoreCase(tutorId)) {
                        tutors.set(i, tutor);
                        break;
                    }
                }
                DataManager.of(Tutor.class).overwriteFile(tutors);

                JOptionPane.showMessageDialog(this, "Password updated successfully.");
                dispose();
                new TutorAccountGUI(tutorId).setVisible(true);
            });

            backBtn.addActionListener(e -> {
                new TutorAccountGUI(tutorId).setVisible(true);
                dispose();
            });

            btnPanel.add(changeBtn);
            btnPanel.add(backBtn);
            add(btnPanel, BorderLayout.SOUTH);
        }
    }
    
    // Private method auto create a label above textfield, and do setting in textField
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field, Font font, Color color) {
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(color);

        field.setFont(font);
        field.setPreferredSize(new Dimension(400, 30));

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }
    
    // Private method to set infoLabel's colour and font
    private JLabel createInfoLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(font);
        label.setForeground(color);
        return label;
        }
}