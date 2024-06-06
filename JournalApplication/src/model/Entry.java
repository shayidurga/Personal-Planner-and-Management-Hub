package model;

import java.sql.Date;

public class Entry {
    private int id;
    private int userID; 
    private String title;
    private String content;
    private Date date;
    private String mood; 
    private int habitRating; 
// Constructor
    public Entry(int id, int userID, String title, String content, Date date, String mood, int habitRating) {
        this.id = id;
        this.userID = userID;
        this.title = title;
        this.content = content;
        this.date = date;
        this.mood = mood;
        this.habitRating = habitRating;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public int getHabitRating() {
        return habitRating;
    }

    public void setHabitRating(int habitRating) {
        this.habitRating = habitRating;
    }
}