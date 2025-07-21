/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Util.*;
import java.util.List;
import DataModel.Receptionist;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nengz
 */
public class ReceptionistAccountManagementGUI extends JFrame {
    private final DefaultTableModel model; // Table model to manage receptionist data

    public ReceptionistAccountManagementGUI(String adminId) {
        setTitle("Receptionist Account Management");
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0, 128, 128)); // Background color

        // ------------------------ Top Panel ------------------------
        JPanel topPanel = UserInterface.Admin.ComponentFactory.createTopTitlePanel("Receptionist Account");
        add(topPanel, BorderLayout.NORTH);

        // ------------------------ Bottom Panel (Back button) ------------------------
        JPanel bottomPanel = UserInterface.Admin.ComponentFactory.createBottomButtonPanel(this, adminId);
        add(bottomPanel, BorderLayout.SOUTH);

        // ------------------------ Center Panel (Table + Buttons) ------------------------
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(20, 30, 45));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40)); // padding

        // ------------------------ Table Setup ------------------------
        List<Receptionist> allRecp = DataManager.of(Receptionist.class).readFromFile();
        String[] columns = {"ID", "Username", "Phone", "Email", "Country"};
        model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        // Fill table with data
        for (Receptionist r : allRecp) {
            model.addRow(new Object[]{ r.getId(), r.getUsername(), r.getPhoneNumber(), r.getEmail(), r.getCountry() });
        }

        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(40, 50, 60));

        // Header styling
        table.getTableHeader().setBackground(new Color(30, 40, 55));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        scrollPane.getViewport().setBackground(new Color(20, 30, 45));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        centerPanel.add(scrollPane);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // spacing

        // ------------------------ Buttons ------------------------
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(20, 30, 45));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));

        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
        Color btnColor = new Color(100, 255, 180);

        JButton registerBtn = new JButton("Register New Receptionist");
        JButton deleteBtn = new JButton("Delete Receptionist Account");
        JButton changePwBtn = new JButton("Change Password");

        // Style buttons
        for (JButton btn : new JButton[]{registerBtn, deleteBtn, changePwBtn}) {
            btn.setPreferredSize(new Dimension(200, 40));
            btn.setFont(btnFont);
            btn.setBackground(Color.DARK_GRAY);
            btn.setForeground(btnColor);
            btn.setFocusPainted(false); // No focus rectangle
            btn.setBorderPainted(false); // No border
            btn.setBorder(BorderFactory.createLineBorder(btnColor)); // Colored border
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Pointer cursor
        }

        buttonPanel.add(registerBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(changePwBtn);

        // ------------------------ Button Actions ------------------------

        // Open Register GUI
        registerBtn.addActionListener(e -> {
            new RegisterReceptionistGUI(adminId).setVisible(true);
            dispose(); // Close current frame
        });

        // Delete selected receptionist
        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String recepId = (String) table.getValueAt(selectedRow, 0); // ID is in column 0

                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete Receptionist ID: " + recepId + "?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    DataManager.of(Receptionist.class).deleteById(recepId);
                    JOptionPane.showMessageDialog(null, "Receptionist deleted.");
                    refreshTable(); // Update UI after delete
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a receptionist to delete.");
            }
        });

        // Change selected receptionist's password
        changePwBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String recepId = (String) table.getValueAt(selectedRow, 0);
                new changeReceptionistPasswordDialog(this, recepId); // open dialog
            } else {
                JOptionPane.showMessageDialog(null, "Please select a receptionist to change password.");
            }
        });

        centerPanel.add(buttonPanel);
        add(centerPanel, BorderLayout.CENTER); // add center content
    }

    // ------------------------ Refresh table content ------------------------
    private void refreshTable() {
        model.setRowCount(0); // Clear existing table rows
        List<Receptionist> updatedList = DataManager.of(Receptionist.class).readFromFile();

        for (Receptionist r : updatedList) {
            model.addRow(new Object[]{ r.getId(), r.getUsername(), r.getPhoneNumber(), r.getEmail(), r.getCountry() });
        }
    }

    // ------------------------ Password Change Dialog ------------------------
    public class changeReceptionistPasswordDialog extends JDialog {
        public changeReceptionistPasswordDialog(JFrame parent, String recepId) {
            super(parent, "Change Password", true);
            setSize(350, 180);
            setLocationRelativeTo(parent);
            setLayout(new GridBagLayout());

            JLabel label = new JLabel("New Password:");
            JPasswordField passwordField = new JPasswordField(15);
            JButton confirmBtn = new JButton("Confirm");
            JLabel messageLabel = new JLabel(" ");

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(label, gbc);

            gbc.gridx = 1;
            add(passwordField, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            add(confirmBtn, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(messageLabel, gbc);

            // Confirm password change
            confirmBtn.addActionListener(e -> {
                String newPassword = new String(passwordField.getPassword()).trim();
                if (newPassword.isEmpty()) {
                    messageLabel.setText("Password cannot be empty.");
                    messageLabel.setForeground(Color.RED);
                    return;
                }

                Receptionist targetRec = DataManager.of(Receptionist.class).getRecordById(recepId);
                targetRec.setPassword(newPassword);
                DataManager.of(Receptionist.class).updateRecord(targetRec);

                messageLabel.setText("Password updated.");
                messageLabel.setForeground(new Color(0, 128, 0));

                // Auto-close after 1 second
                Timer timer = new Timer(1000, evt -> dispose());
                timer.setRepeats(false);
                timer.start();
            });

            setVisible(true);
        }
    }
}

