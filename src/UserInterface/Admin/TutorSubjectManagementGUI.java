/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Admin;

import DataModel.Subject;
import Util.DataManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class TutorSubjectManagementGUI extends JFrame {
    private final DefaultTableModel model; // Table model to manage Subject data
    private JTextField keywordField;
    
    public TutorSubjectManagementGUI(String adminId) {
        setTitle("Tutor Subject Management");
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0, 128, 128)); // Background color

        // ------------------------ Top Panel ------------------------
        JPanel topPanel = UserInterface.Admin.ComponentFactory.createTopTitlePanel("Subject Management");
        add(topPanel, BorderLayout.NORTH);

        // ------------------------ Bottom Panel (Back button) ------------------------
        JPanel bottomPanel = UserInterface.Admin.ComponentFactory.createBottomButtonPanel(this, adminId);
        add(bottomPanel, BorderLayout.SOUTH);

        // ---------------------------------------------------- Center Panel (Table + Buttons) ------------------------
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(20, 30, 45)); // Dark background
        
        // ------------------------ Filter Setup ----------------------------
        // Filter controls
        JLabel keyWordLabel = new JLabel("Search Keyword (TutorId, SubjectId, SubjectName):");
        keyWordLabel.setForeground(Color.WHITE);           // Label text color

        JPanel filterPanel = new JPanel();
        filterPanel.setBackground(new Color(20, 30, 45)); // Match background
        filterPanel.setForeground(Color.WHITE);
        filterPanel.add(keyWordLabel);
        
        keywordField = new JTextField("Input Keyword Here", 20);
        filterPanel.add(keywordField);
        
        // Filter button
        JButton filterButton = new JButton("Filter");
        filterButton.setBackground(new Color(0, 153, 153));
        filterButton.setForeground(Color.WHITE);
        filterButton.setFocusPainted(false); // No focus border
        filterPanel.add(filterButton);

        centerPanel.add(filterPanel, BorderLayout.NORTH);

        // ------------------------ Table Setup ------------------------
        
        List<Subject> allSubject = DataManager.of(Subject.class).readFromFile();
        String[] columns = {"Subject Id", "Subject Name", "Level", "Tutor Id", "Fee Per Month"};
        model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        // Fill table with data
        for (Subject s : allSubject) {
            model.addRow(new Object[]{ s.getId(), s.getSubjectName(), s.getLevel(), s.getTutorId(), s.getFeePerMonth()});
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


        // ------------------------ Buttons ------------------------
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(20, 30, 45));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));

        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
        Color btnColor = new Color(100, 255, 180);

        JButton registerBtn = new JButton("Register New Subject");
        JButton deleteBtn = new JButton("Delete Subject Record");

        // Style buttons
        for (JButton btn : new JButton[]{registerBtn, deleteBtn}) {
            btn.setPreferredSize(new Dimension(200, 40));
            btn.setFont(btnFont);
            btn.setBackground(Color.DARK_GRAY);
            btn.setForeground(btnColor);
            btn.setFocusPainted(false); // No focus rectangle
            btn.setBorderPainted(false); // No border
            btn.setBorder(BorderFactory.createLineBorder(btnColor));       // Colored border
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Pointer cursor
        }

        buttonPanel.add(registerBtn);
        buttonPanel.add(deleteBtn);
        
        // Create a vertical box to hold table and buttons with spacing
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(scrollPane);
        verticalBox.add(Box.createRigidArea(new Dimension(0, 20))); // 20px vertical space
        verticalBox.add(buttonPanel);

        centerPanel.add(verticalBox, BorderLayout.CENTER);

        // ------------------------ Button Actions ------------------------

        // RegisterButton Action: Open Register Subject GUI
        registerBtn.addActionListener(e -> {
            new RegisterSubjectGUI(adminId).setVisible(true);
            dispose(); // Close current frame
        });

        // DeleteButton Actin: Delete selected Subject
        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String subjectId = (String) table.getValueAt(selectedRow, 0); // ID is in column 0

                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete this Subject: " + subjectId + "?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    DataManager.of(Subject.class).deleteById(subjectId);
                    JOptionPane.showMessageDialog(null, "Subject deleted.");
                    refreshTable(); // Update UI after delete
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select asubject to delete.");
            }
        });

        // Filter Button Action
        filterButton.addActionListener(e -> reloadTableByKeyword());

        add(centerPanel, BorderLayout.CENTER); // add center content
    }

    
    private void reloadTableByKeyword() {
        model.setRowCount(0); // Clear existing table rows
        
        String keyword = keywordField.getText();
        
        List<String> rawRecordListOfSubject = DataManager.of(Subject.class).readRawLines();
        
        // get all the record that have the user's keyword
        List<String> keywordRecordList = rawRecordListOfSubject.stream()
            .filter(rec -> rec.toLowerCase().contains(keyword.toLowerCase()))
            .toList();
        
        for (String keywordRecord : keywordRecordList) {
            String [] recordColSplit = keywordRecord.split(",");
            
            model.addRow(recordColSplit);
        }
    }
    
        // ------------------------ Refresh table content ------------------------
    private void refreshTable() {
        model.setRowCount(0); // Clear existing table rows
        List<Subject> updatedList = DataManager.of(Subject.class).readFromFile();

        for (Subject s : updatedList) {
            model.addRow(new Object[]{ s.getId(), s.getSubjectName(), s.getLevel(), s.getTutorId(), s.getFeePerMonth() });
        }
    }

}
