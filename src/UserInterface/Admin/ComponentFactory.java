/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author nengz
 */
public class ComponentFactory {
    
    // Create a rounded button with hoven effect
    public static JButton createRoundedButton(String text, Color color) {
        JButton button = new JButton(text) {
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

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 40));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(color.darker());
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(color);
                button.repaint();
            }
        });

        return button;
    }
    
    // Create dashboard style button
    public static JButton createDashboardStyleButton(String label) {
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
                setPreferredSize(new Dimension(120, 40)); 

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
    
    // Create TopPanel for display Title 
    public static JPanel createTopTitlePanel(String title) {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 128, 128));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("MV Boli", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 53, 97));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        return topPanel;
    }
    
    // Create BottomPanel for display Back Button
    public static JPanel createBottomButtonPanel(JFrame parentFrame, String adminId) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 128, 128));

        JButton backButton = ComponentFactory.createRoundedButton("Back", new Color(40, 160, 100));
        backButton.addActionListener(e -> {
            new AdminMainGUI(adminId).setVisible(true);
            parentFrame.dispose();
        });

        buttonPanel.add(backButton);
        return buttonPanel;
    }
    
    // If user was out of data for the page, this method will display empty page
    // param(title)  : Title of the frame
    // param(adminId): The id of the admin
    // param(message): The message that will display at the center of the frame
    public static void showEmptyStatusFrame(String title, String adminId, String message) {
        JFrame frame = new JFrame(title);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel msgLabel = new JLabel(message, SwingConstants.CENTER);
        msgLabel.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        msgLabel.setForeground(Color.LIGHT_GRAY);
        msgLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));

        JPanel topPanel = createTopTitlePanel(title); 
        JPanel botPanel = createBottomButtonPanel(frame, adminId); 

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(30, 30, 30));
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(msgLabel, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(botPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
    
    // Rounded Panel Class
    public static class RoundedPanel extends JPanel {
        final private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }
}
