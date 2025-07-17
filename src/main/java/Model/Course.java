package model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Updated column name to 'id_course'
    private int idCourse;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "teacher_id")
    private int idTeacher; // Renamed to idTeacher

    @Column(name = "thumbnail")
    private String thumbnail;

    // Correct: mappedBy points to the 'course' field in the Lecture entity
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lecture> lectures = new ArrayList<>();

    // Correct: mappedBy points to the 'course' field in the Assignment entity
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Assignment> assignments = new ArrayList<>();

    // Constructors, Getters, and Setters remain the same...
    public Course() {}

    public Course(String name, String description, int idTeacher, String thumbnail) {
        this.name = name;
        this.description = description;
        this.idTeacher = idTeacher;
        this.thumbnail = thumbnail;
    }

    // ... other getters and setters
    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getidTeacher() {
        return idTeacher;
    }

    public void setidTeacher(int idTeacher) {
        this.idTeacher = idTeacher;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}