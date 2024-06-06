package model;

public class Schedule {
    private int id;
    private int userID;
    private String day;
    private String title;
    private String description;
    private String priority;
    private String status;

    public Schedule(int id, int userID, String day, String title, String description, String priority, String status) {
        this.id = id;
        this.userID = userID;
        this.day = day;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
    }

    // Getters and Setters
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString method
    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", userID=" + userID +
                ", day='" + day + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
