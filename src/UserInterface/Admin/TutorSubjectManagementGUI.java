package UserInterface.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import DataModel.Subject;
import Service.AdminService;
import Util.WindowUtils;

public class TutorSubjectManagementGUI extends JFrame{
    private final JTable subjectTable;
    private final DefaultTableModel model;

    public TutorSubjectManagementGUI() {
        setTitle("Tutor Subject Management");
        setLayout(new BorderLayout());
        
        add(ComponentFactory.createTopPanel("Tutor Subject Management"), BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Subject ID", "Name", "Level", "Tutor ID", "Fee"}, 0);
        subjectTable = new JTable(model);
        refreshSubjectTable();
        add(new JScrollPane(subjectTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton assignBtn = new JButton("Assign Tutor");
        JButton removeBtn = new JButton("Remove Tutor");
        JButton updateFeeBtn = new JButton("Update Fee");
        
        assignBtn.addActionListener(e -> assignTutor());
        removeBtn.addActionListener(e -> removeTutorFromSubject());
        updateFeeBtn.addActionListener(e -> updateSubjectFee());

        buttonPanel.add(assignBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(updateFeeBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        WindowUtils.centerWindow(this);
        setVisible(true);
    }
    
    private void refreshSubjectTable() {
        model.setRowCount(0);
        for (Subject s : Util.DataManager.of(Subject.class).readFromFile()) {
            model.addRow(new String[]{
                s.getId(), s.getSubjectName(), s.getLevel(), s.getTutorId(), String.valueOf(s.getFeePerMonth())
            });
        }
    }

    private void assignTutor() {
        int row = subjectTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a subject first.");
            return;
        }
        
        String subjectId = (String) model.getValueAt(row, 0);
        String tutorId = JOptionPane.showInputDialog(this, "Enter Tutor ID to assign:");
        if (tutorId != null && !tutorId.trim().isEmpty()) {
            if (AdminService.assignSubjectToTutor(subjectId, tutorId)) {
                JOptionPane.showMessageDialog(this, "Tutor assigned.");
                refreshSubjectTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to assign tutor.");
            }
        }
    }
    
    private void removeTutorFromSubject() {
        int row = subjectTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a subject first.");
            return;
        }

        String subjectId = (String) model.getValueAt(row, 0);
        if (AdminService.removeSubjectFromTutor(subjectId)) {
            JOptionPane.showMessageDialog(this, "Tutor removed.");
            refreshSubjectTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to remove tutor.");
        }
    }
    
    private void updateSubjectFee() {
        int row = subjectTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a subject first.");
            return;
        }

        String subjectId = (String) model.getValueAt(row, 0);
        String newFeeStr = JOptionPane.showInputDialog(this, "Enter new fee:");
        try {
            int newFee = Integer.parseInt(newFeeStr);
            if (AdminService.updateSubjectFee(subjectId, newFee)) {
                JOptionPane.showMessageDialog(this, "Fee updated.");
                refreshSubjectTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update fee.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number.");
        }
    }
}
