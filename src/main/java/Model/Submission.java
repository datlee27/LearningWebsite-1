package model;

public class Submission {
    private int id;
    private Assignment assignment;
    private User student;
    private String content;
    private String submittedAt;
    private Double grade; // nullable until graded
    // getters/setters

    public Submission() {
    }

    public Submission(int id, Assignment assignment, User student, String content, String submittedAt, Double grade) {
        this.id = id;
        this.assignment = assignment;
        this.student = student;
        this.content = content;
        this.submittedAt = submittedAt;
        this.grade = grade;
    }

    public int getId() {
      return this.id;
    }
    public void setId(int value) {
      this.id = value;
    }

    public Assignment getAssignment() {
      return this.assignment;
    }
    public void setAssignment(Assignment value) {
      this.assignment = value;
    }

    public User getStudent() {
      return this.student;
    }
    public void setStudent(User value) {
      this.student = value;
    }

    public String getContent() {
      return this.content;
    }
    public void setContent(String value) {
      this.content = value;
    }

    public String getSubmittedAt() {
      return this.submittedAt;
    }
    public void setSubmittedAt(String value) {
      this.submittedAt = value;
    }

    public Double getGrade() {
      return this.grade;
    }
    public void setGrade(Double value) {
      this.grade = value;
    }
}
