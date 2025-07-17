/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Tutor;

/**
 *
 * @author nengz
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// UserInterface.Tutor class for create common element

public class ComponentFactory {
    
    // Create a rounded button with hoven effect
    public static JButton createRoundedButton(String text, Color baseColor) {
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
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 40));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(baseColor.darker());
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(baseColor);
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
