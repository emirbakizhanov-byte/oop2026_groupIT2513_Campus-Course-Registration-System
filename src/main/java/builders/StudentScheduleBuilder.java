package builders;

import entities.Course;
import entities.StudentSchedule;

import java.util.ArrayList;
import java.util.List;

public class StudentScheduleBuilder {
    private int studentId;
    private final List<Course> courses = new ArrayList<>();

    public StudentScheduleBuilder studentId(int studentId) {
        this.studentId = studentId;
        return this;
    }

    public StudentScheduleBuilder addCourse(Course c) {
        this.courses.add(c);
        return this;
    }

    public StudentScheduleBuilder addCourses(List<Course> list) {
        this.courses.addAll(list);
        return this;
    }

    public StudentSchedule build() {
        int totalCredits = courses.size() * 3;

        StringBuilder sb = new StringBuilder();
        sb.append("=== Student Schedule ===\n");
        sb.append("StudentId: ").append(studentId).append("\n");
        sb.append("Total credits: ").append(totalCredits).append("\n");
        sb.append("-----------------------------------------\n");
        sb.append(String.format("%-3s %-16s %-5s %-7s %-7s%n", "#", "Course", "Day", "Start", "End"));

        int i = 1;
        for (Course c : courses) {
            String title = safe(c.getTitle());
            String day = safe(c.getDayOfWeek());
            String start = toHHMM(c.getStartMinute());
            String end = toHHMM(c.getEndMinute());

            sb.append(String.format("%-3d %-16s %-5s %-7s %-7s%n", i++, title, day, start, end));
        }

        sb.append("-----------------------------------------\n");

        return new StudentSchedule(studentId, courses, totalCredits, sb.toString());
    }

    private String toHHMM(int minutes) {
        int h = minutes / 60;
        int m = minutes % 60;
        return String.format("%02d:%02d", h, m);
    }

    private String safe(String s) {
        return (s == null) ? "-" : s.trim();
    }
    }


