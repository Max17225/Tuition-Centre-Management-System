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

/**
 *
 * @author nengz
 */
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
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(102, 102, 102));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        JLabel title = new JLabel("My Account", SwingConstants.CENTER);
        title.setFont(new Font("MV Boli", Font.BOLD, 28));
        title.setForeground(new Color(0, 53, 97));
        topPanel.add(title, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // ------------------------------------------------------------------------ Center Panel with Two Columns 
        JPanel centerWrapperPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        centerWrapperPanel.setBackground(new Color(40, 40, 40));
        centerWrapperPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // -----------------------------------------------------------------------------------LEFT - Tutor Info 
        JPanel infoPanel = new RoundedPanel(20);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(60, 60, 60));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 90, 90), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // 标题
        JLabel infoTitle = new JLabel("TUTOR INFO");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        infoTitle.setForeground(Color.WHITE);
        infoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(infoTitle);
        infoPanel.add(Box.createVerticalStrut(15));

        // 内容字段
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

        // 空隙将按钮推到底部
        infoPanel.add(Box.createVerticalGlue());

        // === Bottom Buttons Panel ===
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); // 水平间距20
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // 整个 panel 左对齐

        JButton editBtn = createDashboardStyleButton("Edit My Info");
        editBtn.setPreferredSize(new Dimension(140, 40)); // 确保两个按钮一样大小
        editBtn.addActionListener(e -> {
            new EditTutorInfoGUI(tutorId).setVisible(true);
            dispose();
        });

        JButton changePwdBtn = createDashboardStyleButton("Change Password");
        changePwdBtn.setPreferredSize(new Dimension(140, 40));
        changePwdBtn.addActionListener(e -> {
            new ChangePasswordGUI(tutorId).setVisible(true);
            dispose();
        });

        btnPanel.add(editBtn);
        btnPanel.add(changePwdBtn);

        // 添加按钮到底部
        infoPanel.add(Box.createVerticalStrut(40)); // 垂直间距（可调）
        infoPanel.add(btnPanel);



        // -----------------------------------------------------------------------------------RIGHT - Subject Info
        JPanel subjectPanel = new RoundedPanel(20);
        subjectPanel.setLayout(new BoxLayout(subjectPanel, BoxLayout.Y_AXIS));
        subjectPanel.setBackground(new Color(60, 60, 60));
        subjectPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 90, 90), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel subjectTitle = new JLabel("TUTOR SUBJECTS");
        subjectTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        subjectTitle.setForeground(Color.WHITE);
        subjectTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subjectPanel.add(subjectTitle);
        subjectPanel.add(Box.createVerticalStrut(15));

        java.util.List<Subject> subjects = DataManager.of(Subject.class).readFromFile();
        subjects.sort(
            Comparator.comparing(Subject::getLevel)
                      .thenComparing(Subject::getSubjectName)
        );
        boolean hasSubject = false;
        for (Subject sub : subjects) {
            if (sub.getTutorId().equals(tutorId)) {
                JLabel subLabel = new JLabel("• " + sub.getSubjectName() + " (Level " + sub.getLevel() + ")");
                subLabel.setFont(infoFont);
                subLabel.setForeground(Color.LIGHT_GRAY);
                subLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                subjectPanel.add(subLabel);
                subjectPanel.add(Box.createVerticalStrut(5));
                hasSubject = true;
            }
        }
        if (!hasSubject) {
            JLabel noSub = new JLabel("No subjects assigned.");
            noSub.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            noSub.setForeground(Color.LIGHT_GRAY);
            noSub.setAlignmentX(Component.LEFT_ALIGNMENT);
            subjectPanel.add(noSub);
        }

        centerWrapperPanel.add(infoPanel);
        centerWrapperPanel.add(subjectPanel);
        add(centerWrapperPanel, BorderLayout.CENTER);

        // --------------------------------------------------------------------------Bottom Panel with Back Button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(102, 102, 102));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton backBtn = new JButton("Back") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground().darker());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };

        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setBackground(new Color(60, 179, 113));
        backBtn.setForeground(Color.WHITE);
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setOpaque(false);

        // hover
        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backBtn.setBackground(new Color(40, 160, 100));
                backBtn.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backBtn.setBackground(new Color(60, 179, 113));
                backBtn.repaint();
            }
        });

        backBtn.addActionListener(e -> {
            new TutorMainGUI(tutorId).setVisible(true);
            dispose();
        });

        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private class EditTutorInfoGUI extends JFrame {
        public EditTutorInfoGUI(String tutorId) {
            setTitle("Edit My Info");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            getContentPane().setBackground(new Color(102, 102, 102));
            setLayout(new BorderLayout());

            Tutor tutor = DataManager.of(Tutor.class).getRecordById(tutorId);

            // ===== 顶部标题 =====
            JLabel title = new JLabel("Edit My Info", SwingConstants.CENTER);
            title.setFont(new Font("MV Boli", Font.BOLD, 26));
            title.setForeground(new Color(0, 53, 97));
            title.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
            add(title, BorderLayout.NORTH);

            // ===== 表单区域 =====
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(new Color(40, 40, 40));

            Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
            Color labelColor = Color.WHITE;
            Insets padding = new Insets(10, 20, 10, 20);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = padding;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            JTextField usernameField = new JTextField(tutor.getUsername());
            JTextField phoneField = new JTextField(tutor.getPhoneNumber());
            JTextField countryField = new JTextField(tutor.getCountry());
            JTextField emailField = new JTextField(tutor.getEmail());

            addFormRow(formPanel, gbc, 0, "Username:", usernameField, labelFont, labelColor);
            addFormRow(formPanel, gbc, 1, "Phone:", phoneField, labelFont, labelColor);
            addFormRow(formPanel, gbc, 2, "Country:", countryField, labelFont, labelColor);
            addFormRow(formPanel, gbc, 3, "Email:", emailField, labelFont, labelColor);

            add(formPanel, BorderLayout.CENTER);

            // ===== 按钮区域 =====
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            btnPanel.setBackground(new Color(102, 102, 102));

            JButton saveBtn = createDashboardStyleButton("Save");
            JButton backBtn = createDashboardStyleButton("Back");

            backBtn.addActionListener(e -> {
                new TutorAccountGUI(tutorId).setVisible(true);
                dispose();
            });

            saveBtn.addActionListener(e -> {
                String username = usernameField.getText().trim();
                String phone = phoneField.getText().trim();
                String country = countryField.getText().trim();
                String email = emailField.getText().trim();

                // 先验证 email 格式
                if (!InputValidator.emailFormatIsValid(email)) {
                    JOptionPane.showMessageDialog(this,
                        "Invalid email format. Only '@gmail.com' and '@mail.com' are accepted.",
                        "Invalid Email",
                        JOptionPane.ERROR_MESSAGE);
                    return; // 阻止继续保存
                }

                // 设置数据
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

        private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField textField, Font font, Color color) {
            JLabel label = new JLabel(labelText);
            label.setFont(font);
            label.setForeground(color);

            textField.setFont(font);
            textField.setPreferredSize(new Dimension(400, 30));

            gbc.gridx = 0;
            gbc.gridy = row;
            panel.add(label, gbc);

            gbc.gridx = 1;
            panel.add(textField, gbc);
        }
    }
    
    private class ChangePasswordGUI extends JFrame {
        public ChangePasswordGUI(String tutorId) {
            setTitle("Change Password");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            getContentPane().setBackground(new Color(102, 102, 102));
            setLayout(new BorderLayout());

            Tutor tutor = DataManager.of(Tutor.class).getRecordById(tutorId);

            // ===== 顶部标题 =====
            JLabel title = new JLabel("Change Password", SwingConstants.CENTER);
            title.setFont(new Font("MV Boli", Font.BOLD, 26));
            title.setForeground(new Color(0, 53, 97));
            title.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
            add(title, BorderLayout.NORTH);

            // ===== 表单区域 =====
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

            // ===== 按钮区域 =====
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            btnPanel.setBackground(new Color(102, 102, 102));

            JButton changeBtn = createDashboardStyleButton("Change");
            JButton backBtn = createDashboardStyleButton("Back");

            changeBtn.addActionListener(e -> {
                String current = new String(currentPassField.getPassword()).trim();
                String newPass = new String(newPassField.getPassword()).trim();
                String confirm = new String(confirmPassField.getPassword()).trim();

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
    }
    
    private class RoundedPanel extends JPanel {
        private final int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private JLabel createInfoLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(font);
        label.setForeground(color);
        return label;
        }

    private JButton createDashboardStyleButton(String label) {
        JButton button = new JButton(label) {
            private boolean hover = false;

            {
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                setForeground(Color.WHITE);
                setFocusPainted(false);
                setContentAreaFilled(false);
                setOpaque(false);
                setBorderPainted(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setPreferredSize(new Dimension(120, 40)); // 可调整大小

                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hover = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = hover ? new Color(100, 100, 100) : new Color(60, 60, 60);
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.GRAY);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
            }
        };

        return button;
    }
}