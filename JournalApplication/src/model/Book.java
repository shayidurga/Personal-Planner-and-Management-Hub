package model;

import java.sql.Date;

public class Book {
    private int id;
    private int userID; // Added field for userID
    private String title;
    private String author;
    private String genre;
    private Date entryDate;
    private int rating;
    private String status;

    // Constructor
    public Book(int id, int userID, String title, String author, String genre, Date entryDate, int rating, String status) {
        this.id = id;
        this.userID = userID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.entryDate = entryDate;
        this.rating = rating;
        this.status = status;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public int getRating() {
        return rating;
    }

    public String getStatus() {
        return status;
    }
}
