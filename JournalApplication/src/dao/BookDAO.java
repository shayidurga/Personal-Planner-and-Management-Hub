package dao;

import model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection connection;

    public BookDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Book> getAllBooks(int userID) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE UserID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                Date entryDate = resultSet.getDate("entry_date");
                int rating = resultSet.getInt("rating");
                String status = resultSet.getString("status");

                Book book = new Book(id, userID, title, author, genre, entryDate, rating, status);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public void addBook(Book book) {
        String query = "INSERT INTO books (userID, title, author, genre, entry_date, rating, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, book.getUserID());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getGenre());
            statement.setDate(5, book.getEntryDate());
            statement.setInt(6, book.getRating());
            statement.setString(7, book.getStatus());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {
        String query = "UPDATE books SET title = ?, author = ?, genre = ?, entry_date = ?, rating = ?, status = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getGenre());
            statement.setDate(4, book.getEntryDate());
            statement.setInt(5, book.getRating());
            statement.setString(6, book.getStatus());
            statement.setInt(7, book.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(int bookId, int userID) {
        String query = "DELETE FROM books WHERE id = ? AND userID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            statement.setInt(2, userID);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Book getBookById(int bookId, int userID) {
        String query = "SELECT * FROM books WHERE id = ? AND userID = ?";
        Book book = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            statement.setInt(2, userID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                Date entryDate = resultSet.getDate("entry_date");
                int rating = resultSet.getInt("rating");
                String status = resultSet.getString("status");

                book = new Book(id, userID, title, author, genre, entryDate, rating, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return book;
    }
}
