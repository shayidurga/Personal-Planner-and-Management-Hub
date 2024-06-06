package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboardUI extends JFrame {
    private static final long serialVersionUID = 1L; // Add serialVersionUID
    
    public AdminDashboardUI() {
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel for the content with BorderLayout
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(64, 224, 208)); // Set background color

        // Create a panel for the buttons with GridBagLayout for centering
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(64, 224, 208)); // Set background color for the button panel

        // Create buttons
        JButton userManagementButton = new JButton("User Management");
        JButton logoutButton = new JButton("Logout");

        // Set button size
        Dimension buttonSize = new Dimension(200, 50);
        userManagementButton.setPreferredSize(buttonSize);
        logoutButton.setPreferredSize(buttonSize);

        // Add action listeners to buttons
        userManagementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openUserManagementWindow();
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openLogin();
            }
        });

        // Add buttons to the button panel with GridBagConstraints to center them
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0); // Add vertical spacing
        buttonPanel.add(userManagementButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(logoutButton, gbc);

        // Add the button panel to the content panel
        contentPanel.add(buttonPanel, BorderLayout.CENTER);

        // Set the content panel of the frame
        setContentPane(contentPanel);
        setVisible(true);
    }

    private void openUserManagementWindow() {
        UserManagementWindow userManagementWindow = new UserManagementWindow();
        userManagementWindow.setVisible(true); // Make the window visible
    }

    private void openLogin() {
        new AdminLoginPage();
        dispose(); // Close dashboard window
    }
}
