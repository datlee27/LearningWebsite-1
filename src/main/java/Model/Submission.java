package model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "assignment_id")
    private int assignmentId;

    @Column(name = "student_id")
    private int studentId;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "submission_date")
    private LocalDateTime submissionDate;

    @Column(name = "grade")
    private Double grade;

    public Submission() {
    }

    public Submission(int id, int assignmentId, int studentId, String fileUrl, LocalDateTime submissionDate, Double grade) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.fileUrl = fileUrl;
        this.submissionDate = submissionDate;
        this.grade = grade;
    }

    public int getId() {
      return this.id;
    }
    public void setId(int value) {
      this.id = value;
    }

    public int getAssignmentId() {
      return this.assignmentId;
    }
    public void setAssignmentId(int value) {
      this.assignmentId = value;
    }

    public int getStudentId() {
      return this.studentId;
    }
    public void setStudentId(int value) {
      this.studentId = value;
    }

    public String getFileUrl() {
      return this.fileUrl;
    }
    public void setFileUrl(String value) {
      this.fileUrl = value;
    }

    public LocalDateTime getSubmissionDate() {
      return this.submissionDate;
    }
    public void setSubmissionDate(LocalDateTime value) {
      this.submissionDate = value;
    }

    public Double getGrade() {
      return this.grade;
    }
    public void setGrade(Double value) {
      this.grade = value;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", assignmentId=" + assignmentId +
                ", studentId=" + studentId +
                ", fileUrl='" + fileUrl + '\'' +
                ", submissionDate=" + submissionDate +
                ", grade=" + grade +
                '}';
    }
}
