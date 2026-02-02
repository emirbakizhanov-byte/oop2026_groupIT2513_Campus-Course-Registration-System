package entities;

public class LabCourse extends Course {
    private String labRoom;

    public LabCourse(int id, String title, int capacity,
                     String dayOfWeek, int startMinute, int endMinute,
                     String labRoom) {
        super(id, title, capacity, dayOfWeek, startMinute, endMinute);
        this.labRoom = labRoom;
    }

    public String getLabRoom() { return labRoom; }
    public void setLabRoom(String labRoom) { this.labRoom = labRoom; }
}

