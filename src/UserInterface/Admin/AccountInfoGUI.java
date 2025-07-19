/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Admin;

import DataModel.*;
import Service.AdminService;
import Util.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;


/**
 *
 * @author nengz
 */

// Admin GUI class, can display account info page

public class AccountInfoGUI extends JFrame {
    public AccountInfoGUI(String adminId) {
        setTitle("My Account");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0, 128, 128));

        Admin targetAdmin = DataManager.of(Admin.class).getRecordById(adminId);

        // ----------------------------------------------------------------------------------- Top Panel 

        JPanel topPanel = UserInterface.Admin.ComponentFactory.createTopTitlePanel("My Account");
        add(topPanel, BorderLayout.NORTH);

        // -----------------------------------------------------------------------------------Center - Admin Info 
        JPanel infoPanel = new UserInterface.Admin.ComponentFactory.RoundedPanel(20);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(20, 30, 45));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
      
        // Title
        JLabel infoTitle = new JLabel("Admin INFO");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        infoTitle.setForeground(Color.WHITE);
        infoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(infoTitle);
        infoPanel.add(Box.createVerticalStrut(15)); // Add a space

        // Info
        Font infoFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color textColor = Color.WHITE;

        JLabel usernameLabel = createInfoLabel("Username: " + targetAdmin.getUsername(), infoFont, textColor);
        JLabel emailLabel = createInfoLabel("Email: " + targetAdmin.getEmail(), infoFont, textColor);
        JLabel phoneLabel = createInfoLabel("Phone: " + targetAdmin.getPhoneNumber(), infoFont, textColor);
        JLabel countryLabel = createInfoLabel("Country: " + targetAdmin.getCountry(), infoFont, textColor);
        JLabel adminIdLabel = createInfoLabel("Admin ID: " + targetAdmin.getId(), infoFont, textColor);

        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        phoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        countryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(usernameLabel);
        infoPanel.add(emailLabel);
        infoPanel.add(phoneLabel);
        infoPanel.add(countryLabel);
        infoPanel.add(adminIdLabel);

        // Make a space, Work Like a spring push the top element and bottom element
        infoPanel.add(Box.createVerticalGlue()); 

        // --- Bottom Buttons Panel ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0)); 
        btnPanel.setOpaque(false); // Remove default background
        btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        JButton editBtn = UserInterface.Admin.ComponentFactory.createDashboardStyleButton("Edit My Info");
        editBtn.setPreferredSize(new Dimension(140, 40)); 
        editBtn.addActionListener(e -> {
            new EditAdminInfoGUI(adminId).setVisible(true); // Take to info editor GUI, private GUI below
            dispose();
        });

        JButton changePwdBtn = UserInterface.Admin.ComponentFactory.createDashboardStyleButton("Change Password");
        changePwdBtn.setPreferredSize(new Dimension(140, 40));
        changePwdBtn.addActionListener(e -> {
            new ChangePasswordGUI(adminId).setVisible(true); // Take to password edigor GUI, private GUI below
            dispose();
        });

        btnPanel.add(editBtn);
        btnPanel.add(changePwdBtn);

        // Make sure button at the bottom
        infoPanel.add(Box.createVerticalStrut(40)); 
        infoPanel.add(btnPanel);
        
        // Make info panel look bigger
        infoPanel.setPreferredSize(new Dimension(600, 390));
        
        // Make info Panel start from middle
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerWrapper.setBackground(new Color(0, 128, 128)); // Match background
        centerWrapper.add(infoPanel);
        
        add(centerWrapper, BorderLayout.CENTER);

        
        // --------------------------------------------------------------------------Bottom Panel with Back Button
        JPanel bottomPanel = UserInterface.Admin.ComponentFactory.createBottomButtonPanel(this, adminId);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    // ----------------------------------- Private Class and Method---------------------------------------------
    
    // ----------------------------------------------------------------------------- Info Editor GUI
    private class EditAdminInfoGUI extends JFrame {
        public EditAdminInfoGUI(String adminId) {
            setTitle("Edit My Info");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            getContentPane().setBackground(new Color(0, 128, 128));
            setLayout(new BorderLayout());

            Admin admin = DataManager.of(Admin.class).getRecordById(adminId);


            // ---- Title ----
            JPanel title = UserInterface.Admin.ComponentFactory.createTopTitlePanel("Profile Editor");
            add(title, BorderLayout.NORTH);

            // ---- Editor ----
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(new Color(20, 30, 45));

            Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
            Color labelColor = Color.WHITE;

            Insets padding = new Insets(10, 20, 10, 20); // Create space

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = padding;
            gbc.fill = GridBagConstraints.HORIZONTAL; // Will make a long text field
            gbc.anchor = GridBagConstraints.WEST; // Start from  left side

            JTextField usernameField = new JTextField(admin.getUsername());
            JTextField phoneField = new JTextField(admin.getPhoneNumber());
            JTextField countryField = new JTextField(admin.getCountry());
            JTextField emailField = new JTextField(admin.getEmail());

            addFormRow(formPanel, gbc, 0, "Username:", usernameField, labelFont, labelColor);
            addFormRow(formPanel, gbc, 1, "Phone:", phoneField, labelFont, labelColor);
            addFormRow(formPanel, gbc, 2, "Country:", countryField, labelFont, labelColor);
            addFormRow(formPanel, gbc, 3, "Email:", emailField, labelFont, labelColor);

            add(formPanel, BorderLayout.CENTER);

            // ---- Bottom Button -----
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            btnPanel.setBackground(new Color(0, 128, 128));

            JButton saveBtn = UserInterface.Admin.ComponentFactory.createDashboardStyleButton("Save");
            JButton backBtn = UserInterface.Admin.ComponentFactory.createDashboardStyleButton("Back");
            
            // Go back and not changing anything
            backBtn.addActionListener(e -> {
                new AccountInfoGUI(adminId).setVisible(true);
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
                AdminService.updateAdminProfile(adminId, username, phone, email, country);

                JOptionPane.showMessageDialog(this, "Info updated successfully.");
                dispose();
                new AccountInfoGUI(adminId).setVisible(true);
            });

            btnPanel.add(saveBtn);
            btnPanel.add(backBtn);
            add(btnPanel, BorderLayout.SOUTH);
        }
    }
    
    // ----------------------------------------------------------------------------- Change Password GUI
    private class ChangePasswordGUI extends JFrame {
        public ChangePasswordGUI(String adminId) {
            setTitle("Change Password");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            getContentPane().setBackground(new Color(102, 102, 102));
            setLayout(new BorderLayout());

            Admin admin = DataManager.of(Admin.class).getRecordById(adminId);

            // ---------- Title --------------
            JPanel title = UserInterface.Admin.ComponentFactory.createTopTitlePanel("Password Editor");
            add(title, BorderLayout.NORTH);

            // --------- Editor(Form) -----------
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(new Color(20, 30, 45));

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
            btnPanel.setBackground(new Color(0, 128, 128));

            JButton changeBtn = UserInterface.Admin.ComponentFactory.createDashboardStyleButton("Change");
            JButton backBtn = UserInterface.Admin.ComponentFactory.createDashboardStyleButton("Back");

            changeBtn.addActionListener(e -> {
                String current = new String(currentPassField.getPassword()).trim();
                String newPass = new String(newPassField.getPassword()).trim();
                String confirm = new String(confirmPassField.getPassword()).trim();
                
                // A little logic to make sure current password and new password is correct.
                if (!current.equals(admin.getPassword())) {
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

                admin.setPassword(newPass);
                List<Admin> admins = DataManager.of(Admin.class).readFromFile();
                for (int i = 0; i < admins.size(); i++) {
                    if (admins.get(i).getId().equalsIgnoreCase(adminId)) {
                        admins.set(i, admin);
                        break;
                    }
                }
                DataManager.of(Admin.class).overwriteFile(admins);

                JOptionPane.showMessageDialog(this, "Password updated successfully.");
                dispose();
                new AccountInfoGUI(adminId).setVisible(true);
            });

            backBtn.addActionListener(e -> {
                new AccountInfoGUI(adminId).setVisible(true);
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
