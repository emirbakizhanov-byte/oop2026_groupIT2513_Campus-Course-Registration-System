package entities;

import java.util.List;

public class StudentSchedule {
    private final int studentId;
    private final List<Course> courses;
    private final int totalCredits;
    private final String summary;

    public StudentSchedule(int studentId, List<Course> courses, int totalCredits, String summary) {
        this.studentId = studentId;
        this.courses = courses;
        this.totalCredits = totalCredits;
        this.summary = summary;
    }

    public int getStudentId() { return studentId; }
    public List<Course> getCourses() { return courses; }
    public int getTotalCredits() { return totalCredits; }
    public String getSummary() { return summary; }

    @Override
    public String toString() {
        return summary;
    }
}
