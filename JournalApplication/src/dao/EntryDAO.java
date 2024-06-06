package dao;

import model.Entry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntryDAO {
    private Connection connection;
 // Constructor: Initializes the DAO with a database connection.
    public EntryDAO(Connection connection) {
        this.connection = connection;
    }
 // Retrieves all entries associated with a specific user ID from the database.
    public List<Entry> getAllEntries(int userID) {
        List<Entry> entries = new ArrayList<>();
        String query = "SELECT * FROM entries WHERE userID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                Date date = resultSet.getDate("date");
                String mood = resultSet.getString("mood");
                int habitRating = resultSet.getInt("habit_rating");

                Entry entry = new Entry(id, userID, title, content, date, mood, habitRating);
                entries.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entries;
    }
 // Retrieves a single entry from the database based on its ID.
    public Entry getEntryById(int entryId) {
        Entry entry = null;
        String query = "SELECT * FROM entries WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, entryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userID = resultSet.getInt("userID");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                Date date = resultSet.getDate("date");
                String mood = resultSet.getString("mood");
                int habitRating = resultSet.getInt("habit_rating");

                entry = new Entry(id, userID, title, content, date, mood, habitRating);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entry;
    }


    public void addEntry(Entry entry) {
        // Validate UserID before inserting
        if (!isValidUserID(entry.getUserID())) {
            System.out.println("Invalid UserID. Entry not added.");
            return;
        }

        String query = "INSERT INTO entries (userID, title, content, date, mood, habit_rating) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, entry.getUserID()); // Set the UserID
            statement.setString(2, entry.getTitle());
            statement.setString(3, entry.getContent());
            statement.setDate(4, entry.getDate());
            statement.setString(5, entry.getMood());
            statement.setInt(6, entry.getHabitRating());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidUserID(int userID) {
        // Check if the given UserID exists in the user table
        String query = "SELECT * FROM user WHERE UserID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Returns true if UserID exists, false otherwise
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of any exception
        }
    }

    public void updateEntry(Entry entry) {
        String query = "UPDATE entries SET title = ?, content = ?, date = ?, mood = ?, habit_rating = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entry.getTitle());
            statement.setString(2, entry.getContent());
            statement.setDate(3, entry.getDate());
            statement.setString(4, entry.getMood());
            statement.setInt(5, entry.getHabitRating());
            statement.setInt(6, entry.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEntry(int entryId) {
        String query = "DELETE FROM entries WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, entryId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
