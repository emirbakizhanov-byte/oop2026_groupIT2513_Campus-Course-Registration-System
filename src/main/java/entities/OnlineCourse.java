package entities;

public class OnlineCourse extends Course {
    private String platform;

    public OnlineCourse(int id, String title, int capacity,
                        String dayOfWeek, int startMinute, int endMinute,
                        String platform) {
        super(id, title, capacity, dayOfWeek, startMinute, endMinute);
        this.platform = platform;
    }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
}
