package entities;

public class LectureCourse extends Course {
    private String hall;

    public LectureCourse(int id, String title, int capacity,
                         String dayOfWeek, int startMinute, int endMinute,
                         String hall) {
        super(id, title, capacity, dayOfWeek, startMinute, endMinute);
        this.hall = hall;
    }

    public String getHall() { return hall; }
    public void setHall(String hall) { this.hall = hall; }
}

