/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Admin;

import Service.AdminService;
import DataModel.Receptionist;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterReceptionistGUI extends JFrame {

    private final JTextField usernameField;
    private final JTextField phoneField;
    private final JTextField emailField;
    private final JTextField countryField;
    private final JPasswordField passwordField;
    private final JLabel messageLabel;

    // ----------------------- GUI Constructor ------------------------
    public RegisterReceptionistGUI(String adminId) {
        setTitle("Register New Receptionist"); // Set window title
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
        // Username Field
        JLabel usernameLabel = createLabel("Username:");
        usernameField = createTextField();
        addField(centerPanel, gbc, 0, usernameLabel, usernameField);

        // Password Field
        JLabel passwordLabel = createLabel("Password:");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250, 30));
        addField(centerPanel, gbc, 1, passwordLabel, passwordField);

        // Phone Field
        JLabel phoneLabel = createLabel("Phone:");
        phoneField = createTextField();
        addField(centerPanel, gbc, 2, phoneLabel, phoneField);

        // Email Field
        JLabel emailLabel = createLabel("Email:");
        emailField = createTextField();
        addField(centerPanel, gbc, 3, emailLabel, emailField);

        // Country Field
        JLabel countryLabel = createLabel("Country:");
        countryField = createTextField();
        addField(centerPanel, gbc, 4, countryLabel, countryField);

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
        registerBtn.addActionListener((ActionEvent e) -> registerReceptionist());

        setVisible(true); // Show window
    }

    // ----------------------- Utility Methods ------------------------

    // Create styled label
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    // Create styled text field
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(250, 30));
        return field;
    }

    // Add label + field to form
    private void addField(JPanel panel, GridBagConstraints gbc, int row, JLabel label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // ----------------------- Registration Logic ------------------------
    private void registerReceptionist() {
        // Get values from form
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String country = countryField.getText().trim();

        // Validate all fields
        if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || email.isEmpty() || country.isEmpty()) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        // Validate email format
        if (!Util.InputValidator.emailFormatIsValid(email)) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Invalid Email Format.");
            return;
        }

        // Generate new ID and call service
        String newId = Util.IdGenerator.getNewId(Receptionist.class);
        AdminService.registerReceptionist(username, password, phone, email, country);

        // Show success message and reset form
        messageLabel.setForeground(Color.GREEN);
        messageLabel.setText("Receptionist registered successfully! New ID is " + newId + ".");
        clearForm();
    }

    // Clear all input fields
    private void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        phoneField.setText("");
        emailField.setText("");
        countryField.setText("");
    }
}

