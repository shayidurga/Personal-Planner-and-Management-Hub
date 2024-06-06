package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.*;
import dao.BookDAO;
import dao.EntryDAO;
import dao.BudgetDAO;
import dao.ScheduleDAO;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Connection connection;
    private static final long serialVersionUID = 1L;

    public LoginPage() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new BackgroundPanel("/resources/bg.jpg");
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel logoPanel = new JPanel(new BorderLayout());
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/login.png"));
        if (logoIcon != null) {
            // Resize the image icon
            Image image = logoIcon.getImage();
            Image scaledImage = image.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(scaledImage);

            JLabel logoLabel = new JLabel(logoIcon);
            logoPanel.add(logoLabel, BorderLayout.NORTH);
        }
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(logoPanel, gbc);

        JLabel welcomeLabel = new JLabel("Welcome to Your Journal Application", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 1;
        mainPanel.add(welcomeLabel, gbc);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints inputGbc = new GridBagConstraints();
        inputGbc.fill = GridBagConstraints.HORIZONTAL;
        inputGbc.insets = new Insets(5, 5, 5, 5);

        JLabel usernameLabel = new JLabel("Username:");
        inputGbc.gridx = 0;
        inputGbc.gridy = 0;
        inputPanel.add(usernameLabel, inputGbc);

        usernameField = new JTextField();
        inputGbc.gridx = 1;
        inputGbc.gridy = 0;
        inputGbc.weightx = 1.0; // Add weight to make the component expand horizontally
        inputGbc.insets = new Insets(5, 5, 5, 5); // Reset insets for the component
        inputPanel.add(usernameField, inputGbc);

        JLabel passwordLabel = new JLabel("Password:");
        inputGbc.gridx = 0;
        inputGbc.gridy = 1;
        inputPanel.add(passwordLabel, inputGbc);

        passwordField = new JPasswordField();
        inputGbc.gridx = 1;
        inputGbc.gridy = 1;
        inputPanel.add(passwordField, inputGbc);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            int userID = authenticate(username, password);
            if (userID != -1) {
                // Initialize DAO objects
                BookDAO bookDAO = new BookDAO(connection);
                BudgetDAO expenseDAO = new BudgetDAO(connection);
                EntryDAO entryDAO = new EntryDAO(connection);
                ScheduleDAO scheduleDAO = new ScheduleDAO(connection);
                // Create HomePage with initialized DAO objects
                HomePage homePage = new HomePage(bookDAO, expenseDAO, entryDAO, scheduleDAO, userID);
                homePage.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        inputGbc.gridx = 0;
        inputGbc.gridy = 2;
        inputGbc.gridwidth = 2;
        inputPanel.add(loginButton, inputGbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(inputPanel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton adminLoginButton = new JButton("Admin Login");
        adminLoginButton.addActionListener(e -> {
            AdminLoginPage adminLoginPage = new AdminLoginPage();
            adminLoginPage.setVisible(true);
        });
        buttonPanel.add(adminLoginButton);
        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(e -> {
            CreateAccount createAccount = new CreateAccount();
            createAccount.setVisible(true);
        });
        buttonPanel.add(createAccountButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/journaldata", "root", "root");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private int authenticate(String username, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT UserID FROM user WHERE username = ? AND password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("UserID");
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    private class BackgroundPanel extends JPanel {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private BufferedImage backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = ImageIO.read(getClass().getResource(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
