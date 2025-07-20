package UserInterface.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import DataModel.Tutor;
import Service.AdminService;
import Util.WindowUtils;

public class TutorAccountManagementGUI extends JFrame{
    private final JTable tutorTable;
    private final DefaultTableModel model;
    
    public TutorAccountManagementGUI() {
        setTitle("Tutor Account Management");
        setLayout(new BorderLayout());
        
        // Top Panel
        add(ComponentFactory.createTopPanel("Tutor Account Management"), BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Name", "Username", "Email", "Phone"}, 0);
        tutorTable = new JTable(model);
        refreshTutorTable();
        add(new JScrollPane(tutorTable), BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton registerBtn = new JButton("Register Tutor");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton changePwdBtn = new JButton("Change Password");
        
        registerBtn.addActionListener(e -> registerTutorDialog());
        deleteBtn.addActionListener(e -> deleteSelectedTutor());
        changePwdBtn.addActionListener(e -> changePasswordDialog());
        
        buttonPanel.add(registerBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(changePwdBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        WindowUtils.centerWindow(this);
        setVisible(true);
    }
    
    private void refreshTutorTable() {
        model.setRowCount(0);
        for (Tutor t : AdminService.getAllTutors()) {
            model.addRow(new String[]{t.getId(), t.getUsername(), t.getEmail(), t.getPhoneNumber()});
        }
    }

    private void registerTutorDialog() {
        // You can design a RegisterTutorGUI separately
        JOptionPane.showMessageDialog(this, "Redirect to RegisterTutorGUI");
    }
    
    private void deleteSelectedTutor() {
        int row = tutorTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a tutor first.");
            return;
        }

        String tutorId = (String) model.getValueAt(row, 0);
        if (AdminService.deleteTutor(tutorId)) {
            JOptionPane.showMessageDialog(this, "Tutor deleted successfully.");
            refreshTutorTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete tutor.");
        }
    }
    
    private void changePasswordDialog() {
        int row = tutorTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a tutor first.");
            return;
        }

        String tutorId = (String) model.getValueAt(row, 0);
        String newPassword = JOptionPane.showInputDialog(this, "Enter new password:");
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            if (AdminService.changeTutorPassword(tutorId, newPassword)) {
                JOptionPane.showMessageDialog(this, "Password updated.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update password.");
            }
        }
    }
}
