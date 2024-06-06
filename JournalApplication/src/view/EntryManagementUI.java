package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import dao.EntryDAO;
import model.Entry;
import java.util.List;

public class EntryManagementUI extends JFrame {
    private EntryDAO entryDAO;
    private DefaultTableModel tableModel;
    private JTable entryTable;
    private int userID; 
    private static final long serialVersionUID = 1L; // Add serialVersionUID
    public EntryManagementUI(EntryDAO entryDAO, int userID) {
        this.entryDAO = entryDAO;
        this.userID = userID; // Storing the userID

        setTitle("Entry Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window

        JPanel entryPanel = createEntryPanel();
        JPanel buttonPanel = createButtonPanel();

        add(entryPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createEntryPanel() {
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BorderLayout());

        entryTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(entryTable);
        entryPanel.add(scrollPane, BorderLayout.CENTER);

        // Create table model
        String[] columnNames = {"ID", "Title", "Content", "Date", "Mood", "Habit Rating"};
        tableModel = new DefaultTableModel(columnNames, 0);
        entryTable.setModel(tableModel);

        // Display all entries initially
        displayAllEntries();

        return entryPanel;
    }
 // Creates the panel for buttons (Add, Update, Delete)
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton addButton = new JButton("Add Entry");
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Entry");
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Entry");
        buttonPanel.add(deleteButton);

        addButton.addActionListener(e -> {
            addEntry();
        });

        updateButton.addActionListener(e -> {
            updateEntry();
        });

        deleteButton.addActionListener(e -> {
            deleteEntry();
        });

        return buttonPanel;
    }

    private void displayAllEntries() {
        // Clear previous data
        tableModel.setRowCount(0);

        // Get all entries for the logged-in user
        List<Entry> entries = entryDAO.getAllEntries(userID);
        for (Entry entry : entries) {
            // Add entry details to the table model
            Object[] rowData = {entry.getId(), entry.getTitle(), entry.getContent(), entry.getDate(), entry.getMood(), entry.getHabitRating()};
            tableModel.addRow(rowData);
        }
    }

    private void addEntry() {
        // Prompt user for new entry details
        String title = JOptionPane.showInputDialog(this, "Enter Title:");
        String content = JOptionPane.showInputDialog(this, "Enter Content:");
        int habitRating = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Habit Rating (1-10):"));
        String mood = JOptionPane.showInputDialog(this, "Enter Mood:");

     // Get the current system date
        java.util.Date currentDate = new java.util.Date();

        // Create Entry object
        Entry newEntry = new Entry(0, userID, title, content,  new java.sql.Date(currentDate.getTime()), mood, habitRating);

        // Add the new entry to the database
        entryDAO.addEntry(newEntry);

        // Refresh entry display
        displayAllEntries();
    }

    private void updateEntry() {
        int selectedRow = entryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an entry to update.");
            return;
        }

        int entryId = (int) entryTable.getValueAt(selectedRow, 0);
        Entry selectedEntry = entryDAO.getEntryById(entryId);

        String updatedTitle = JOptionPane.showInputDialog(this, "Enter Updated Title:", selectedEntry.getTitle());
        String updatedContent = JOptionPane.showInputDialog(this, "Enter Updated Content:", selectedEntry.getContent());
        int updatedHabitRating = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Updated Habit Rating (1-10):", selectedEntry.getHabitRating()));
        String updatedMood = JOptionPane.showInputDialog(this, "Enter Updated Mood:", selectedEntry.getMood());
     // Get the current system date
        java.util.Date currentDate = new java.util.Date();
      

        // Create updated Entry object
        Entry updatedEntry = new Entry(entryId, userID, updatedTitle, updatedContent, new java.sql.Date(currentDate.getTime()), updatedMood, updatedHabitRating);

        // Update the entry in the database
        entryDAO.updateEntry(updatedEntry);

        // Refresh entry display
        displayAllEntries();
    }

    private void deleteEntry() {
        int selectedRow = entryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an entry to delete.");
            return;
        }

        int entryId = (int) entryTable.getValueAt(selectedRow, 0);

        // Confirm entry deletion
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this entry?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // Delete the entry from the database
            entryDAO.deleteEntry(entryId);

            // Refresh entry display
            displayAllEntries();
        }
    }

    public void displayUI() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }
}
