package services;

import builders.StudentScheduleBuilder;
import entities.Course;
import entities.Enrollment;
import entities.Student;
import entities.StudentSchedule;
import exceptions.*;

import repositories.CourseRepository;
import repositories.EnrollmentRepository;
import repositories.StudentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationService {
    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepo;

    public RegistrationService(StudentRepository studentRepo, CourseRepository courseRepo, EnrollmentRepository enrollmentRepo) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.enrollmentRepo = enrollmentRepo;
    }

    public Enrollment enroll(int studentId, int courseId) {
        if (studentId <= 0) throw new InvalidInputException("studentId must be > 0");
        if (courseId <= 0) throw new InvalidInputException("courseId must be > 0");

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));

        // already exists?
        enrollmentRepo.findByStudentAndCourse(studentId, courseId)
                .ifPresent(e -> { throw new AlreadyExistsException("Student already enrolled in this course."); });

        // capacity check
        int enrolledCount = enrollmentRepo.countByCourseId(courseId);
        if (enrolledCount >= course.getCapacity()) {
            throw new CapacityExceededException("Course is full. Capacity=" + course.getCapacity());
        }

        // time conflict check (simple version)
        for (Enrollment e : enrollmentRepo.findByStudentId(studentId)) {
            Course other = courseRepo.findById(e.getCourseId()).orElse(null);
            if (other != null && isTimeConflict(course, other)) {
                throw new TimeConflictException("Time conflict with courseId=" + other.getId());
            }
        }

        return enrollmentRepo.create(new Enrollment(student.getId(), course.getId()));
    }

    public void drop(int studentId, int courseId) {
        if (studentId <= 0) throw new InvalidInputException("studentId must be > 0");
        if (courseId <= 0) throw new InvalidInputException("courseId must be > 0");

        enrollmentRepo.findByStudentAndCourse(studentId, courseId)
                .orElseThrow(() -> new NotFoundException("Enrollment not found for studentId=" + studentId + ", courseId=" + courseId));

        enrollmentRepo.deleteByStudentAndCourse(studentId, courseId);
    }

    public List<Enrollment> listAllEnrollments() {
        return enrollmentRepo.findAll();
    }

    public List<Enrollment> listEnrollmentsForCourse(int courseId) {
        if (courseId <= 0) throw new InvalidInputException("courseId must be > 0");
        return enrollmentRepo.findByCourseId(courseId);
    }


    public StudentSchedule viewStudentSchedule(int studentId) {
        if (studentId <= 0) throw new InvalidInputException("studentId must be > 0");


        studentRepo.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));

        List<Enrollment> enrollments = enrollmentRepo.findByStudentId(studentId);

        List<Course> courses = enrollments.stream()
                .map(e -> courseRepo.findById(e.getCourseId()).orElse(null))
                .filter(c -> c != null)

                .sorted(Comparator
                        .comparing(Course::getDayOfWeek, String.CASE_INSENSITIVE_ORDER)
                        .thenComparingInt(Course::getStartMinute))
                .collect(Collectors.toList());

        return new StudentScheduleBuilder()
                .studentId(studentId)
                .addCourses(courses)
                .build();
    }

    // helper
    private boolean isTimeConflict(Course a, Course b) {
        if (a.getDayOfWeek() == null || b.getDayOfWeek() == null) return false;
        if (!a.getDayOfWeek().equalsIgnoreCase(b.getDayOfWeek())) return false;

        // overlap check
        return a.getStartMinute() < b.getEndMinute() && b.getStartMinute() < a.getEndMinute();
    }
}
