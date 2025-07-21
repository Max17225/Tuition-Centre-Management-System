/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Admin;

/**
 *
 * @author nengz
 */

import Service.AdminService;
import DataModel.Tutor;
import Util.DataManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RegisterSubjectGUI extends JFrame{
    private final JTextField subjectNameField;
    private final JTextField levelField;
    private final JTextField tutorIdField;
    private final JTextField feePerMonthField;
    private final JLabel messageLabel;
    
    // ----------------------- GUI Constructor ------------------------
    public RegisterSubjectGUI(String adminId) {
        setTitle("Register New Subject"); // Set window title
        setSize(600, 500);                     // Set window size
        setLocationRelativeTo(null);          // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLayout(new BorderLayout());        // Use BorderLayout

        JPanel centerPanel = new JPanel();    // Create center panel
        centerPanel.setBackground(new Color(20, 30, 45));
        centerPanel.setLayout(new GridBagLayout()); // Use grid layout

        GridBagConstraints gbc = new GridBagConstraints(); // Constraints for layout
        gbc.insets = new Insets(10, 10, 10, 10);           // Padding
        gbc.anchor = GridBagConstraints.WEST;              // Left align

        // ----------------------- Form Fields ------------------------
        // subjectName Field
        JLabel subjectNameLabel = ComponentFactory.createLabel("Subject Name:");
        subjectNameField = ComponentFactory.createTextField();
        ComponentFactory.addField(centerPanel, gbc, 0, subjectNameLabel, subjectNameField);

        // level Field
        JLabel levelLabel = ComponentFactory.createLabel("Level:");
        levelField = new JTextField(2);
        ComponentFactory.addField(centerPanel, gbc, 1, levelLabel, levelField);

        // tutorId Field
        JLabel tutorIdLabel = ComponentFactory.createLabel("Tutor Id:");
        tutorIdField = ComponentFactory.createTextField();
        ComponentFactory.addField(centerPanel, gbc, 2, tutorIdLabel, tutorIdField);

        // fee Field
        JLabel feePerMonthLabel = ComponentFactory.createLabel("Fee Per Month:");
        feePerMonthField = ComponentFactory.createTextField();
        ComponentFactory.addField(centerPanel, gbc, 3, feePerMonthLabel, feePerMonthField);

        // ----------------------- Register Button ------------------------
        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(new Color(0, 153, 153));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false); // Remove dotted focus border
        registerBtn.setPreferredSize(new Dimension(200, 40));

        gbc.gridx = 1;
        gbc.gridy = 5;
        centerPanel.add(registerBtn, gbc); // Add button to panel

        // ----------------------- Message Label ------------------------
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.GREEN);       // Success text
        messageLabel.setOpaque(true);                  // Required for background
        messageLabel.setBackground(new Color(0, 128, 128));

        // ----------------------- Bottom Panel ------------------------
        JPanel bottomPanel = UserInterface.Admin.ComponentFactory.createBottomButtonPanel(this, adminId);

        add(bottomPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.NORTH);

        // Register button logic
        registerBtn.addActionListener((ActionEvent e) -> registerSubject());

        setVisible(true); // Show window
    }
    
    // ----------------------- Registration Logic ------------------------
    private void registerSubject() {
        // Get values from form
        String subjectName = subjectNameField.getText().trim();
        String level = levelField.getText().trim();
        String tutorId = tutorIdField.getText().trim();
        String feePerMonth = feePerMonthField.getText().trim();

        // Validate all fields
        if (subjectName.isEmpty() || level.isEmpty() || tutorId.isEmpty() || feePerMonth.isEmpty()) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        // Validate level format
        if (!Util.InputValidator.isValidLevel(level)) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Invalid level Format.(L1 - L5)");
            return;
        }
        
        // Validate Tutor Id
        List<DataModel.Tutor> alltutor = DataManager.of(Tutor.class).readFromFile();
        Set<String> allTutorId = alltutor.stream()
            .map(t -> t.getId())
            .collect(Collectors.toSet());
        
        if (!allTutorId.contains(tutorId)) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Tutor ID Not Found");
            return; 
        }
        
        // Validate Fee
        boolean feeIsNumber;
        try {
            Integer.valueOf(feePerMonth);
            feeIsNumber = true;
        } catch(NumberFormatException e) {
            feeIsNumber = false;
        }
        
        if (!feeIsNumber) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Invalid Fee Format(Number Only)");
            return;
        }


        AdminService.assignSubjectToTutor(tutorId, subjectName, level,feePerMonth);

        // Show success message and reset form
        messageLabel.setForeground(Color.GREEN);
        messageLabel.setText("Subject register successfully!");
        clearForm();
    }

    // Clear all input fields
    private void clearForm() {
        subjectNameField.setText("");
        levelField.setText("");
        tutorIdField.setText("");
        feePerMonthField.setText("");
    }
    
}
