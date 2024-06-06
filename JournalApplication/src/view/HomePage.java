package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.IOException;

import dao.BookDAO;
import dao.EntryDAO;
import dao.BudgetDAO;
import dao.ScheduleDAO; // Import ScheduleDAO

public class HomePage extends JFrame {
    private BookDAO bookDAO;
    private BudgetDAO budgetDAO;
    private EntryDAO entryDAO;
    private ScheduleDAO scheduleDAO; // Add ScheduleDAO field
    private int userID;
    private static final long serialVersionUID = 1L; // Add serialVersionUID

    public HomePage(BookDAO bookDAO, BudgetDAO budgetDAO, EntryDAO entryDAO, ScheduleDAO scheduleDAO, int userID) {
        this.bookDAO = bookDAO;
        this.budgetDAO = budgetDAO;
        this.entryDAO = entryDAO;
        this.scheduleDAO = scheduleDAO; // Initialize ScheduleDAO
        this.userID = userID;

        setTitle("Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);

        getContentPane().setBackground(new Color(230, 230, 255)); // Mild background color
    }

    public int getUserID() {
        return userID;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        contentPanel.setBackground(Color.WHITE);

        JPanel bookPanel = createPanelWithImageAndLabel("Book Tracking", "/resources/book.jpg", 275, 275, Color.PINK, () -> {
            BookManagementUI bookManagementUI = new BookManagementUI(bookDAO, userID);
            bookManagementUI.setVisible(true);
        });
        contentPanel.add(bookPanel);

        JPanel expensePanel = createPanelWithImageAndLabel("Budget Tracking", "/resources/expense.jpeg", 275, 275, Color.PINK, () -> {
            BudgetManagementUI expenseManagementUI = new BudgetManagementUI(budgetDAO, userID);
            expenseManagementUI.setVisible(true);
        });
        contentPanel.add(expensePanel);

        JPanel entryPanel = createPanelWithImageAndLabel("Journal Entries", "/resources/diary_logo.jpg", 275, 275, Color.PINK, () -> {
            EntryManagementUI entryManagementUI = new EntryManagementUI(entryDAO, userID);
            entryManagementUI.setVisible(true);
        });
        contentPanel.add(entryPanel);

        JPanel schedulePanel = createPanelWithImageAndLabel("Schedule", "/resources/schedule.jpg", 275, 275, Color.PINK, () -> {
            ScheduleManagementUI scheduleUI = new ScheduleManagementUI(scheduleDAO, userID); // Pass ScheduleDAO to ScheduleUI
            scheduleUI.setVisible(true);
        });
        contentPanel.add(schedulePanel);

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.setBackground(new Color(255, 192, 203)); // Mild background color
        JButton logoutButton = createButtonWithImage("/resources/logout.png", "Logout", () -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
            dispose();
        });
        logoutButton.setPreferredSize(new Dimension(120, 120)); // Set size of logout button
        logoutPanel.add(logoutButton);
        contentPanel.add(logoutPanel);

        return contentPanel;
    }

    private JButton createButtonWithImage(String imagePath, String buttonText, Runnable action) {
        JButton button = new JButton(buttonText);
        try {
            Image img = ImageIO.read(getClass().getResource(imagePath));
            ImageIcon icon = new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            button.setIcon(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.addActionListener(e -> action.run());
        return button;
    }

    private JPanel createPanelWithImageAndLabel(String labelText, String imagePath, int width, int height, Color backgroundColor, Runnable action) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);

        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(img);
        JLabel imageLabel = new JLabel(resizedIcon);
        panel.add(imageLabel, BorderLayout.CENTER);

        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        panel.add(label, BorderLayout.SOUTH);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });

        return panel;
    }
}
