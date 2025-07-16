package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Lectures")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLecture;

    // The primitive idCourse field is replaced with a direct object relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "title")
    private String title;

    @Column(name = "video_url")
    private String videoUrl;
    
    // Note: The original DAO had a 'content' parameter in saveLecture, 
    // but the entity was missing this field. I've added it here.
    @Lob // Use @Lob for potentially large text fields
    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private String status;

    public Lecture() {
    }

    // Constructor updated to accept a Course object
    public Lecture(Course course, String title, String content, String videoUrl, String status) {
        this.course = course;
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.status = status;
    }

    // --- Getters and Setters ---

    public int getIdLecture() {
        return idLecture;
    }

    public void setIdLecture(int idLecture) {
        this.idLecture = idLecture;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}