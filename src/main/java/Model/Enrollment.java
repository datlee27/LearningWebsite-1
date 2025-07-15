package model;

public class Enrollment {
    private int id;
    private User student;
    private Course course;
    private String enrollmentDate;
    // getters/setters

    public Enrollment() {
    }

    public Enrollment(int id, User student, Course course, String enrollmentDate) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
    }

    public int getId() {
      return this.id;
    }
    public void setId(int value) {
      this.id = value;
    }

    public User getStudent() {
      return this.student;
    }
    public void setStudent(User value) {
      this.student = value;
    }

    public Course getCourse() {
      return this.course;
    }
    public void setCourse(Course value) {
      this.course = value;
    }

    public String getEnrollmentDate() {
      return this.enrollmentDate;
    }
    public void setEnrollmentDate(String value) {
      this.enrollmentDate = value;
    }}
