package ch5;

public class Event
{
    public String id;

    public int year;

    public int month;

    public int day;

    public int userId;

    public int pageId;

    public Event(String id, int year, int month, int day, int userId, int pageId) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.userId = userId;
        this.pageId = pageId;
    }
}
