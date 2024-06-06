package model;

import java.sql.Date;

public class Budget {
    private int id;
    private int userId;
    private String title;
    private String description;
    private double income;
    private double expense;
    private Date date;

    public Budget(int id, int userId, String title, String description, double income, double expense, Date date) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.income = income;
        this.expense = expense;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
