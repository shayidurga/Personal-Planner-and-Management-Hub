package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import dao.BookDAO;
import model.Book;
import java.util.List;

public class BookManagementUI extends JFrame {
    private BookDAO bookDAO;
    private DefaultTableModel tableModel;
    private JTable bookTable;
    private int userID;
    private static final long serialVersionUID = 1L; // Add serialVersionUID
    public BookManagementUI(BookDAO bookDAO, int userID) {
        this.bookDAO = bookDAO;
        this.userID = userID;

        setTitle("Book Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window

        JPanel bookPanel = createBookPanel();
        JPanel buttonPanel = createButtonPanel();

        add(bookPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createBookPanel() {
        JPanel bookPanel = new JPanel();
        bookPanel.setLayout(new BorderLayout());

        bookTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(bookTable);
        bookPanel.add(scrollPane, BorderLayout.CENTER);

        // Create table model
        String[] columnNames = {"ID", "Title", "Author", "Genre", "Entry Date", "Rating", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bookTable.setModel(tableModel);

        // Display all books initially
        displayAllBooks();

        return bookPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton addButton = new JButton("Add Book");
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Book");
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Book");
        buttonPanel.add(deleteButton);

        addButton.addActionListener(e -> {
            addBook();
        });

        updateButton.addActionListener(e -> {
            updateBook();
        });

        deleteButton.addActionListener(e -> {
            deleteBook();
        });

        return buttonPanel;
    }

    private void displayAllBooks() {
        // Clear previous data
        tableModel.setRowCount(0);

        // Get all books from DAO
        List<Book> books = bookDAO.getAllBooks(userID);
        for (Book book : books) {
            // Add book details to the table model
            Object[] rowData = {book.getId(), book.getTitle(), book.getAuthor(), book.getGenre(), book.getEntryDate(), book.getRating(), book.getStatus()};
            tableModel.addRow(rowData);
        }
    }

    private void addBook() {
        // Prompt user for new book details
        String title = JOptionPane.showInputDialog(this, "Enter Title:");
        String author = JOptionPane.showInputDialog(this, "Enter Author:");
        String genre = JOptionPane.showInputDialog(this, "Enter Genre:");
        int rating = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Rating:"));
        String status = JOptionPane.showInputDialog(this, "Enter Status:");
     // Get the current system date
        java.util.Date currentDate = new java.util.Date();
        // Create Book object
        Book newBook = new Book(0, userID, title, author, genre, new java.sql.Date(currentDate.getTime()), rating, status);

        // Add the new book to the database
        bookDAO.addBook(newBook);

        // Refresh book display
        displayAllBooks();
    }
        // Implement updateBook method
    	private void updateBook() {
    	    int selectedRow = bookTable.getSelectedRow();
    	    if (selectedRow == -1) {
    	        JOptionPane.showMessageDialog(this, "Please select a book to update.");
    	        return;
    	    }

    	    int bookId = (int) bookTable.getValueAt(selectedRow, 0);
    	    Book selectedBook = bookDAO.getBookById(bookId, userID);

    	    // Prompt user for updated book details
    	    String updatedTitle = JOptionPane.showInputDialog(this, "Enter Updated Title:", selectedBook.getTitle());
    	    String updatedAuthor = JOptionPane.showInputDialog(this, "Enter Updated Author:", selectedBook.getAuthor());
    	    String updatedGenre = JOptionPane.showInputDialog(this, "Enter Updated Genre:", selectedBook.getGenre());
    	    int updatedRating = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Updated Rating:", selectedBook.getRating()));
    	    String updatedStatus = JOptionPane.showInputDialog(this, "Enter Updated Status:", selectedBook.getStatus());
    	    java.util.Date currentDate = new java.util.Date();
    	    // Create updated Book object
    	    Book updatedBook = new Book(bookId, userID, updatedTitle, updatedAuthor, updatedGenre, new java.sql.Date(currentDate.getTime()), updatedRating, updatedStatus);

    	    // Update the book in the database
    	    bookDAO.updateBook(updatedBook);

    	    // Refresh book display
    	    displayAllBooks();
    	}


    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            return;
        }

        int bookId = (int) bookTable.getValueAt(selectedRow, 0);

        // Confirm book deletion
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // Delete the book from the database
            bookDAO.deleteBook(bookId, userID);

            // Refresh book display
            displayAllBooks();
        }
    }

    public void displayUI() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }
}
