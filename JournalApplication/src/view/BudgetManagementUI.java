package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.BudgetDAO;
import model.Budget;

public class BudgetManagementUI extends JFrame {
    private BudgetDAO expenseDAO;
    private DefaultTableModel tableModel;
    private JTable expenseTable;
    private JLabel totalAmountLabel;
    private int userId; // Added userId attribute
    private static final long serialVersionUID = 1L; // Add serialVersionUID
    public BudgetManagementUI(BudgetDAO budgetDAO, int userId) { // Modified constructor to accept userId
        this.expenseDAO=budgetDAO;
        this.userId = userId; // Assigning userId

        setTitle("Expense Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window

        JPanel expensePanel = createExpensePanel();
        JPanel buttonPanel = createButtonPanel();

        add(expensePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createExpensePanel() {
        JPanel expensePanel = new JPanel();
        expensePanel.setLayout(new BorderLayout());

        // Create total amount label
        totalAmountLabel = new JLabel("Total Amount: $0.00");
        totalAmountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        expensePanel.add(totalAmountLabel, BorderLayout.NORTH);

        expenseTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        expensePanel.add(scrollPane, BorderLayout.CENTER);

        // Create table model
        String[] columnNames = {"ID", "Title", "Description", "Income", "Expense", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        expenseTable.setModel(tableModel);

        // Display all expenses initially
        displayAllExpenses();

        return expensePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton addButton = new JButton("Add Expense/Income");
        JButton updateButton = new JButton("Update Expense/Income");
        JButton deleteButton = new JButton("Delete Expense/Income");

        addButton.addActionListener(e -> addExpense());
        updateButton.addActionListener(e -> updateExpense());
        deleteButton.addActionListener(e -> deleteExpense());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        return buttonPanel;
    }

    private void displayAllExpenses() {
        // Clear previous data
        tableModel.setRowCount(0);

        // Get all expenses for the user from DAO
        List<Budget> expenses = expenseDAO.getAllExpenses(userId);
        for (Budget expense : expenses) {
            // Add expense details to the table model
            Object[] rowData = {expense.getId(), expense.getTitle(), expense.getDescription(),
                                expense.getIncome(), expense.getExpense(), expense.getDate()};
            tableModel.addRow(rowData);
        }

        // Update total amount label
        double totalAmount = calculateTotalAmount(expenses);
        totalAmountLabel.setText("Total Amount: $" + String.format("%.2f", totalAmount));
    }

    private double calculateTotalAmount(List<Budget> expenses) {
        double total = 0.0;
        for (Budget expense : expenses) {
            total += expense.getIncome();
            total -= expense.getExpense();
        }
        return total;
    }

    private void addExpense() {
        // Prompt user for new expense details
        String title = JOptionPane.showInputDialog(this, "Enter Title:");
        String description = JOptionPane.showInputDialog(this, "Enter Description:");
        double income = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Income:"));
        double expense = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Expense:"));
        String dateStr = JOptionPane.showInputDialog(this, "Enter Date (YYYY-MM-DD):");

        // Parse date
        java.sql.Date date = java.sql.Date.valueOf(dateStr);

        // Create Expense object
        Budget newExpense = new Budget(0, userId, title, description, income, expense, date); // Modified to include userId

        // Add the new expense to the database
        expenseDAO.addExpense(newExpense);

        // Refresh expense display
        displayAllExpenses();
    }

    private void updateExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to update.");
            return;
        }

        int expenseId = (int) expenseTable.getValueAt(selectedRow, 0);
        Budget selectedExpense = expenseDAO.getExpenseById(expenseId);

        // Prompt user for updated expense details
        String updatedTitle = JOptionPane.showInputDialog(this, "Enter Updated Title:", selectedExpense.getTitle());
        String updatedDescription = JOptionPane.showInputDialog(this, "Enter Updated Description:", selectedExpense.getDescription());
        double updatedIncome = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Updated Income:", selectedExpense.getIncome()));
        double updatedExpense = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Updated Expense:", selectedExpense.getExpense()));
        String updatedDateStr = JOptionPane.showInputDialog(this, "Enter Updated Date (YYYY-MM-DD):", selectedExpense.getDate());

        // Parse updated date
        java.sql.Date updatedDate = java.sql.Date.valueOf(updatedDateStr);

        // Create updated Expense object
        Budget updatedExpenseObj = new Budget(expenseId, userId, updatedTitle, updatedDescription, updatedIncome, updatedExpense, updatedDate); // Modified to include userId

        // Update the expense in the database
        expenseDAO.updateExpense(updatedExpenseObj);

        // Refresh expense display
        displayAllExpenses();
    }

    private void deleteExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to delete.");
            return;
        }

        int expenseId = (int) expenseTable.getValueAt(selectedRow, 0);

        // Confirm expense deletion
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this expense?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // Delete the expense from the database
            expenseDAO.deleteExpense(expenseId);

            // Refresh expense display
            displayAllExpenses();
        }
    }

    public void displayUI() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }
}
