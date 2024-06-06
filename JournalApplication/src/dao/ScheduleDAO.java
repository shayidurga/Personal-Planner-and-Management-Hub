package dao;

import model.Schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ScheduleDAO {
    private Connection connection;

    public ScheduleDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to add a new schedule
    public void addSchedule(Schedule schedule) throws SQLException {
        String query = "INSERT INTO schedule (UserID, day, Title, Description, Priority, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, schedule.getUserID());
            preparedStatement.setString(2, schedule.getDay());
            preparedStatement.setString(3, schedule.getTitle());
            preparedStatement.setString(4, schedule.getDescription());
            preparedStatement.setString(5, schedule.getPriority());
            preparedStatement.setString(6, schedule.getStatus());

            preparedStatement.executeUpdate();
        }
    }

    // Method to retrieve all schedules for a specific user
    public Map<Integer, Schedule> getAllSchedules(int userId) throws SQLException {
        Map<Integer, Schedule> schedulesMap = new HashMap<>();
        String query = "SELECT * FROM schedule WHERE UserID=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Schedule schedule = new Schedule(
                            resultSet.getInt("id"),
                            resultSet.getInt("UserID"),
                            resultSet.getString("day"),
                            resultSet.getString("Title"),
                            resultSet.getString("Description"),
                            resultSet.getString("Priority"),
                            resultSet.getString("Status")
                    );
                    schedulesMap.put(schedule.getId(), schedule);
                }
            }
        }
        return schedulesMap;
    }

    // Method to update a schedule
    public void updateSchedule(Schedule schedule) throws SQLException {
        String query = "UPDATE schedule SET UserID=?, day=?, Title=?, Description=?, Priority=?, Status=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, schedule.getUserID());
            preparedStatement.setString(2, schedule.getDay());
            preparedStatement.setString(3, schedule.getTitle());
            preparedStatement.setString(4, schedule.getDescription());
            preparedStatement.setString(5, schedule.getPriority());
            preparedStatement.setString(6, schedule.getStatus());
            preparedStatement.setInt(7, schedule.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("No rows updated for schedule ID: " + schedule.getId());
            } else {
                System.out.println("Schedule updated successfully for ID: " + schedule.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error updating schedule with ID " + schedule.getId() + ": " + e.getMessage());
            throw e;
        }
    }


    // Method to delete a schedule
    public void deleteSchedule(int scheduleId) throws SQLException {
        String query = "DELETE FROM schedule WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, scheduleId);
            preparedStatement.executeUpdate();
        }
    }

}
