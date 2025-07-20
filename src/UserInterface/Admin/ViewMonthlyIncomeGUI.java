/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Admin;

import DataModel.Payment;
import Util.DataManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author nengz
 */
public class ViewMonthlyIncomeGUI extends JFrame {
    private final JTable paymentTable;              // Table to display payments
    private final DefaultTableModel tableModel;     // Table model for dynamic data
    private final JComboBox<Integer> monthComboBox; // Dropdown to select month
    private final JLabel totalLabel;                // Label to show total income

    public ViewMonthlyIncomeGUI(String adminId) {
        setTitle("Monthly Income Report");                 // Window title
        setSize(800, 600);                                 // Window size
        setLocationRelativeTo(null);                       // Center window on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLayout(new BorderLayout());                     // Use BorderLayout for layout

        // Top Panel
        JPanel topPanel = UserInterface.Admin.ComponentFactory.createTopTitlePanel("My Account");
        add(topPanel, BorderLayout.NORTH);

        // Bottom Panel
        JPanel bottomPanel = UserInterface.Admin.ComponentFactory.createBottomButtonPanel(this, adminId);
        add(bottomPanel, BorderLayout.SOUTH);

        // Center Panel setup
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(20, 30, 45)); // Dark background

        // Filter controls
        JLabel selectMonth = new JLabel("Select Month:");
        selectMonth.setForeground(Color.WHITE);           // Label text color

        JPanel filterPanel = new JPanel();
        filterPanel.setBackground(new Color(20, 30, 45)); // Match background
        filterPanel.setForeground(Color.WHITE);
        filterPanel.add(selectMonth);

        // Month dropdown (1 to 12)
        monthComboBox = new JComboBox<>();
        for (int i = 1; i <= 12; i++) monthComboBox.addItem(i);
        filterPanel.add(monthComboBox);

        // Filter button
        JButton filterButton = new JButton("Filter");
        filterButton.setBackground(new Color(0, 153, 153));
        filterButton.setForeground(Color.WHITE);
        filterButton.setFocusPainted(false); // No focus border
        filterPanel.add(filterButton);

        // Label for displaying total income
        totalLabel = new JLabel("Total Income: RM0");
        totalLabel.setForeground(Color.WHITE);
        filterPanel.add(totalLabel);

        centerPanel.add(filterPanel, BorderLayout.NORTH); // Add filter panel to top of center

        // Table setup
        String[] columnNames = {"Payment ID", "Receptionist ID", "Student ID", "Subject ID", "Amount", "Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0); // Empty table with headers
        paymentTable = new JTable(tableModel);

        // Table appearance
        paymentTable.setGridColor(new Color(80, 80, 80)); // Grid line color
        paymentTable.setSelectionForeground(Color.WHITE); // Selected text color
        paymentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Row font
        paymentTable.setRowHeight(28);                              // Row height

        // Table header styling
        JTableHeader tableHeader = paymentTable.getTableHeader();
        tableHeader.setForeground(Color.BLACK); // Header text color
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Header font

        // Add table to scroll pane and center panel
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.getViewport().setBackground(new Color(50, 50, 50)); // Background behind table
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER); // Add center panel to frame

        // Add filter action
        filterButton.addActionListener(e -> loadPaymentsForSelectedMonth());

        setVisible(true); // Show window
    }

    private void loadPaymentsForSelectedMonth() {
        int selectedMonth = (int) monthComboBox.getSelectedItem(); // Get selected month
        int currentYear = LocalDate.now().getYear();               // Get current year
        List<Payment> payments = DataManager.of(Payment.class).readFromFile(); // Load payment data

        tableModel.setRowCount(0); // Clear existing table rows
        double total = 0;          // Reset total

        // Process each payment record
        for (Payment p : payments) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); // Define expected date format
                LocalDate date = LocalDate.parse(p.getPaymentDate(), formatter); // Parse payment date

                // Check if payment matches selected month/year
                if (date.getMonthValue() == selectedMonth && date.getYear() == currentYear) {
                    // Add row to table
                    tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getReceptionistId(),
                        p.getStudentId(),
                        p.getSubjectId(),
                        "RM" + p.getAmount(),
                        p.getPaymentDate(),
                        p.getStatus()
                    });

                    // Add to total income, but not unpaid satus
                    if (p.getStatus().equalsIgnoreCase("Paid"))
                        total += Double.parseDouble(p.getAmount());
                }
                
            } catch (NumberFormatException e) {
                // Log invalid entries
                System.err.println("Invalid payment entry: " + p.toDataLine());
            }
        }

        // Display total income formatted to 2 decimal places
        totalLabel.setText(String.format("Total Income: RM%.2f", total));
    }
}

