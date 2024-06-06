package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.UserDAO;
import model.User;

public class UserManagementWindow extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserDAO userDAO;

    // Components
    private JTable userTable;
    private JScrollPane tableScrollPane;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton viewButton;

    public UserManagementWindow() {
        setTitle("User Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize UserDAO
        userDAO = new UserDAO();

        // Initialize components
        initComponents();

        // Fetch and display users
        refreshUserTable();

        // Add components to the layout
        addComponentsToLayout();
    }

    private void initComponents() {
        // Initialize buttons
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        viewButton = new JButton("View");

        // Initialize table and scroll pane
        userTable = new JTable();
        tableScrollPane = new JScrollPane(userTable);

        // Button listeners
        addButton.addActionListener(e -> addUser());
        editButton.addActionListener(e -> editUser());
        deleteButton.addActionListener(e -> deleteUser());
        viewButton.addActionListener(e -> viewUserDetails());
    }

    private void addComponentsToLayout() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private void refreshUserTable() {
        List<User> userList = userDAO.getAllUsers();
        Object[][] data = new Object[userList.size()][7];

        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            data[i] = new Object[]{
                    user.getUserID(),
                    user.getFullName(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getGender(),
                    user.getCreatedAt()
            };
        }

        String[] columnNames = {"UserID", "Full Name", "Username", "Email", "Password", "Gender", "Created At"};

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        userTable.setModel(model);
    }

    private void addUser() {
        // Add user logic
        // For simplicity, you can use JOptionPane for user input
        String fullName = JOptionPane.showInputDialog(this, "Enter Full Name:");
        String username = JOptionPane.showInputDialog(this, "Enter Username:");
        String email = JOptionPane.showInputDialog(this, "Enter Email:");
        String password = JOptionPane.showInputDialog(this, "Enter Password:");
        String gender = JOptionPane.showInputDialog(this, "Enter Gender:");

        // Create a new User object
        User user = new User(0, fullName, username, email, password, gender, null);

        // Add the user to the database
        userDAO.addUser(user);

        // Refresh the user table
        refreshUserTable();
    }

    private void editUser() {
        // Edit user logic
        // For simplicity, you can use JOptionPane for user input
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            int userID = (int) userTable.getValueAt(selectedRow, 0);
            String fullName = JOptionPane.showInputDialog(this, "Enter New Full Name:");
            String username = JOptionPane.showInputDialog(this, "Enter New Username:");
            String email = JOptionPane.showInputDialog(this, "Enter New Email:");
            String password = JOptionPane.showInputDialog(this, "Enter New Password:");
            String gender = JOptionPane.showInputDialog(this, "Enter New Gender:");

            // Create a new User object
            User user = new User(userID, fullName, username, email, password, gender, null);

            // Update the user in the database
            userDAO.updateUser(user);

            // Refresh the user table
            refreshUserTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.");
        }
    }

    private void deleteUser() {
        // Delete user logic
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            int userID = (int) userTable.getValueAt(selectedRow, 0);
            int confirmDelete = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirmDelete == JOptionPane.YES_OPTION) {
                userDAO.deleteUser(userID);
                refreshUserTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        }
    }

    private void viewUserDetails() {
        // View user details logic
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            int userID = (int) userTable.getValueAt(selectedRow, 0);
            User user = userDAO.getUserByID(userID);
            JOptionPane.showMessageDialog(this, "User Details:\n" + user);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to view details.");
        }
    }
}
