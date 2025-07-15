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
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import DataModel.*;
import UserInterface.LoginGUI;
import Util.*;


public class TutorMainGUI extends JFrame {
    final private String tutorId;
    
    // Initialize GUI
    public TutorMainGUI(String tutorId) {
        this.tutorId = tutorId;
        
        // ===Frame Setting===
        setTitle("Tutor Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === Top Panel ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(102, 102, 102));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Add extra space surround the topPanel

        JLabel logoLabel = new JLabel(); // A JLabel to display image
        logoLabel.setIcon(getScaledIcon("/images/ATCIcon.png", 100, 100));
        topPanel.add(logoLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS)); // Create a BoxLayout at center of topPanel, it can put two textLabel top and bottom
        textPanel.setBackground(new Color(102, 102, 102));

        JLabel titleLabel = new JLabel("Tutor Dashboard", SwingConstants.CENTER); 
        titleLabel.setFont(new Font("MV Boli", Font.BOLD, 30));
        titleLabel.setForeground(new Color(0, 53, 97));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // center of textpanel
        
        DataManager<Tutor> tutorManager = DataManager.of(Tutor.class);
        Tutor targetTutor = tutorManager.getRecordById(tutorId);
        JLabel welcomeLabel = new JLabel("Welcome " + targetTutor.getUsername(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Lucida Handwriting", Font.PLAIN, 18));
        welcomeLabel.setForeground(new Color(0, 53, 97));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(welcomeLabel);

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerWrapper.setBackground(new Color(102, 102, 102));
        centerWrapper.add(textPanel);
        topPanel.add(centerWrapper, BorderLayout.CENTER);
        
        // Account button
        JButton accountButton = new JButton("My Account", getScaledIcon("/images/uis--user-md.png", 48, 48));
        accountButton.setHorizontalTextPosition(SwingConstants.CENTER);
        accountButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        accountButton.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        accountButton.setForeground(new Color(200, 200, 200));
        accountButton.setFocusPainted(false);
        accountButton.setContentAreaFilled(false);
        accountButton.setOpaque(false);
        accountButton.setBorderPainted(false);
        accountButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        accountButton.setPreferredSize(new Dimension(90, 90));

        accountButton.addActionListener(e -> {
            new TutorAccountGUI(tutorId).setVisible(true); 
            dispose(); 
        });

        accountButton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                int width = c.getWidth();
                int height = c.getHeight();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ButtonModel model = ((AbstractButton) c).getModel();
                Color bgColor = model.isRollover() ? new Color(150, 150, 150) : new Color(40, 40, 40);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, width, height, 20, 20);
                g2.setColor(new Color(200, 200, 200));
                g2.drawRoundRect(0, 0, width - 1, height - 1, 20, 20);
                g2.dispose();
                super.paint(g, c);
            }
        });
        topPanel.add(accountButton, BorderLayout.EAST);
        
        // Add everything on top
        add(topPanel, BorderLayout.NORTH);
        
        
        // === Center Buttons ===
        // layout setting
        JPanel centerPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        centerPanel.setBackground(new Color(40, 40, 40));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        // button label
        String[] btnLabels = {
            "View My Schedule",  "Add Subject Schedule", "View Subject Student",
            "Update Subject Schedule", "Update Subject Fee"
        };
        
        // defult icon picture
        String[] defaultIcons = {
            "/images/Time Table 1.png",    
            "/images/Add Schedule 1.png",
            "/images/Student 1.png",
            "/images/Update Subject Schedule 1.png",
            "/images/Fee 1.png"
        };
        
        // hoven icon picture
        String[] hoverIcons = {
            "/images/Time Table 2.png",   
            "/images/Add Schedule 2.png",
            "/images/Student 2.png",
            "/images/Update Subject Schedule 2.png",
            "/images/Fee 2.png"
        };
        
        // create 5 button at center
        for (int i = 0; i < btnLabels.length; i++) {
            final String label = btnLabels[i];
            final ImageIcon defaultIcon = getScaledIcon(defaultIcons[i], 40, 40);  
            final ImageIcon hoverIcon = getScaledIcon(hoverIcons[i], 40, 40);

            JButton button = new JButton(label, defaultIcon) {
                private boolean hover = false;

                {
                    setHorizontalTextPosition(SwingConstants.CENTER); 
                    setVerticalTextPosition(SwingConstants.BOTTOM); // text will at below of the icon
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    setForeground(Color.WHITE);
                    setFocusPainted(false);
                    setContentAreaFilled(false);
                    setOpaque(false);
                    setBorderPainted(false);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));

                    addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent e) {
                            hover = true;
                            setIcon(hoverIcon);
                            repaint();
                        }

                        @Override
                        public void mouseExited(java.awt.event.MouseEvent e) {
                            hover = false;
                            setIcon(defaultIcon);
                            repaint();
                        }
                    });
                }
                
                // change button graphic and colour
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
                
                // change border graphic and colour
                @Override
                protected void paintBorder(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.GRAY);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                    g2.dispose();
                }
            };

            button.setPreferredSize(new Dimension(160, 100));

            // === Action Events ===
            button.addActionListener(e -> {
                switch (label) {
                    case "View My Schedule"        -> System.out.println("Viewing schedule for " + tutorId);
                    case "Add Subject Schedule"    -> System.out.println("Adding subject schedule...");
                    case "Update Subject Schedule" -> System.out.println("Updating subject schedule...");
                    case "Update Subject Fee"      -> System.out.println("Updating subject fee...");
                    case "View Subject Student"    -> System.out.println("Viewing subject student list...");
                    default                        -> System.out.println("Unknown button clicked");
                }
            });

            centerPanel.add(button);
            add(centerPanel, BorderLayout.CENTER);
        }



        // === Logout Button ===
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(102,102,102));

        ImageIcon defaultIcon = getScaledIcon("/images/Key 1.png", 20, 20); 
        ImageIcon hoverIcon = getScaledIcon("/images/Key 2.png", 20, 20);     

        JButton logoutBtn = new JButton("Logout", defaultIcon) {
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

        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setBackground(new Color(60, 179, 113));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setPreferredSize(new Dimension(120, 40));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setOpaque(false);

        // ==== Hover Effect: Icon + Color ====
        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(40, 160, 100)); // darker green
                logoutBtn.setIcon(hoverIcon); 
                logoutBtn.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(60, 179, 113)); // 原色
                logoutBtn.setIcon(defaultIcon); 
                logoutBtn.repaint();
            }
        });

        logoutBtn.addActionListener((ActionEvent e) -> {
            new LoginGUI().setVisible(true); // assuming LoginGUI exists
            dispose();
        });

        bottomPanel.add(logoutBtn);
        add(bottomPanel, BorderLayout.SOUTH);

    }
    
    // ----------------------------- Private Method -------------------------------------------------------
    
    private ImageIcon getScaledIcon(String path, int width, int height) {
        try {
            BufferedImage original = ImageIO.read(getClass().getResource(path));
            BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resized.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(original, 0, 0, width, height, null);
            g2d.dispose();
            return new ImageIcon(resized);
        } catch (IOException e) {
            System.err.println("Failed to load image: " + path);
            return null;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TutorMainGUI("T0001").setVisible(true));
    }
}
