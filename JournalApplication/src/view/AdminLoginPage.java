package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminLoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private static final long serialVersionUID = 1L; // Add serialVersionUID
    
    public AdminLoginPage() {
        setTitle("Admin Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel for the login components
        JPanel loginPanel = new JPanel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			// Override paintComponent to draw the background image
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load the background image
                ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/resources/bg3.jpeg"));
                // Draw the background image
                g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        loginPanel.setLayout(null); // Set layout to null for absolute positioning
        
        // Add login components to the login panel
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 80, 20); // Adjust position and size as needed
        loginPanel.add(usernameLabel);
        
        usernameField = new JTextField();
        usernameField.setBounds(140, 50, 150, 20); // Adjust position and size as needed
        loginPanel.add(usernameField);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 90, 80, 20); // Adjust position and size as needed
        loginPanel.add(passwordLabel);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(140, 90, 150, 20); // Adjust position and size as needed
        loginPanel.add(passwordField);
        
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 130, 100, 30); // Adjust position and size as needed
        loginPanel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Check admin credentials (Replace with your authentication logic)
                if ("admin".equals(username) && "admin123".equals(password)) {
                    openAdminDashboard();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
                }
            }
        });

        add(loginPanel);
        setVisible(true);
    }

    private void openAdminDashboard() {
        new AdminDashboardUI();
        dispose(); // Close login window
    }
}
