/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Tutor;

/**
 *
 * @author nengz
 */

import DataModel.Subject;
import Service.TutorService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UpdateSubjectFeeGUI extends JFrame {
    private JComboBox<Subject> subjectComboBox;
    private JTextField feeField;
    private JLabel currentFeeLabel;
    
    // Use this to launch GUI if tutor was empty status, it will display empty status page
    public static void launch(String tutorId) {
        List<Subject> mySubjects = TutorService.getMySubject(tutorId);
        if (mySubjects.isEmpty()) {
            ComponentFactory.showEmptyStatusFrame("Update Subject Fee", tutorId, "No Subject Registered, Please register with Admin");
        } else {
            new UpdateSubjectFeeGUI(tutorId).setVisible(true);
        }
    }
    
    // Constructor
    public UpdateSubjectFeeGUI(String tutorId) {
        List<Subject> mySubjects = TutorService.getMySubject(tutorId);
        
        // ---------------------------------------------------------------------------- Frame Setting
        setTitle("Update Subject Fee");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(30, 30, 30));
        
        // -------------------------------------------------------------------------------------- Center
        JPanel topPanel = ComponentFactory.createTopTitlePanel("Update Subject Fee");
        add(topPanel, BorderLayout.NORTH);
        
        JPanel bottomPanel = ComponentFactory.createBottomButtonPanel(this, tutorId);
        add(bottomPanel, BorderLayout.SOUTH);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(30, 30, 30));
        contentPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 18);
        Color labelColor = new Color(200, 200, 200);
        Color fieldBg = new Color(50, 50, 50);
        Color fieldFg = Color.WHITE;

        // Subject ComboBox
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(createLabel("Select Subject:", labelFont, labelColor), gbc);
        subjectComboBox = new JComboBox<>(mySubjects.toArray(Subject[]::new));
        subjectComboBox.setFont(labelFont);
        subjectComboBox.setBackground(fieldBg);
        subjectComboBox.setForeground(fieldFg);
        subjectComboBox.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        contentPanel.add(subjectComboBox, gbc);

        // Current fee label
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(createLabel("Current Fee:", labelFont, labelColor), gbc);
        currentFeeLabel = createLabel("", labelFont, Color.GREEN);
        gbc.gridx = 1;
        contentPanel.add(currentFeeLabel, gbc);

        // New fee field
        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(createLabel("New Fee (RM):", labelFont, labelColor), gbc);
        feeField = new JTextField();
        feeField.setFont(labelFont);
        feeField.setBackground(fieldBg);
        feeField.setForeground(fieldFg);
        feeField.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        contentPanel.add(feeField, gbc);

        // Update Button
        JButton updateBtn = ComponentFactory.createDashboardStyleButton("Update");
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        contentPanel.add(updateBtn, gbc);

        add(contentPanel, BorderLayout.CENTER);

        updateFeeLabel(); // show initial fee

        subjectComboBox.addActionListener(e -> updateFeeLabel());
        
        // Update button action listener
        updateBtn.addActionListener((ActionEvent e) -> {
            String newFeeText = feeField.getText().trim();
            if (newFeeText.isEmpty()) {
                JOptionPane.showMessageDialog(UpdateSubjectFeeGUI.this, "Please enter a new fee.");
                return;
            }
            try {
                double newFee = Double.parseDouble(newFeeText);
                if (newFee <= 0) throw new NumberFormatException();
                
                Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
                if (selectedSubject != null) {
                    TutorService.updateSubjectFee(selectedSubject.getId(), newFeeText);
                    JOptionPane.showMessageDialog(UpdateSubjectFeeGUI.this, "Fee updated successfully!");
                    updateFeeLabel();
                    feeField.setText("");
                    new UpdateSubjectFeeGUI(tutorId).setVisible(true);
                    dispose();
                    
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(UpdateSubjectFeeGUI.this, "Invalid fee. Enter a positive number.");
            }
        });

        setVisible(true);
    }
    
    // ----------------------------------------- Private Methods ----------------------------
    
    // Display fee of choosen subject
    private void updateFeeLabel() {
        Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
        if (selectedSubject != null) {
            currentFeeLabel.setText("RM " + selectedSubject.getFeePerMonth());
        }
    }
    
    // Create Label 
    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }
}


