package dao;

import model.Budget;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDAO {
    private Connection connection;

    public BudgetDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Budget> getAllExpenses(int userID) {
        List<Budget> expenses = new ArrayList<>();
        String query = "SELECT * FROM journaldata.budget WHERE userId = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                double income = resultSet.getDouble("income");
                double expenseAmt = resultSet.getDouble("expense");
                Date date = resultSet.getDate("date");

                Budget expense = new Budget(id, userID, title, description, income, expenseAmt, date);
                expenses.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

    public void addExpense(Budget expense) {
        String query = "INSERT INTO journaldata.budget (userId, title, description, income, expense, date) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, expense.getUserId());
            statement.setString(2, expense.getTitle());
            statement.setString(3, expense.getDescription());
            statement.setDouble(4, expense.getIncome());
            statement.setDouble(5, expense.getExpense());
            statement.setDate(6, expense.getDate());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateExpense(Budget expense) {
        String query = "UPDATE journaldata.budget SET title = ?, description = ?, income = ?, expense = ?, date = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, expense.getTitle());
            statement.setString(2, expense.getDescription());
            statement.setDouble(3, expense.getIncome());
            statement.setDouble(4, expense.getExpense());
            statement.setDate(5, expense.getDate());
            statement.setInt(6, expense.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteExpense(int expenseId) {
        String query = "DELETE FROM journaldata.budget WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, expenseId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Budget getExpenseById(int expenseId) {
        Budget expense = null;
        String query = "SELECT * FROM journaldata.budget WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, expenseId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("userId");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                double income = resultSet.getDouble("income");
                double expenseAmt = resultSet.getDouble("expense");
                Date date = resultSet.getDate("date");

                expense = new Budget(id, userId, title, description, income, expenseAmt, date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expense;
    }
}
